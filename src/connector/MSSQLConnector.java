/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Damangrea
 */
public class MSSQLConnector {
    Connection conn;
    
    public  MSSQLConnector(String db_connect_string,
            String db_userid,
            String db_password)
   {
      try {
         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
         conn = DriverManager.getConnection(db_connect_string,
                  db_userid, db_password);
//         System.out.println("connected");
//         Statement statement = conn.createStatement();
//         String queryString = "select * from AXDEV.dbo.ACCOUNTINGEVENT ";
//         ResultSet rs = statement.executeQuery(queryString);
//         while (rs.next()) {
//            System.out.print(rs.getString(1)+",");
//            System.out.print(rs.getString(2)+",");
//            System.out.print(rs.getString(3)+",");
//            System.out.println(rs.getString(4));
//         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

    public MSSQLConnector() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
     
    public Connection getConnection(){
        return conn;
    }
    
    
    
}
