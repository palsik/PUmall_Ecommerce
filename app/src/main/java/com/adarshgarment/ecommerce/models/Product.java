package com.adarshgarment.ecommerce.models;



//public class Product {
//        private String productCategory, productDescription, productPrice,productTitle, ProductIcon, productId, timestamp, userId;
//
//       // Empty Constructor
//
//    public Product(String productCategory, String productDescription, String productPrice, String productTitle, String productIcon,
//                   String productId, String timestamp, String userId ) {
//        this.productCategory = productCategory;
//        this.productPrice = productPrice;
//        this.productDescription = productDescription;
//        this.productTitle = productTitle;
//        this.ProductIcon = productIcon;
//        this.productId = productId;
//        this.timestamp = timestamp;
//        this.userId = userId;
//    }
//
//    public String getProductCategory() {
//        return productCategory;
//    }
//
//    public void setProductCategory(String productCategory) {
//        this.productCategory = productCategory;
//    }
//
//    public String getProductDescription() {
//        return productDescription;
//    }
//
//    public void setProductDescription(String productDescription) {
//        this.productDescription = productDescription;
//    }
//
//    public String getProductTitle() {
//        return productTitle;
//    }
//
//    public void setProductTitle(String productTitle) {
//        this.productTitle = productTitle;
//    }
//
//    public String getProductPrice() {
//        return productPrice;
//    }
//
//    public void setProductPrice(String productPrice) {
//        this.productPrice = productPrice;
//    }
//
//    public String getProductIcon() {
//        return ProductIcon;
//    }
//
//    public void setProductIcon(String productIcon) {
//        ProductIcon = productIcon;
//    }
//
//    public String getProductId() {
//        return productId;
//    }
//
//    public void setProductId(String productId) {
//        this.productId = productId;
//    }
//
//    public String getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(String timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//}

import java.util.ArrayList;

public class Product {

    public Product()
    {

    }

    private String productCategory, productDescription,productTitle, Links, uid,productIcon;
    private long productId;
    private long timestamp;
    private double Price;
    private String product_status;
    private int productQuantity;

    public long getProductId() {
        return productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public String getProductCategory() {
        return productCategory;
    }


    public double getPrice() {
        return Price;
    }

    public String getLinks() {
        return Links;
    }

    public String getproductIcon() {
        return productIcon;
    }

    public long gettimestamp() {
        return timestamp;
    }

    public String getuid() {
        return uid;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

//    private static int lastContactId = 0;
//
//    public static ArrayList<Product> createContactsList(int numContacts) {
//        ArrayList<Product> products1 = new ArrayList<Product>();
//
//        for (int i = 1; i <= numContacts; i++) {
//            products1.add(new Product("Person " + ++lastContactId, i <= numContacts / 2));
//        }
//
//        return contacts;
//    }

}