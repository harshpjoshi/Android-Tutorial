package teqvirtual.deep.healthcare.Model;

public class SubscriberModel {
    String id;
    String firstname;
    String lastname;
    String email;
    String mobileno;
    String image;
    String city;
    String address;
    String token;
    String f_subscriber_id;

    public SubscriberModel(String id, String firstname, String lastname, String email, String mobileno, String image, String city, String address, String token, String f_subscriber_id) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.mobileno = mobileno;
        this.image = image;
        this.city = city;
        this.address = address;
        this.token = token;
        this.f_subscriber_id = f_subscriber_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getF_subscriber_id() {
        return f_subscriber_id;
    }

    public void setF_subscriber_id(String f_subscriber_id) {
        this.f_subscriber_id = f_subscriber_id;
    }

    public SubscriberModel() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
