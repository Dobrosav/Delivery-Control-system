package student;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author vd180005d
 */
public class vd180005_DB {
private Connection connection;  
    
    private vd180005_DB(){
        try {
            connection=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SAB;integratedSecurity=true;");
        } catch (SQLException ex) {
            connection = null;
            Logger.getLogger(vd180005_DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
     
    private static vd180005_DB db=null;
    
    public static vd180005_DB getInstance()
    {
        if(db == null)
            db = new vd180005_DB();
        return db;
    } 
}
