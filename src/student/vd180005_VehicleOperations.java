/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.VehicleOperations;

/**
 *
 * @author boki0
 */
public class vd180005_VehicleOperations implements VehicleOperations {

    @Override
    public boolean insertVehicle(String licensePlateNumber, int fuelType, BigDecimal fuelConsumtion) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "insert into Vehicle (licensePlate, fuelType, consumption) values (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, licensePlateNumber);
            stmt.setInt(2, fuelType);
            stmt.setBigDecimal(3, fuelConsumtion);
            
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public int deleteVehicles(String... licensePlateNumbers) {
                StringBuilder sb = new StringBuilder();
        for (String licensePlateNumber : licensePlateNumbers)
            sb.append(",?");
        String query = "delete from Vehicle where licensePlate in (" + sb.deleteCharAt(0).toString() + ")";
        
        Connection conn = vd180005_DB.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 1; i <= licensePlateNumbers.length; i++) {
                stmt.setString(i, licensePlateNumbers[i - 1]);
            }
            return stmt.executeUpdate();
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public List<String> getAllVehichles() {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select licensePlate from Vehicle";
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
    public boolean changeFuelType(String licensePlateNumber, int fuelType) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "update Vehicle set fuelType=? where licensePlate=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(2, licensePlateNumber);
            stmt.setInt(1, fuelType);
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeConsumption(String licensePlateNumber, BigDecimal fuelConsumption) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "update Vehicle set consumption=? where licensePlate=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(2, licensePlateNumber);
            stmt.setBigDecimal(1, fuelConsumption);
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
