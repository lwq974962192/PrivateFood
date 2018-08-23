/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood.model;

public class MoreSetting {
    private int pic;
    private String title;

    public MoreSetting() {
    }

    public MoreSetting(int pic, String title) {
        this.pic = pic;
        this.title = title;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
