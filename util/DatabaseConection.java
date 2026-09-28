package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConection {
    private static final String url = "jdbc:mysql://localhost:3306/barber_shop_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "QWert1234!@#";
    private DatabaseConection(){}
    public static Connection getConnection()throws SQLException{
        return DriverManager.getConnection(url,user,password);
    }
    
}

