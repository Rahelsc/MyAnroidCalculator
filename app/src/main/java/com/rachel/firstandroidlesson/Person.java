package com.rachel.firstandroidlesson;

public class Person {
    private String email;
    private String phoneNumber;
    private String city;

    public Person(String email, String phoneNumber, String city) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.city = city;
    }

    public Person(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
