/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scan;

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
public class Scanning {
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
            //schemaMS    = "AXPSIQA";
            schemaMS    = "MsDynamicsAXStaging";
            }
        MSSQLConnector mssqlConnector=new MSSQLConnector("jdbc:sqlserver://"+sServerPath, sUsername,
               sPassword);
        SQLConnector sqlConnector=new SQLConnector();
        
        Calendar cal=Calendar.getInstance();
        
        Connection mssqlConnection=mssqlConnector.getConnection();
        Connection sqlConnection=sqlConnector.getConnection();
        try {
            Statement msStmt= mssqlConnection.createStatement();
            Statement msStmtUpd= mssqlConnection.createStatement();
            Statement myStmt= sqlConnection.createStatement();
            //get data case id,case title dll
            String query="select "
//                    + " top 5 "
                    + " ct.TIDCASEID ,ct.TIDCASETITLE "
//                    + ", ct.TIDCASESTATUS "
//                    + ", emp.PERSON , eng.PERSON "
                    + ",concat(empper.FIRSTNAME,' ',empper.MIDDLENAME,' ',empper.LASTNAME)"
                    + ",concat(engper.FIRSTNAME,' ',engper.MIDDLENAME,' ',engper.LASTNAME) "
                    + " from "+schemaMS+".dbo.TIDCASETABLE ct"
                    + ","+schemaMS+".dbo.HCMWORKER emp"
                    + ","+schemaMS+".dbo.HCMWORKER eng"
                    + ","+schemaMS+".dbo.DIRPERSONNAME empper"
                    + ","+schemaMS+".dbo.DIRPERSONNAME engper"
                    + "  where TIDCASESTATUS=10 "
                    + " and ct.TIDEMPLOYEE=emp.RECID "
                    + " and emp.PERSON=empper.PERSON "
                    + " and ct.TIDPRIMARYENGINEER=eng.RECID "
                    + " and eng.PERSON=engper.PERSON "
                    + " and len(ct.TIDCASEID)> 10"
                    + " and len(ct.TIDSURVEYURL)=0"
                    ;
            System.out.println(query);
            ResultSet rs= msStmt.executeQuery(query);
            long l;
            String url,survey_id;
            int iSuccess=0,iFailed=0,iTotal=0;
            
            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String sDate=sdf.format(cal.getTime());
            
            while (rs.next()) {
                iTotal++;
                l=System.currentTimeMillis();
                survey_id=Long.toHexString(l)+"_"+rs.getString(1).replaceAll("/", "_");
                //url="https://ssportal-tbs-2.packet-systems.com/survey/survey_index/"+survey_id;
                url="https://survey.packet-systems.com/survey/survey_index/"+survey_id;
                    //loop insert mysql for mapping
                query="insert into survey_mapping(survey_id,case_id,case_title,employee,engineer_name) "
                        + " values('"+survey_id+"','"+rs.getString(1)+"','"+rs.getString(2)+"','"+rs.getString(3)+"','"+rs.getString(4)+"')";
//                System.out.println(query);
                if(myStmt.executeUpdate(query)>0){
                    sDate=sdf.format(cal.getTime());
//                    //loop insert mssql for link
                    query="update "+schemaMS+".dbo.TIDCASETABLE set TIDSURVEYURL='"+url+"' where TIDCASEID='"+rs.getString(1)+"'";
                    System.out.println(sDate+" "+query);
                    if(msStmtUpd.executeUpdate(query)>0){
                        //success
                        iSuccess++;
                    }else{
                        //error
                        iFailed++;
                    }
                }else{
                    //error
                    iFailed++;
                }
                
            }
            
            System.out.println(sDate+" Success:"+iSuccess);
            System.out.println(sDate+" Failed:"+iFailed);
            System.out.println(sDate+" Total:"+iTotal);
            
            mssqlConnection.close();
            sqlConnection.close();
            
            
            
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
