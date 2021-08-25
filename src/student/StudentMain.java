package student;

import rs.etf.sab.operations.*;
import rs.etf.sab.tests.*;
public class StudentMain {

    public static void main(String[] args) {
        CityOperations cityOperations = new vd180005_CityOperations(); // Change this to your implementation.
        DistrictOperations districtOperations = new vd180005_DistrictOperations(); // Do it for all classes.
        CourierOperations courierOperations = new vd180005_CourierOperations(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new vd180005_CourierRequestOperations();
        GeneralOperations generalOperations = new vd180005_GeneralOperations();
        UserOperations userOperations = new vd180005_UserOperations();
        VehicleOperations vehicleOperations = new vd180005_VehicleOperations();
        PackageOperations packageOperations = new student.vd180005_PackageOperations();
        
        TestHandler.createInstance(cityOperations, courierOperations, courierRequestOperation, districtOperations, generalOperations, userOperations, vehicleOperations, packageOperations);
        TestRunner.runTests();
        

    }
}
