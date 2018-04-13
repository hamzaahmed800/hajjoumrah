package com.ziaetaiba.ziaehajjandumrah.models;

/**
 * Created by HAMI on 10/04/2018.
 */

public class Book {

    private int id;
    private int book_detailcategory_id;
    private String book_name;
    private String book_imageLink;
    private String book_downloadLink;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBook_detailcategory_id() {
        return book_detailcategory_id;
    }

    public void setBook_detailcategory_id(int book_detailcategory_id) {
        this.book_detailcategory_id = book_detailcategory_id;
    }
    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_imageLink() {
        return book_imageLink;
    }

    public void setBook_imageLink(String book_imageLink) {
        this.book_imageLink = book_imageLink;
    }

    public String getBook_downloadLink() {
        return book_downloadLink;
    }

    public void setBook_downloadLink(String book_downloadLink) {
        this.book_downloadLink = book_downloadLink;
    }
}
