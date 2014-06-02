/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sms;

/**
 *
 * @author Owner
 */
public class Device {
    private static final long STANDARD=500;
    private static final long LONG=2000;
    private static final long  VERYLONG=20000;
    
    SerialConnection mySerial =null;
    static final private char cntrlZ=(char)26;
    String in, out;
    private long delay=STANDARD;
    
    private SerialParameters defaultParameters;
    public int step;
    public int status=-1;
    private String[] device = new String[3];
    
    public Device(String port){
        defaultParameters= new SerialParameters (port,9600,0,0,8,1,0);
    }
    
    public boolean isModem(){
        try {
            SerialParameters params = defaultParameters;
            mySerial =new SerialConnection (params);
            mySerial.openConnection();
            int status=-1;
            String result = "";
            
            mySerial.send("atz");
            Thread.sleep(100);
            result = mySerial.getIncommingString();
            
            status=result.indexOf("OK");
            if (status>-1){
                mySerial.send("at+cgmi");
                Thread.sleep(100);
                result = mySerial.getIncommingString();
                status=result.indexOf("OK");
                if (status>-1){
                    mySerial.closeConnection();
                    return true;
                }
            }
            
            mySerial.closeConnection();
            return false;
            
        } catch (Exception ex) {
            System.out.println("ERROR >> "+ex.getMessage());
            mySerial.closeConnection();
            return false;
        }
    }    
}
