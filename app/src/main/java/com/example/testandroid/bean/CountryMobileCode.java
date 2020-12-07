package com.example.testandroid.bean;

import java.text.Collator;

public class CountryMobileCode implements Comparable<CountryMobileCode> {

    private static final int TITLE_TYPE = 2;
    private static final int MOBILE_CODE_TYPE = 1;

    private String name;
    private String english_name;
    private String name_code;
    private String phone_code;
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getName_code() {
        return name_code;
    }

    public void setName_code(String name_code) {
        this.name_code = name_code;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTitleType(){
        this.type = TITLE_TYPE;
    }

    public void setMobileCodeType(){
        this.type = MOBILE_CODE_TYPE;
    }

    public boolean isTitleType(){
        return this.type == TITLE_TYPE;
    }

    @Override
    public int compareTo(CountryMobileCode o) {
        return Collator.getInstance().compare(getName_code(), o.getName_code());
    }
}
