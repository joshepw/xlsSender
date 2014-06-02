/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


/**
 *
 * @author Owner
 */
public class Base64Coder {
    
    public String Encode(String text){
        return Base64.encode(text.getBytes());
    }
    
    public String Decode(String text){
        return new String(Base64.decode(text));
    }
}
