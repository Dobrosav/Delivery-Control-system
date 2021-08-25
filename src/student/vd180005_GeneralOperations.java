/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.*;
import student.*;

/**
 *
 * @author vd180005d
 */
public class vd180005_GeneralOperations implements GeneralOperations {

    public static void populateDB(CityOperations cityOperations,
            CourierOperations courierOperations,
            CourierRequestOperation courierRequestOperation,
            DistrictOperations districtOperations,
            GeneralOperations generalOperations,
            UserOperations userOperations,
            VehicleOperations vehicleOperations,
            PackageOperations packageOperations
    ) {
        int init;
        int id = cityOperations.insertCity("Beograd", "11000");
        init = districtOperations.insertDistrict("Palilula", id, 100, 200);
        districtOperations.insertDistrict("Novi Beograd", id, 120, 200);
        id = cityOperations.insertCity("Nis", "18000");
        districtOperations.insertDistrict("Medijana", id, 50, 50);
        id = cityOperations.insertCity("Leskovac", "16000");
        districtOperations.insertDistrict("Dubocica", id, 50, 10);
        cityOperations.insertCity("Subotica", "24000");
        userOperations.insertUser("bbadnjarevic", "Bogdan", "Badnjarevic", "password1");
        userOperations.insertUser("kurir", "FirstName", "LastName", "password1");
        userOperations.insertUser("user1", "FirstName", "LastName", "password1");
        userOperations.insertUser("user2", "FirstName", "LastName", "password1");
        userOperations.insertUser("admin", "FirstName", "LastName", "password1");
        vehicleOperations.insertVehicle("REG0", vd180005_Consts.FUEL_DIESEL, new BigDecimal(8.1));
        vehicleOperations.insertVehicle("REG1", vd180005_Consts.FUEL_DIESEL, new BigDecimal(7.6));
        vehicleOperations.insertVehicle("REG2", vd180005_Consts.FUEL_PETROL, new BigDecimal(6.0));
        vehicleOperations.insertVehicle("REG3", vd180005_Consts.FUEL_LPG, new BigDecimal(4.9));
        userOperations.declareAdmin("admin");
        userOperations.declareAdmin("bbadnjarevic");
        courierOperations.insertCourier("kurir", "REG0");
        courierOperations.insertCourier("user2", "REG2");

        int init2;
        int res;
        init--;
        init2 = packageOperations.insertPackage(init + 1, init + 2, "user1", vd180005_Consts.PKG_TYPE_LETTER, null);
        System.out.println(init2);
        init2--;
        res = packageOperations.insertPackage(init + 1, init + 3, "bbadnjarevic", vd180005_Consts.PKG_TYPE_LETTER, new BigDecimal(2.1));
        System.out.println(res);
        res = packageOperations.insertPackage(init + 4, init + 2, "user1", vd180005_Consts.PKG_TYPE_FRAGILE, new BigDecimal(2.5));
        System.out.println(res);
        res = packageOperations.insertPackage(init + 4, init + 1, "user2", vd180005_Consts.PKG_TYPE_STANDARD, new BigDecimal(8));
        System.out.println(res);

        init = init2;
        res = packageOperations.insertTransportOffer("user2", init + 2, new BigDecimal(15));
        System.out.println(res);
        res = packageOperations.insertTransportOffer("kurir", init + 4, new BigDecimal(15));
        System.out.println(res);
        res = packageOperations.insertTransportOffer("kurir", init + 1, new BigDecimal(15));
        System.out.println(res);
        res = packageOperations.insertTransportOffer("user2", init + 1, new BigDecimal(10));
        System.out.println(res);
    }

    public static void dropAll() {
        String query = "DROP TABLE [dbo].[Admin]\n"
                + "DROP TABLE [dbo].[Offer]\n"
                + "DROP TABLE [dbo].[CourierRequest]\n"
                + "DROP TABLE [dbo].[Package]\n"
                + "DROP TABLE [dbo].[District]\n"
                + "DROP TABLE [dbo].[City]\n"
                + "DROP TABLE [dbo].[Courier]\n"
                + "DROP TABLE [dbo].[Vehicle]\n"
                + "DROP TABLE [dbo].[User]";

        Connection conn = vd180005_DB.getInstance().getConnection();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eraseAll() {
        String query = "DELETE FROM [dbo].[Admin]\n"
                + "DELETE FROM [dbo].[Offer]\n"
                + "DELETE FROM [dbo].[CourierRequest]\n"
                + "DELETE FROM [dbo].[Package]\n"
                + "DELETE FROM [dbo].[District]\n"
                + "DELETE FROM [dbo].[City]\n"
                + "DELETE FROM [dbo].[Courier]\n"
                + "DELETE FROM [dbo].[Vehicle]\n"
                + "DELETE FROM [dbo].[User]";

        Connection conn = vd180005_DB.getInstance().getConnection();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        } catch (SQLException ex) {
//            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printCourier() {
        Connection conn = vd180005_DB.getInstance().getConnection();
        try (
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from Courier")) {

            System.out.println();
            while (rs.next()) {
                System.out.println("username: " + rs.getString(1)
                        + "\tcnt: " + rs.getInt(2) + "\tprofit: " + rs.getBigDecimal(3)
                        + "\tstatus: " + rs.getInt(4) + "\tREGBr: " + rs.getString(5));
            }
        } catch (SQLException ex) {
            Logger.getLogger(vd180005_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
