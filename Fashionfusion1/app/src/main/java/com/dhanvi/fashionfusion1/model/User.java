package com.dhanvi.fashionfusion1.model;

public class User {


     /* Initialisation */

  private String id = null;
  private String name = "";
  private String email = "";
  private String mobile = "";
  private String address = "";
  private String password = "";

     /* Setter */

  public void setId(String id) {
     this.id = id;
  }

  public void setName(String name) {
     this.name = name;
  }

  public void setEmail(String email) {
     this.email = email;
  }

  public void setMobile(String mobile) {
     this.mobile = mobile;
  }

  public void setAddress(String address) {
     this.address = address;
  }

  public void setPassword(String password) {
     this.password = password;
  }

     /* Get table name */
  public static String getTableName() {
    return "user";
  }

     /* Getter */

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getMobile() {
    return mobile;
  }

  public String getAddress() {
    return address;
  }

  public String getPassword() {
    return password;
  }

     /* Table Name String */

   public class s {
       public static final String id = "id";
       public static final String name = "name";
       public static final String email = "email";
       public static final String mobile = "mobile";
       public static final String address = "address";
       public static final String password = "password";
   }

}
