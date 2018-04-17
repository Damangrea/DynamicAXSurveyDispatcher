/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import connector.MSSQLConnector;
import connector.SQLConnector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Damang
 */
public class testing {
    
    public static void main(String[] args) {
        String sServerPath  = "";
        String sUsername    = "";
        String sPassword    = "";
        String schemaMS     = "";
            sServerPath = "erpdb-tbs-1.packet-systems.com";
            sUsername   = "sa";
            sPassword   = "Jakarta##";
            //schemaMS    = "AXPSIQA";
            schemaMS    = "PSIMicrosoftDynamicsAX";
        MSSQLConnector mssqlConnector=new MSSQLConnector("jdbc:sqlserver://"+sServerPath, sUsername,
               sPassword);
        
        Connection mssqlConnection=mssqlConnector.getConnection();
        try {
            Statement msStmt= mssqlConnection.createStatement();
            //get data case id,case title dll
//            String query="select TIDCASEID ,TIDCASECLOSE "
////                    + " top 5 "
////                    + " now() "
////                    + " current_timestamp "
//                    + " from "+schemaMS+".dbo.TIDCASETABLE ct"
//                    + " where TIDCASESTATUS=12 "
//                    ;
//              ResultSet rs = msStmt.executeQuery(query);
            String sQuery="update "+schemaMS+".dbo.TIDCASETABLE set TIDCASECLOSE=CURRENT_TIMESTAMP where TIDCASECLOSE = '1900-01-01 00:00:00.0' and TIDCASESTATUS=12 ";
            int iRes=msStmt.executeUpdate(sQuery);
            System.out.println(sQuery);
            System.out.println(iRes);
            //ResultSet rs= msStmt.executeQuery(query);
            
//            while (rs.next()) {
//                System.out.println(rs.getString(1)+"  "+rs.getString(2));
//            }
            mssqlConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
