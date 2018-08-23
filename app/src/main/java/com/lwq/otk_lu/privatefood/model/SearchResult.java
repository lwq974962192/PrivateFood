/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood.model;

public class SearchResult {
    private String title;
    private String content;
    private String imgurl;
    private int viewNum;
    private String view_num;

    public SearchResult() {
    }

    public SearchResult(String title, String content, String imgurl, int viewNum) {
        this.title = title;
        this.content = content;
        this.imgurl = imgurl;
        this.viewNum = viewNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public String getView_num() {
        return viewNum+"人浏览";
    }

    public void setView_num(String view_num) {
        this.view_num = view_num;
    }
}
