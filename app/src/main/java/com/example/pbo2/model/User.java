package com.example.pbo2.model;

public class User {
    // Atribut
    private String name;
    private String phoneNumber;
    private int age;
    private String role;

    // Konstruktor
    public User(String name, String phoneNumber, int age, String role) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.role = role;
    }

    // Getter dan Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
