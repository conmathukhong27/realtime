package org.example.redis;

import org.apache.log4j.Logger;
import org.example.model.EventContent;
import org.example.model.EventTrack;
import org.example.model.Events;
import org.example.model.NotiCMP;
import org.example.process.dictionary.EventTrackingContentDict;
import org.example.utils.CustomerTestUtils;
import org.example.utils.DateTimeUtils;
import org.example.utils.JedisStorage;
import org.example.utils.SmartProducer;
import redis.clients.jedis.JedisPubSub;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class KeyExpiredListener extends JedisPubSub {
    private static final Logger LOGGER = Logger.getLogger(KeyExpiredListener.class);
    public static final Gson gson = new Gson();
    //    private List<String> list;
    private final SmartProducer producer;
    private final JedisStorage jedisStorage;
    private int count;
    private static final int DEFAULT_EXPIRE_TIME = 2678400;

    private final String NOTI_LOG_PATH = "/u01/cskh_customer/bin/noti_send_log.txt";

    public KeyExpiredListener(SmartProducer producer, JedisStorage jedisStorage) {
        this.producer = producer;
        this.jedisStorage = jedisStorage;
//        list = new ArrayList<>();
//        list.add("84359366164");
        count = 0;
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        LOGGER.info("onPSubscribe "
                + pattern + " " + subscribedChannels);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        this.count = this.count + 1;
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Chuyển đổi thời gian hiện tại sang định dạng chuỗi
        String formattedTimestamp = currentDateTime.format(formatter);
        String formattedDateTimestamp = currentDateTime.format(formatterDate);
        // In ra timestamp
//        LOGGER.info("Thue bao expire" + this.count + ": " + message.split("#")[0] + "----" + formattedTimestamp);
        String[] messageArray = message.split("#");

        EventContent search = EventTrackingContentDict.search(messageArray[1]);
        if (search != null) {
            EventTrack event = this.jedisStorage.getObject(messageArray[0] + "#" + search.getProduct() + "#" + DateTimeUtils.getCurrentMonth(), EventTrack.class);
            //check customer receive in this month existed in redis
            if (event == null) {
                NotiCMP notiCMP = new NotiCMP(messageArray[0], search.getContentId(), DateTimeUtils.getLastDateCM());
                // push noti customer to CM
                boolean productPush = producer.putMessage(gson.toJson(notiCMP));
                //check push success
                if (productPush) {
                    LOGGER.info("Successful push " + messageArray[0] + "to CEM");
                    // save customer receive noti to redis in this month
                    this.jedisStorage.putObject(messageArray[0] + "#" + search.getProduct() + "#" + DateTimeUtils.getCurrentMonth(), new EventTrack(messageArray[0], messageArray[1], search.getProduct(), DateTimeUtils.getLastDateCM()), DEFAULT_EXPIRE_TIME);
                    //save log sen noti
                    String log = messageArray[0] + "," + messageArray[1] + "," + formattedTimestamp + "," + formattedDateTimestamp + "," + search.getContentId() + "," + search.getProduct();
                    try {
                        FileWriter fileWriter = new FileWriter(NOTI_LOG_PATH, true);
                        fileWriter.write(log + "\n");
                        fileWriter.close();
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                }
            } else {
                LOGGER.info("Customer " + messageArray[0] + " has already received the notification");
            }
        }

    }

}
