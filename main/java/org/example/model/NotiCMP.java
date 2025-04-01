package org.example.model;

public class NotiCMP {
    private String MSISDN;
    private String CMP_SEGMENT_ID;
    private String expire_time;

    public NotiCMP(String MSISDN, String CMP_SEGMENT_ID) {
        this.MSISDN = MSISDN;
        this.CMP_SEGMENT_ID = CMP_SEGMENT_ID;
    }

    public NotiCMP(String MSISDN, String CMP_SEGMENT_ID, String expire_time) {
        this.MSISDN = MSISDN;
        this.CMP_SEGMENT_ID = CMP_SEGMENT_ID;
        this.expire_time = expire_time;
    }

    public String getMSISDN() {
        return MSISDN;
    }

    public String getCMP_SEGMENT_ID() {
        return CMP_SEGMENT_ID;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }

    public void setCMP_SEGMENT_ID(String CMP_SEGMENT_ID) {
        this.CMP_SEGMENT_ID = CMP_SEGMENT_ID;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    @Override
    public String toString() {
        return "NotiCMP{" +
                "MSISDN='" + MSISDN + '\'' +
                ", CMP_SEGMENT_ID='" + CMP_SEGMENT_ID + '\'' +
                ", expire_time='" + expire_time + '\'' +
                '}';
    }
}
