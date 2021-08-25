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
import rs.etf.sab.operations.CourierOperations;

/**
 *
 * @author vd180005d
 */
public class vd180005_CourierOperations implements CourierOperations {

    @Override
    public boolean insertCourier(String courierUsername, String licensePlateNumber) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "insert into Courier (courierUsername, licensePlate) values (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, courierUsername);
            stmt.setString(2, licensePlateNumber);
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean deleteCourier(String courierUsername) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "delete from Courier where courierUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, courierUsername);
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<String> getCouriersWithStatus(int status) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select courierUsername from Courier where status = ?";
        List<String> ret = new LinkedList<>();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs != null) {
                    while (rs.next()) {
                        ret.add(rs.getString(1));
                    }
                }
            } catch (SQLException ex) {
//                Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
//            Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public List<String> getAllCouriers() {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select courierUsername from Courier order by profit desc";
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
    public BigDecimal getAverageCourierProfit(int deliveriesCnt) {
        String query = "select coalesce(avg(profit), 0) as 'AVG(profit)' from Courier where deliveriesCnt >= ?";
        Connection conn = vd180005_DB.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, deliveriesCnt);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getBigDecimal(1);
            } catch (SQLException ex) {
//                Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
//            Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(0);
    }
    
}
