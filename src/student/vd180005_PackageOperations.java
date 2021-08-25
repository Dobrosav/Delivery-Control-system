/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.PackageOperations;

/**
 *
 * @author boki0
 */
public class vd180005_PackageOperations implements PackageOperations {

    @Override
    public int insertPackage(int districtFrom, int districtTo, String username,
            int packageType, BigDecimal weight) {
        if (packageType == vd180005_Consts.PKG_TYPE_LETTER) {
            weight = new BigDecimal(0);
        }

        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "insert into Package (districtFrom, districtTo, senderUsername, type, weight) values (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, districtFrom);
            stmt.setInt(2, districtTo);
            stmt.setString(3, username);
            stmt.setInt(4, packageType);
            stmt.setBigDecimal(5, weight);
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
    public int insertTransportOffer(String courierUsername, int packageId, BigDecimal pricePercentage) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select * from Courier where courierUsername = ? and status = " + vd180005_Consts.COURIER_NOT_DRIVING;
        try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, courierUsername);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return -1;
                }
            } catch (SQLException ex) {
//              Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }

        query = "select * from Package where id = ? and status = " + vd180005_Consts.PKG_CREATED;
        try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, packageId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return -1;
                }
            } catch (SQLException ex) {
//              Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }

        query = "insert into Offer (courierUsername, packageId, pricePercentage) values (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, courierUsername);
            stmt.setInt(2, packageId);
            stmt.setBigDecimal(3, pricePercentage);
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
    public boolean acceptAnOffer(int offerId) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "update Offer set accepted = 1 where id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, offerId);
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<Integer> getAllOffers() {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select id from Offer";
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

    @Override
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int packageId) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select id, pricePercentage from Offer where packageId = ?";
        List<Pair<Integer, BigDecimal>> ret = new LinkedList<Pair<Integer, BigDecimal>>();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, packageId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs != null) {
                    while (rs.next()) {
                        ret.add(new vd180005_OfferPair(rs.getInt(1), rs.getBigDecimal(2)));
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
    public boolean deletePackage(int packageId) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "delete from Package where id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, packageId);
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeWeight(int packageId, BigDecimal newWeight) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "update Package set weight = ? where id = ? and status = " + vd180005_Consts.PKG_CREATED;
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBigDecimal(1, newWeight);
            stmt.setInt(2, packageId);
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeType(int packageId, int newType) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "update Package set type = ? where id = ? and status = " + vd180005_Consts.PKG_CREATED;
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, newType);
            stmt.setInt(2, packageId);
            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public Integer getDeliveryStatus(int packageId) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select status from Package where id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, packageId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs != null) {
                    return rs.next() ? rs.getInt(1) : null;
                }
            } catch (SQLException ex) {
//                Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
//            Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public BigDecimal getPriceOfDelivery(int packageId) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select deliveryPrice from Package where id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, packageId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs != null) {
                    return rs.next() ? rs.getBigDecimal(1) : null;
                }
            } catch (SQLException ex) {
//                Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
//            Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Date getAcceptanceTime(int packageId) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select acceptanceTime from Package where id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, packageId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs != null) {
                    if (rs.next()) {
                        java.util.Date d = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).parse(rs.getString(1));
                        return new Date(d.getTime());
                    }
                }
            } catch (Exception ex) {
//                Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
//            Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int type) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select id from Package where type = ?";
        List<Integer> ret = new LinkedList<>();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs != null) {
                    while (rs.next()) {
                        ret.add(rs.getInt(1));
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
    public List<Integer> getAllPackages() {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select id from Package";
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

    @Override
    public List<Integer> getDrive(String courierUsername) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        String query = "select id from Package where courierUsername = ? and status = " + vd180005_Consts.PKG_TAKEN;
        List<Integer> ret = new LinkedList<>();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, courierUsername);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs != null) {
                    while (rs.next()) {
                        ret.add(rs.getInt(1));
                    }
                }
            } catch (SQLException ex) {
//                Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
//            Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret.isEmpty() ? null : ret;
    }

    @Override
    public int driveNextPackage(String courierUsername) {
        Connection conn = vd180005_DB.getInstance().getConnection();
        int courierStatus = 0;
        String query = "select status from Courier where courierUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, courierUsername);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    courierStatus = rs.getInt(1);
                } else {
                    return -2;
                }
            } catch (SQLException ex) {
//            Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
//            Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (courierStatus == 0) {
            // NOT DRIVING
            // Update packages status to 2 (TOKEN)
            query = "update Package set status = 2 where status = 1 and courierUsername = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, courierUsername);
                if (stmt.executeUpdate() == 0) {
                    return -1;
                }
            } catch (SQLException ex) {
//            Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // Get next pkgs
        int pkg[] = {-1, -1};
        int districtFrom[] = {-1, -1};
        int districtTo[] = {-1, -1};
        query = "select top 2 id, districtFrom, districtTo from Package where courierUsername = ? and status = 2 order by acceptanceTime";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, courierUsername);
            try (ResultSet rs = stmt.executeQuery()) {
                int i = 0;
                while (rs.next()) {
                    pkg[i] = rs.getInt(1);
                    districtFrom[i] = rs.getInt(2);
                    districtTo[i] = rs.getInt(3);
                    i++;
                }
            } catch (SQLException ex) {
//            Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
//            Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
        }

//        System.out.println("pkgs: " + pkg[0] + ", " + pkg[1]);
//        System.out.println("districtFrom: " + districtFrom[0] + ", " + districtFrom[1]);
//        System.out.println("districtTo: " + districtTo[0] + ", " + districtTo[1]);

        // Update nextPkg.status = 3
        query = "update Package set status = 3 where id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, pkg[0]);
            if (stmt.executeUpdate() == 0) {
                return -2;
            }
        } catch (SQLException ex) {
//            Logger.getLogger(District.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Gas bills for courier
        query = "{ call SPGasBills (?, ?, ?) }";
        try (CallableStatement cs = conn.prepareCall(query)) {
            cs.setString(1, courierUsername);
            cs.setInt(2, districtFrom[0]);
            cs.setInt(3, districtTo[0]);
            cs.execute();
        } catch (SQLException ex) {
            Logger.getLogger(vd180005_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("EXCEPTION1");
        }

        if (districtFrom[1] != -1) {
            // Bills from current delivery to next pick up location
            query = "{ call SPGasBills (?, ?, ?) }";
            try (CallableStatement cs = conn.prepareCall(query)) {
                cs.setString(1, courierUsername);
                cs.setInt(2, districtTo[0]);
                cs.setInt(3, districtFrom[1]);
                cs.execute();
            } catch (SQLException ex) {
            Logger.getLogger(vd180005_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("EXCEPTION2");
            }
        }

        return pkg[0];
    }

}
