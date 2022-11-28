package com.web.ajmerainfotech;

public class AuthorResponse {

    String id,authorname,bookname,bookprice,createdate;

    public AuthorResponse(String id, String authorname, String bookname, String bookprice, String createdate) {
        this.id = id;
        this.authorname = authorname;
        this.bookname = bookname;
        this.bookprice = bookprice;
        this.createdate = createdate;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookprice() {
        return bookprice;
    }

    public void setBookprice(String bookprice) {
        this.bookprice = bookprice;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
