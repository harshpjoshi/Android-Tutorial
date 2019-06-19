package teqvirtual.deep.firebasechatapp.Model;

public class UserData {



    String name,email,username,password,image,status,screenstatus;

    public UserData() {
    }

    public UserData(String name, String email, String username, String password, String image, String status,String screenstatus) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.image = image;
        this.status = status;
        this.screenstatus=screenstatus;
    }

    public String getScreenstatus() {
        return screenstatus;
    }

    public void setScreenstatus(String screenstatus) {
        this.screenstatus = screenstatus;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public String toString() {
        return "UserData{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                ", status='" + status + '\'' +
                ", screenstatus='" + screenstatus + '\'' +
                '}';
    }
}
