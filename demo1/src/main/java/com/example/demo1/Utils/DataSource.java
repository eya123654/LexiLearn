package com.example.demo1.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

    private String url="jdbc:mysql://localhost:3306/pidev1";
    private String login="root";
    private String pwd="";
    private static DataSource data;

    private Connection con;
    private DataSource(){

        try {
            con= DriverManager.getConnection(url,login,pwd);
            System.out.println("connexion établie");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public Connection getCon(){
        return con;
    }


    public static DataSource getInstance()
    {
        if (data==null)
            data=new DataSource();
        return data;
    }


}