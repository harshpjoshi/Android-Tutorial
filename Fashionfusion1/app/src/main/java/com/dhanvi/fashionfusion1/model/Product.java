package com.dhanvi.fashionfusion1.model;

public class Product {


     /* Initialisation */

  private String id = null;
  private String name = "";
  private int image ;
  private String price = "";
  private String size = "";
  private String category_id = "";
  private String description = "";

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    /* Setter */

  public void setId(String id) {
     this.id = id;
  }

  public void setName(String name) {
     this.name = name;
  }

  public void setImage(int image) {
     this.image = image;
  }

  public void setPrice(String price) {
     this.price = price;
  }

  public void setCategory_id(String category_id) {
     this.category_id = category_id;
  }

  public void setDescription(String description) {
     this.description = description;
  }

     /* Get table name */
  public static String getTableName() {
    return "product";
  }

     /* Getter */

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getImage() {
    return image;
  }

  public String getPrice() {
    return price;
  }

  public String getCategory_id() {
    return category_id;
  }

  public String getDescription() {
    return description;
  }

     /* Table Name String */

   public class s {
       public static final String id = "id";
       public static final String name = "name";
       public static final String image = "image";
       public static final String price = "price";
       public static final String category_id = "category_id";
       public static final String description = "description";
   }

}
