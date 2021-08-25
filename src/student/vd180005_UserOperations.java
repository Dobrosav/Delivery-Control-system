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
import rs.etf.sab.operations.UserOperations;

/**
 *
 * @author boki0
 */
public class vd180005_UserOperations implements UserOperations {
    private boolean validName(String name) {
        return name != null && !name.isBlank() && Character.isUpperCase(name.charAt(0));
    }
    private boolean validPassword(String password) {
        return password != null && password.length() >= 8 
                && password.matches(".*\\d.*") && password.matches(".*[a-zA-Z].*");
                
    }
        
    @Override
    public boolean insertUser(String username, String firstName, String lastName, String password) {
        if (!validName(firstName) || !validName(lastName) || !validPassword(password))
            return false;
        
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "insert into [dbo].[User] (username, name, surname, password) values (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, password);
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public int declareAdmin(String username) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select username from Admin where username=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return 1;
            } catch (SQLException ex) {
//                Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
//            Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "select username from [dbo].[User] where username=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next())
                    return 2;
            } catch (SQLException ex) {
//                Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
//            Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "insert into Admin (username) values (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            if (stmt.executeUpdate() == 1)
                return 0;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 3;
    }

    @Override
    public Integer getSentPackages(String... usernames) {
        Integer ret = null;
        StringBuilder sb = new StringBuilder();
        for (String username : usernames)
            sb.append(",?");
        
        String query = "select coalesce(sum(sentPackagesCnt), -1) from [dbo].[User] where username in (" + sb.deleteCharAt(0).toString() + ")";
        Connection conn = vd180005_DB.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 1; i <= usernames.length; i++) {
                stmt.setString(i, usernames[i - 1]);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int r = rs.getInt(1);
                    ret = r == -1 ? null : Integer.valueOf(r);
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
    public int deleteUsers(String... names) {
        StringBuilder sb = new StringBuilder();
        for (String name : names)
            sb.append(",?");
        String query = "delete from [dbo].[User] where username in (" + sb.deleteCharAt(0).toString() + ")";
        
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
    public List<String> getAllUsers() {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select username from [dbo].[User]";
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
    
    
}
