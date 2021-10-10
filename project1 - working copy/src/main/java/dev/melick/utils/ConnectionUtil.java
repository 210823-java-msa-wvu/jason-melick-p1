package dev.melick.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {

    private static ConnectionUtil cu = null;
    private static Properties props;

    // private constructor so we can control the creation of any instances (Creational Design Pattern - Singleton)
    private ConnectionUtil() {
        props = new Properties();

        InputStream dbProps = ConnectionUtil.class.getClassLoader().getResourceAsStream("connection.properties");
        try {
            props.load(dbProps);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // a getter method to return an instance of this ConnectionUtil Class
    public static synchronized ConnectionUtil getConnectionUtil() {
        // Does one exist?
        if (cu == null) {
            // if it is null -> create one
            cu = new ConnectionUtil();
        }
        // if it's not null -> return the one that already exists
        return cu;
    }

    // a method to get the connection itself
    public Connection getConnection() {

        Connection conn = null;

        // Hot fix to basically force the Driver class to load
        try {
            Class.forName(props.getProperty("driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Get the credentials needed to access the database from our connection.properties file
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

/*   public static void main(String[] args) {

       Connection connection = getConnectionUtil().getConnection();

     if (connection != null) {
           System.out.println("Connection successful");
       } else {
         System.out.println("Something went wrong...");
     }
    }*/

}