package DataImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Laura on 2017/10/28.
 * 数据库连接配置
 */
public class DBConnector {
    //ip localhost:3306 database `accommodation_system` user root password 123456  charcterEncoding=utf8
    String mysqlIP="localhost:3306";
    String database="accommodation_system";
    String user_name="root";
    String password="123456";

    public Connection getMySqlConnection() {
        Connection connection=null;
        String url="jdbc:mysql://"+mysqlIP+"/"+database+"?user="+user_name
                +"&password="+password+"&characterEncoding=utf8";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection= DriverManager.getConnection(url);
            System.out.println("Connect to database successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("Connect to database failed!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connect to database failed!");
            e.printStackTrace();
        }
        return connection;
    }

}
