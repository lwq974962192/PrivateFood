/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood.model;

public class Boutique {
    private int id;
    private String cate;
    private String name;
    private String imgurl;

    public Boutique() {
    }

    public Boutique(int id, String cate, String name, String imgurl) {
        this.id = id;
        this.cate = cate;
        this.name = name;
        this.imgurl = imgurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
