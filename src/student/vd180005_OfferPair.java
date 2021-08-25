/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import rs.etf.sab.operations.PackageOperations.Pair;

/**
 *
 * @author vd180005d
 */
public class vd180005_OfferPair implements Pair {
    private int i;
    private BigDecimal s;
    
    public vd180005_OfferPair(int i, BigDecimal s) {
        this.i = i;
        this.s = s;
    }
    
    @Override
    public Object getFirstParam() {
        return  i;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getSecondParam() {
        return s; //To change body of generated methods, choose Tools | Templates.
    }
    
}
