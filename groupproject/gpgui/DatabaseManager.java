package com.groupproject.gpgui;

import java.sql.*;


public class DatabaseManager{
    private static final String URL = "jdbc:sqlite:doctorsoffice.db";
    private static Connection connection;

    //open connection
    public static Connection getConnection() throws SQLException{
        if (connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(URL);
        }
        return connection;
    }

    public static void closeConnection(){

        try{
            if(connection != null && !connection.isClosed()) connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
