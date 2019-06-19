package teqvirtual.deep.healthcare.FragmentModels;

public class Request {
    String request_id,your_id,friend_id,request_type;

    public Request() {
    }

    public Request(String request_id, String your_id, String friend_id, String request_type) {
        this.request_id = request_id;
        this.your_id = your_id;
        this.friend_id = friend_id;
        this.request_type = request_type;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getYour_id() {
        return your_id;
    }

    public void setYour_id(String your_id) {
        this.your_id = your_id;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }
}
