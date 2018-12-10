package DataBase;

import java.io.Serializable;

public class Message implements Serializable {
    public int messageId;
    public int fromId;
    public int toId;
    public String message;
    public Message(){};
    public Message(int _fromId, int _toId, String _message){
        fromId = _fromId;
        toId = _toId;
        message = _message;
    }
    void setToId(int id){
        toId = id;
    }
    void setFromId(int id){
        fromId = id;
    }
    void setMessageId(int id){
        messageId = id;
    }
    void setMessage(String _message){
        message = _message;
    }
}
