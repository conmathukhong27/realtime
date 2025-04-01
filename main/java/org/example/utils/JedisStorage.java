package org.example.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.util.Pair;
import java.time.Duration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import org.apache.log4j.Logger;

import java.util.List;

public class JedisStorage {
    private static final Logger LOGGER = Logger.getLogger(JedisStorage.class);
    // expire after 12h
    private static final int DEFAULT_EXPIRE_TIME = 900;
    private final Gson gson;
    private JedisPool jedisPool;
    private static JedisStorage storage = null;

    /**
     *
     */
    private JedisStorage() {
        this.gson = new Gson();
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        try {
            this.jedisPool = new JedisPool(poolConfig, Resource.REDIS_RESOURCE.REDIS_HOST, Resource.REDIS_RESOURCE.REDIS_PORT, 30000, Resource.REDIS_RESOURCE.REDIS_PASSWORD, Resource.REDIS_RESOURCE.REDIS_DB_INDEX);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    /**
     *
     * @return
     */
    public synchronized static JedisStorage getInstance() {
        if (storage == null) {
            storage = new JedisStorage();
        }
        return storage;
    }

    /**
     *
     * @param <T>
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public <T> T putObject(String key, T value, int expireTime) {
        T object = null;
        Jedis jedis = null;
        try {
            String code;
            jedis = jedisPool.getResource();
            if (expireTime <= 0) {
                code = jedis.set(key, this.gson.toJson(value));
            } else {
                code = jedis.setex(key, expireTime, this.gson.toJson(value));
            }
            if ("OK".equalsIgnoreCase(code)) {
                object = value;
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return object;
    }

    /**
     *
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public String putString(String key, String value, int expireTime) {
        String result = null;
        Jedis jedis = null;
        try {
            String code;
            jedis = jedisPool.getResource();
            if (expireTime <= 0) {
                code = jedis.set(key, value);
            } else {
                code = jedis.setex(key, expireTime, value);
            }
            if ("OK".equalsIgnoreCase(code)) {
                result = value;
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     *
     * @param <T>
     * @param key
     * @param value
     * @return
     */
    public <T> T putObject(String key, T value) {
        return putObject(key, value, DEFAULT_EXPIRE_TIME);
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public String putString(String key, String value) {
        return putString(key, value, DEFAULT_EXPIRE_TIME);
    }

    /**
     *
     * @param <T>
     * @param key
     * @param clazz
     * @return
     */
    public <T> T getObject(String key, Class<T> clazz) {
        T object = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            if (value != null) {
                object = this.gson.fromJson(value, clazz);
            }
        } catch (JsonSyntaxException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return object;
    }

    /**
     *
     * @param key
     * @return
     */
    public String getAsString(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        } catch (JsonSyntaxException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }

    /**
     *
     * @param key
     * @return
     */
    public Long getAsLong(String key) {
        Long result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            if (value != null) {
                result = Long.parseLong(value);
            }
        } catch (JsonSyntaxException | NumberFormatException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     *
     * @param <T>
     * @param key
     * @param clazz
     * @return
     */
    public <T> T popObject(String key, Class<T> clazz) {
        T object = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            if (value != null) {
                object = this.gson.fromJson(value, clazz);
            }
            jedis.del(key);
        } catch (JsonSyntaxException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return object;
    }

    /**
     *
     * @param key
     * @return
     */
    public String popAsString(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
            jedis.del(key);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }

    /**
     *
     * @param key
     * @return
     */
    public Long popAsLong(String key) {
        Long result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            if (value != null) {
                result = Long.parseLong(value);
            }
            jedis.del(key);
        } catch (NumberFormatException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     *
     * @param key
     * @return
     */
    public Integer popAsInteger(String key) {
        Integer result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            if (value != null) {
                result = Integer.parseInt(value);
            }
            jedis.del(key);
        } catch (NumberFormatException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     *
     * @param key
     */
    public void delObject(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     *
     * @param keys
     */
    public void delObject(List<String> keys) {
        Jedis jedis = null;
        try {
            if ((keys != null) && (!keys.isEmpty())) {
                jedis = jedisPool.getResource();
                jedis.del(keys.toArray(new String[keys.size()]));
            }
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     *
     * @return
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     *
     * @param jedis
     */
    public void release(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     *
     * @param pairs
     */
    public void insert(List<Pair<String, String>> pairs) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Pipeline pipeline = jedis.pipelined();
            for (Pair<String, String> pair : pairs) {
                pipeline.setex(pair.getKey(), DEFAULT_EXPIRE_TIME, pair.getValue());
            }
            pipeline.sync();
            pipeline.close();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    public int getDatabaseIndex(){
        return getJedis().getDB();
    }
}
