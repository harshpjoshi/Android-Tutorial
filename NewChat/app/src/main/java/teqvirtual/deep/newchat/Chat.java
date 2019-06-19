package teqvirtual.deep.newchat;

public class Chat {
    String receiver,sender,message,time;


    public Chat() {
    }

    public Chat(String receiver, String sender, String message, String time) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
        this.time = time;
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
