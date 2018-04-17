/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package update;

import connector.MSSQLConnector;
import connector.SQLConnector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Damangrea
 */
public class Updating {
    public static void main(String[] args) {
        String sServerPath  = "";
        String sUsername    = "";
        String sPassword    = "";
        String schemaMS     = "";
        if(args.length >=4){
            sServerPath = args[0];
            sUsername   = args[1];
            sPassword   = args[2];
            schemaMS    = args[3];
            }
        else{
            sServerPath = "erpdbdev-tbs-2.packet-systems.com";
            sUsername   = "sa";
            sPassword   = "Jakarta##";
           // schemaMS    = "AXPSIQA";
            schemaMS    = "MsDynamicsAXStaging";
            }
        MSSQLConnector mssqlConnector=new MSSQLConnector("jdbc:sqlserver://"+sServerPath, sUsername,
               sPassword);
        SQLConnector sqlConnector=new SQLConnector();
        
        Calendar cal=Calendar.getInstance();
        
        Connection mssqlConnection=mssqlConnector.getConnection();
        Connection sqlConnection=sqlConnector.getConnection();
        
        ResultSet rs;
        try {
            Statement sqlStmt=sqlConnection.createStatement();
            Statement sqlStmt2=sqlConnection.createStatement();
            Statement mssqlStmt=mssqlConnection.createStatement();
            //get data from mysql
            String sQuery="select case_id,overall,ease_access,timeliness,communication,effectiveness,technical,attitude,customer_comments from survey";
            rs=sqlStmt.executeQuery(sQuery);
            int iRes,iSuccessHeader=0,iSuccessDetail=0,iSuccess=0,iFailed=0,iTotal=0;
            while (rs.next()) {                
                iTotal++;
                    //loop insert mssql for survey result header
                    //loop insert mssql for survey result detail
                sQuery="update "+schemaMS+".dbo.TIDCASESURVEYREPLY set TIDSCORE='"+rs.getString(2)+"' where TIDCASEID='"+rs.getString(1)+"' and SEQUENCE=1 ";
                iRes=mssqlStmt.executeUpdate(sQuery);
                switch(iRes){case 1 : iSuccessDetail++;break;case 0 :iFailed++;break;default:iFailed++;break;}
                sQuery="update "+schemaMS+".dbo.TIDCASESURVEYREPLY set TIDSCORE='"+rs.getString(3)+"' where TIDCASEID='"+rs.getString(1)+"' and SEQUENCE=2 ";
                iRes=mssqlStmt.executeUpdate(sQuery);
                switch(iRes){case 1 : iSuccessDetail++;break;case 0 :iFailed++;break;default:iFailed++;break;}
                sQuery="update "+schemaMS+".dbo.TIDCASESURVEYREPLY set TIDSCORE='"+rs.getString(4)+"' where TIDCASEID='"+rs.getString(1)+"' and SEQUENCE=3 ";
                iRes=mssqlStmt.executeUpdate(sQuery);
                switch(iRes){case 1 : iSuccessDetail++;break;case 0 :iFailed++;break;default:iFailed++;break;}
                sQuery="update "+schemaMS+".dbo.TIDCASESURVEYREPLY set TIDSCORE='"+rs.getString(5)+"' where TIDCASEID='"+rs.getString(1)+"' and SEQUENCE=4 ";
                iRes=mssqlStmt.executeUpdate(sQuery);
                switch(iRes){case 1 : iSuccessDetail++;break;case 0 :iFailed++;break;default:iFailed++;break;}
                sQuery="update "+schemaMS+".dbo.TIDCASESURVEYREPLY set TIDSCORE='"+rs.getString(6)+"' where TIDCASEID='"+rs.getString(1)+"' and SEQUENCE=5 ";
                iRes=mssqlStmt.executeUpdate(sQuery);
                switch(iRes){case 1 : iSuccessDetail++;break;case 0 :iFailed++;break;default:iFailed++;break;}
                sQuery="update "+schemaMS+".dbo.TIDCASESURVEYREPLY set TIDSCORE='"+rs.getString(7)+"' where TIDCASEID='"+rs.getString(1)+"' and SEQUENCE=6 ";
                iRes=mssqlStmt.executeUpdate(sQuery);
                switch(iRes){case 1 : iSuccessDetail++;break;case 0 :iFailed++;break;default:iFailed++;break;}
                sQuery="update "+schemaMS+".dbo.TIDCASESURVEYREPLY set TIDSCORE='"+rs.getString(8)+"' where TIDCASEID='"+rs.getString(1)+"' and SEQUENCE=7 ";
                iRes=mssqlStmt.executeUpdate(sQuery);
                switch(iRes){case 1 : iSuccessDetail++;break;case 0 :iFailed++;break;default:iFailed++;break;}
                
                    //update mssql for survey case from surveysend to closed
                sQuery="update "+schemaMS+".dbo.TIDCASETABLE set TIDCASESTATUS=12,TIDCASECLOSE=CURRENT_TIMESTAMP,PSICUSTOMERCOMMENTS='"+rs.getString(9)+"' where TIDCASEID='"+rs.getString(1)+"' ";
                iRes=mssqlStmt.executeUpdate(sQuery);
                switch(iRes){case 1 :iSuccess++;break;case 0 :iFailed++;break;default:iFailed++;break;}
                System.out.println(sQuery);
                
                sQuery="delete from survey where case_id='"+rs.getString(1)+"' ";
                iRes=sqlStmt2.executeUpdate(sQuery);
                sQuery="delete from survey_mapping where case_id='"+rs.getString(1)+"' ";
                iRes=sqlStmt2.executeUpdate(sQuery);
            }
            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String sDate=sdf.format(cal.getTime());
            System.out.println(sDate+" Success:"+iSuccess);
            System.out.println(sDate+" Success Header:"+iSuccessHeader);
            System.out.println(sDate+" Success Detail:"+iSuccessDetail);
            System.out.println(sDate+" Failed:"+iFailed);
            System.out.println(sDate+" Total:"+iTotal);
            System.out.println();
            
            mssqlConnection.close();
            sqlConnection.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
