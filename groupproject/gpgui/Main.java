package com.groupproject.gpgui;
import javafx.application.Application;

import java.sql.*;
import java.util.Scanner;



public class Main {

    public static void main(String[] args){
        int run=1;
        int ID=1;
        String url = "jdbc:sqlite:doctorsoffice.db";

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    while(run==1){
                        Scanner scanner = new Scanner(System.in);
                        Statement stmt = conn.createStatement();
                        //make tables if they dont exist
                        stmt.execute("CREATE TABLE IF NOT EXISTS patient ("+"id INTEGER PRIMARY KEY AUTOINCREMENT, "+"name TEXT, " +"phone TEXT, "+ "email TEXT, " + "address TEXT, " +  "doctor_name TEXT" +  ");");
                        System.out.println("what would you like to do");
                        System.out.println("Option 1: Add new patient ,\n Option 2: Update Patient,\n Option 3: View patient \n Option 4:Exit");
                        int op = scanner.nextInt();
                        scanner.nextLine();
                        switch(op){
                            case 1:
                                System.out.println("What is the paitent name");
                                String  name = scanner.nextLine();
                                System.out.println("What is the Paitents phone number");
                                String phone=scanner.nextLine();
                                System.out.println("What is the email of the Paitent");
                                String email= scanner.nextLine();
                                System.out.println("What is the address of the Paitent");
                                String adress=scanner.nextLine();
                                System.out.println("What is the doctors name");
                                String docname=scanner.nextLine();

                                addPatient(conn,name,adress,email,phone,docname);

                                break;
                            case 2:
                                System.out.println("Whats the Paitent ID pf the Paitent you'd like to update?");
                                int search=scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("What is the paitent name");
                                String  newname = scanner.nextLine();
                                System.out.println("What is the Paitents phone number");
                                String newphone=scanner.nextLine();
                                System.out.println("What is the email of the Paitent");
                                String newemail= scanner.nextLine();
                                System.out.println("What is the address of the Paitent");
                                String newadress=scanner.nextLine();
                                System.out.println("What is the doctors name");
                                String newdocname=scanner.nextLine();
                                updatePatient(conn, search, newname, newadress, newemail, newphone, newdocname);
                                break;
                            case 3:
                                System.out.println("Whats the ID of the patient your searching for?");
                                int search2=scanner.nextInt();
                                searchPatient(conn,search2);
                                break;
                            case 4:
                                run=0;

                        }

                    }

                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found. Add the JAR to your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed.");
            e.printStackTrace();
        }

    }
    // add patient
    public static void addPatient(Connection conn,String Name,String adressString,String email,String phone,String drname) throws SQLException{
        String sql = "INSERT INTO patient (name,phone,email,address,doctor_name) VALUES (?, ?,?,?,?)";
        try (PreparedStatement pstmt=conn.prepareStatement(sql)){

            pstmt.setString(1,Name);
            pstmt.setString(2,phone);
            pstmt.setString(3,email);
            pstmt.setString(4,adressString);
            pstmt.setString(5,drname);
            pstmt.executeUpdate();
            System.out.println("added!");
        }
    }
    //updatePaitent
    public static void updatePatient(Connection conn,int ID,String Name,String adressString,String email,String phone,String drname) throws SQLException{
        String sql="UPDATE patient SET name=?,phone=?,email=?,address=?,doctor_name=? WHERE id=?";
        try (PreparedStatement pstmt=conn.prepareStatement(sql)){
            pstmt.setString(1,Name);
            pstmt.setString(2,phone);
            pstmt.setString(3,email);
            pstmt.setString(4,adressString);
            pstmt.setString(5,drname);
            pstmt.setInt(6, ID);
            pstmt.executeUpdate();
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patient updated successfully!");
            } else {
                System.out.println("No patient found with ID: " + ID);
            }
        }

    }
    public static void searchPatient(Connection conn,int ID) throws SQLException{
        String sql = "SELECT * FROM patient WHERE id = ?";
        try (PreparedStatement pstmt=conn.prepareStatement(sql)){
            pstmt.setInt(1, ID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    //"phone TEXT, "+ "email TEXT, " + "address TEXT, " +  "doctor_name TEXT" +
                    System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name")+", Phone: "+rs.getString("phone")+", Email: "+rs.getString("email")+", Address:"+rs.getString("address")+", Doctor:"+rs.getString("doctor_name"));
                } else {
                    System.out.println("No record found for ID " + ID);
                }
            }
        }
    }
}
