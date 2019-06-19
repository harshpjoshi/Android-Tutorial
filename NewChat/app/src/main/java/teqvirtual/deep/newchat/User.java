package teqvirtual.deep.newchat;

public class User {
    String myid,name,email,phonenumber,image,status,token,screnn_status;

    public User() {
    }

    public User(String myid, String name, String email, String phonenumber, String image, String status, String token, String screnn_status) {
        this.myid = myid;
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
        this.image = image;
        this.status = status;
        this.token = token;
        this.screnn_status = screnn_status;
    }

    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getScrenn_status() {
        return screnn_status;
    }

    public void setScrenn_status(String screnn_status) {
        this.screnn_status = screnn_status;
    }

    @Override
    public String toString() {
        return "User{" +
                "myid='" + myid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", image='" + image + '\'' +
                ", status='" + status + '\'' +
                ", token='" + token + '\'' +
                ", screnn_status='" + screnn_status + '\'' +
                '}';
    }
}
