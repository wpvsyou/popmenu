package com.wp.demo.menupopup;

import android.content.ContentValues;
import android.net.Uri;

/**
 * Created by wangpeng on 16-1-3.
 */
public class Message {

    public final static String TABLE_NAME = "message";
    public final static String MESSAGE_DATA = "message_data";
    public final static String TIME = "time";
    public final static String MINI_TYPE = "mini_type";
    public final static String _ID = "_id";

    public final static Uri
            URI_MESSAGE = Uri.parse("content://" + MyProvider.AUTHORITY + "/" + TABLE_NAME);

    public Message(String messageData, String time, String miniType) {
        this.messageData = messageData;
        this.time = time;
        this.miniType = miniType;
    }

    private String messageData;
    private String time;
    private String miniType;

    public String getMessageData() {
        return messageData;
    }

    public void setMessageData(String messageData) {
        this.messageData = messageData;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMiniType() {
        return miniType;
    }

    public void setMiniType(String miniType) {
        this.miniType = miniType;
    }

    public ContentValues toValues() {
        final ContentValues values = new ContentValues();
        values.put(MESSAGE_DATA, this.messageData);
        values.put(TIME, this.time);
        values.put(MINI_TYPE, this.miniType);
        return values;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageData='" + messageData + '\'' +
                ", time='" + time + '\'' +
                ", miniType='" + miniType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (messageData != null ? !messageData.equals(message.messageData) : message.messageData != null)
            return false;
        if (time != null ? !time.equals(message.time) : message.time != null) return false;
        return !(miniType != null ? !miniType.equals(message.miniType) : message.miniType != null);

    }

    @Override
    public int hashCode() {
        int result = messageData != null ? messageData.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (miniType != null ? miniType.hashCode() : 0);
        return result;
    }
}
