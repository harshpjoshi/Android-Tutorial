package com.dhanvi.fashionfusion1.model;

import com.dhanvi.fashionfusion1.R;

import java.util.ArrayList;
import java.util.List;

public class MyData {

    private static List<Product> list;
    private static List<Product> cartList;
    private static List<Product> wishList;
    static Product selectedProduct;
   public static List<Product> getList(){
       if (list==null){
           list = createList();
       }

       return list;
   }

    public static List<Product> getCartList(){
        if (cartList==null){
            cartList = new ArrayList<>();
        }

        return cartList;
    }
    public static List<Product> getWishList() {
        if (wishList==null){
            wishList = new ArrayList<>();
        }

        return wishList;
    }


    public static Product getSelectedProduct() {
        return selectedProduct;
    }

    public static void setSelectedProduct(Product selectedProduct) {
        MyData.selectedProduct = selectedProduct;
    }

    private static List<Product> createList() {
       setImages();
        List<Product> list = new ArrayList<>();

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 10; j++) {
                Product product = new Product();
                product.setCategory_id(i-1+"");
                product.setName("item"+i+""+j);
                product.setDescription("Description"+i+""+j);
                product.setPrice(i*10 + j*10 +"");
                product.setSize(i + j +"");
                product.setImage(images[i-1][j-1]);
                list.add(product);
            }
        }

        return list;
    }

    private static void setImages() {
//category 0
        images[0][0] = R.drawable.red;
        images[0][1] = R.drawable.mm;
        images[0][2] = R.drawable.shopping;
        images[0][3] = R.drawable.time;
        images[0][4] = R.drawable.dhanvipatel;
        images[0][5] = R.drawable.red;
        images[0][6] = R.drawable.mm;
        images[0][7] = R.drawable.mom;
        images[0][8] = R.drawable.ghatha;
        images[0][9] = R.drawable.hh;

//category 1
       images[1][0] = R.drawable.login;
       images[1][1] = R.drawable.joy;
       images[1][2] = R.drawable.shopping;
       images[1][3] = R.drawable.time;
       images[1][4] = R.drawable.dhanvipatel;
       images[1][5] = R.drawable.red;
       images[1][6] = R.drawable.mm;
       images[1][7] = R.drawable.mom;
       images[1][8] = R.drawable.ghatha;
       images[1][9] = R.drawable.hh;
//category 2
       images[2][0] = R.drawable.dhanvipatel;
       images[2][1] = R.drawable.mom;
       images[2][2] = R.drawable.shopping;
       images[2][3] = R.drawable.time;
       images[2][4] = R.drawable.dhanvipatel;
       images[2][5] = R.drawable.red;
       images[2][6] = R.drawable.mm;
       images[2][7] = R.drawable.mom;
       images[2][8] = R.drawable.ghatha;
       images[2][9] = R.drawable.hh;
//category 3
       images[3][0] = R.drawable.ghatha;
       images[3][1] = R.drawable.hh;
       images[3][2] = R.drawable.shopping;
       images[3][3] = R.drawable.time;
       images[3][4] = R.drawable.dhanvipatel;
       images[3][5] = R.drawable.red;
       images[3][6] = R.drawable.mm;
       images[3][7] = R.drawable.mom;
       images[3][8] = R.drawable.ghatha;
       images[3][9] = R.drawable.hh;


    }

    public static Integer[][] images = new Integer[4][10];

    public static List<Product> getListOfCategory(String categoryNo) {
        if (list==null){
            list = createList();
        }

        return filterList(list,categoryNo);
    }

    private static List<Product> filterList(List<Product> list, String categoryNo) {
        List<Product> newList = new ArrayList<>();
        for (Product product : list) {
            if (product.getCategory_id().equals(categoryNo)){
                newList.add(product);
            }
        }
        return newList;
    }

    public static String getPrice(String price) {
        return "Rs "+price;
    }




//   {
//            R.drawable.login, R.drawable.joy,
//            R.drawable.time, R.drawable.shopping,
//            R.drawable.dhanvi, R.drawable.fav,
//            R.drawable.splash, R.drawable.fashion,
//            R.drawable.dp, R.drawable.dhanvipatel,
//            R.drawable.mango, R.drawable.red,
//            R.drawable.het, R.drawable.hetu,
//            R.drawable.mm, R.drawable.mom,
//            R.drawable.het, R.drawable.time,
//            R.drawable.hh, R.drawable.ghatha
//    };
}
