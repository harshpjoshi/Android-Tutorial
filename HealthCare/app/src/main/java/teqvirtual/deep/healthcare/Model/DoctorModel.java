package teqvirtual.deep.healthcare.Model;

public class DoctorModel {
    String id,firstname,lastname,image,address,city,type,email,password,mobileno,latitude,longitude,token,f_doctor_id;

    public DoctorModel() {
    }

    public DoctorModel(String id, String firstname, String lastname, String image, String address, String city, String type, String email, String password, String mobileno, String latitude, String longitude, String token, String f_doctor_id) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.image = image;
        this.address = address;
        this.city = city;
        this.type = type;
        this.email = email;
        this.password = password;
        this.mobileno = mobileno;
        this.latitude = latitude;
        this.longitude = longitude;
        this.token = token;
        this.f_doctor_id = f_doctor_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getF_doctor_id() {
        return f_doctor_id;
    }

    public void setF_doctor_id(String f_doctor_id) {
        this.f_doctor_id = f_doctor_id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
