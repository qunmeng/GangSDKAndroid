package com.qm.gangsdk.ui.entity;

/**
 * Author: mengbo
 * Time: 2017/11/9 14:48
 * Description: 片段字符串
 */

public class SpannableStringBean {

    private String text = "";
    private int color = 0;
    private String replaceText = "";
    private boolean splitable = false;

    public SpannableStringBean(String text, int color){
        this.text = text;
        this.color = color;
    }

    public SpannableStringBean(String text, String replaceText, int color){
        this.text = text;
        this.color = color;
        this.replaceText = replaceText;
    }

    public SpannableStringBean(String text, int color, boolean splitable){
        this.text = text;
        this.color = color;
        this.splitable = splitable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getReplaceText() {
        return replaceText;
    }

    public void setReplaceText(String replaceText) {
        this.replaceText = replaceText;
    }

    public boolean isSplitable() {
        return splitable;
    }

    public void setSplitable(boolean splitable) {
        this.splitable = splitable;
    }
}
