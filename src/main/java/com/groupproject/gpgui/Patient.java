package com.groupproject.gpgui;

public class Patient {

    private final int id;
    private final String name, phone, email, address, doctorName;

    public Patient(int id, String name, String phone, String email,
                   String address, String doctorName){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.doctorName = doctorName;
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public String getPhone() {return phone;}
    public String getEmail() {return email;}
    public String getAddress() {return address;}
    public String getDoctorName() {return doctorName;}
}