package com.wyze.sandglasslibrary.moudle;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
/**
 * Created by wangjian on 2023/1/5
 */
@Entity(tableName = "chatbotmessage")
public class SLFChatBotMsgData {
    @PrimaryKey(autoGenerate = true)
    private int id;//数据库id
    private int faqId;//获得解答answer的id
    private long msgTime;//消息时间戳
    private int type;//消息类型
    private String title;//消息title
    private String content;//消息内容
    private String question;//已知问题
    private int msg_from;//谁发的消息
    private int answer_effective;//解答是否有效
    private int send_msg_status;//发送消息的状态
    private int question_index = 0;//问题的第一个索引
    private boolean isUpdate;//是否显示更新问题按钮
    @Ignore
    public SLFChatBotMsgData(){}
    public SLFChatBotMsgData (int id, int faqId, long msgTime, int type, String title, String content, String question, int msg_from, int answer_effective, int send_msg_status, int question_index) {
        this.id = id;
        this.faqId = faqId;
        this.msgTime = msgTime;
        this.type = type;
        this.title = title;
        this.content = content;
        this.question = question;
        this.msg_from = msg_from;
        this.answer_effective = answer_effective;
        this.send_msg_status = send_msg_status;
        this.question_index = question_index;
    }

    public enum MsgType{
        SINGLE_ROBOT_MSG(1),
        SINGLE_USER_MSG(2),
        FEEDBACK_ROBOT_MSG(3),
        SINGLE_TIME_MSG(4),
        HOT_ROBOT_MSG(5);

        private int value;
        MsgType (int value) {
            this.value = value;
        }

        public void setValue (int value) {
            this.value = value;
        }

        public int getValue ( ) {
            return value;
        }
    }
    public enum MsgFrom{
        FROM_USER(11),
        FROM_ROBOT(22);
        private int value;
        MsgFrom (int value) {
            this.value = value;
        }

        public void setValue (int value) {
            this.value = value;
        }

        public int getValue ( ) {
            return value;
        }
    }

    public enum AnswerEffective{
        ANSWER_NO_SELECT(111),
        ANSWER_NOEFFECTIVE(222),
        ANSWER_EFFECTIVE(333);
        private int value;
        AnswerEffective (int value) {
            this.value = value;
        }

        public void setValue (int value) {
            this.value = value;
        }

        public int getValue ( ) {
            return value;
        }
    }

    public enum MsgSendStatus{
        SENDED_MSG(1111),
        SEND_FAIL_MSG(2222),
        SENDING_MSG(3333);

        private int value;
        MsgSendStatus (int value) {
            this.value = value;
        }

        public void setValue (int value) {
            this.value = value;
        }

        public int getValue ( ) {
            return value;
        }
    }

    public void setId (int id) {
        this.id = id;
    }

    public void setFaqId (int faqId) {
        this.faqId = faqId;
    }

    public void setIsUpdate (boolean update) {
        isUpdate = update;
    }

    public void setQuestion_index (int question_index) {
        this.question_index = question_index;
    }

    public void setMsgTime (long msgTime) {
        this.msgTime = msgTime;
    }

    public void setType (int type) {
        this.type = type;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public void setQuestion (String question) {
        this.question = question;
    }

    public void setMsg_from (int msg_from) {
        this.msg_from = msg_from;
    }

    public void setAnswer_effective (int answer_effective) {
        this.answer_effective = answer_effective;
    }

    public void setSend_msg_status (int send_msg_status) {
        this.send_msg_status = send_msg_status;
    }

    public int getId ( ) {
        return id;
    }

    public int getFaqId ( ) {
        return faqId;
    }

    public boolean getIsUpdate ( ) {
        return isUpdate;
    }

    public int getQuestion_index ( ) {
        return question_index;
    }

    public long getMsgTime ( ) {
        return msgTime;
    }

    public int getType ( ) {
        return type;
    }

    public String getTitle ( ) {
        return title;
    }

    public String getContent ( ) {
        return content;
    }

    public String getQuestion ( ) {
        return question;
    }

    public int getMsg_from ( ) {
        return msg_from;
    }

    public int getAnswer_effective ( ) {
        return answer_effective;
    }

    public int getSend_msg_status ( ) {
        return send_msg_status;
    }

    @Override
    public String toString ( ) {
        return "SLFChatBotMsgData{" +
                "msgTime=" + msgTime +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", question='" + question + '\'' +
                ", msg_from=" + msg_from +
                ", answer_effective=" + answer_effective +
                ", send_msg_status=" + send_msg_status +
                '}';
    }
}
