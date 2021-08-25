/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CityOperations;

/**
 *
 * @author vd180005d
 */
public class vd180005_CityOperations implements CityOperations {

    @Override
    public int insertCity(String name, String postalCode) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "insert into City (name, postalCode) values (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, postalCode);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int deleteCity(String... names) {
        StringBuilder sb = new StringBuilder();
        for (String name : names)
            sb.append(",?");
        String query = "delete from City where name in (" + sb.deleteCharAt(0).toString() + ")";
        
        Connection conn = vd180005_DB.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 1; i <= names.length; i++) {
                stmt.setString(i, names[i - 1]);
            }
            return stmt.executeUpdate();
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public boolean deleteCity(int id) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "delete from City where id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<Integer> getAllCities() {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select id from City";
        List<Integer> ret = new LinkedList<>();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            if (rs != null) {
                while (rs.next()) {
                    ret.add(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }
}
