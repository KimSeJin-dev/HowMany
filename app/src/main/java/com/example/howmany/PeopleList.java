package com.example.howmany;

public class PeopleList {
    private String name; // 분반
    private String major; // 학수번호
    private String phone_num; // 수업이름

    public PeopleList(){}

    public void setName(String name) {
        this.name = name;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public PeopleList(String name, String major, String phone_num){
        this.name = name;
        this.major = major;
        this.phone_num = phone_num;
    }
    public String getName() {return name;}
    public String getMajor() {
        return major;
    }
    public String getPhone_num() {
        return phone_num;
    }

}