/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CourierRequestOperation;

/**
 *
 * @author vd180005d
 */
public class vd180005_CourierRequestOperations implements CourierRequestOperation {

    @Override
    public boolean insertCourierRequest(String username, String licensePlateNumber) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select * from Courier where licensePlate=?";
        try (PreparedStatement stmt=conn.prepareStatement(query)){
            stmt.setString(1, licensePlateNumber);
            try (ResultSet rs=stmt.executeQuery()){
                if(rs.next()){
                    return false;
                }
            } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "insert into CourierRequest (username, licensePlate) values (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, licensePlateNumber);
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean deleteCourierRequest(String username) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "delete from CourierRequest where username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeVehicleInCourierRequest(String username, String licencePlateNumber) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "update CourierRequest set licensePlate = ? where username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, licencePlateNumber);
            stmt.setString(2, username);
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<String> getAllCourierRequests() {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select username from CourierRequest";
        List<String> ret = new LinkedList<>();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            if (rs != null) {
                while (rs.next()) {
                    ret.add(rs.getString(1));
                }
            }
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public boolean grantRequest(String username) {
        Connection conn=vd180005_DB.getInstance().getConnection();
        String query="{ call SPGrantCourierRequest (?, ?) }"; 
        try (CallableStatement cs= conn.prepareCall(query)){
            cs.setString(1, username);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);
            cs.execute();
            return cs.getInt(2) == 1;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
