package teqvirtual.deep.healthcare.Model;

public class Chat {
    String receiver,sender,message,time,type,chat_id;


    public Chat() {
    }

    public Chat(String receiver, String sender, String message, String time, String type, String chat_id) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
        this.time = time;
        this.type = type;
        this.chat_id = chat_id;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
