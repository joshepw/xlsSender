/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import sms.SerialConnection;
import sms.SerialConnectionException;
import sms.SerialParameters;

/**
 *
 * @author Owner
 */
public class UpdateDetails implements Runnable{
    SerialConnection comm =null;
    private SerialParameters data;
    String imei = "  -  -  -  -  -  ";
    String model = "  -  -  -  -  -  ";
    
    public UpdateDetails(String port){
        data = new SerialParameters (port,9600,0,0,8,1,0);
        run();
    }

    @Override
    public void run() {
        try{
            comm = new SerialConnection(data);
            String result = "";
            

                comm.openConnection();
                
                comm.send("ATZ");
                Thread.sleep(100);
                result = comm.getIncommingString().replace("\n", " ").trim();
                System.out.println(result);
                
                comm.send("AT+CGMI");
                Thread.sleep(100);
                result = comm.getIncommingString().replace("\n", " ").trim();
                result = result.substring(result.indexOf(" "), result.lastIndexOf(" ")).trim();
                showModems.company.setText(result);
                model = result;
                System.out.println(result);
                
                comm.send("AT+CGSN");
                Thread.sleep(100);
                result = comm.getIncommingString().replace("\n", " ").trim();
                result = result.substring(result.indexOf(" "), result.lastIndexOf(" ")).trim();
                showModems.imei.setText(result);
                imei = result;
                System.out.println(result);
                
                comm.send("AT+CSQ");
                Thread.sleep(100);
                result = comm.getIncommingString().replace("\n", " ").trim();
                System.out.println(result);
                double nivel = 0;
                try{
                    result = result.substring(result.indexOf(":")+2, result.lastIndexOf(",")).trim();
                    if(Integer.parseInt(result)==99){ nivel = 0; }else{
                        nivel = (Double.parseDouble(result)/(double)31)*100;
                    }
                    System.out.println(nivel+"%");
                }catch(StringIndexOutOfBoundsException ex){ nivel = 0; }
                
                showModems.antena.setValue((int)nivel);
                main.mainFrame.setDeviceBar((int)nivel, imei , model);
                
                comm.closeConnection();        
            
        }catch(SerialConnectionException | InterruptedException ex){
            System.out.println("ERROR >> "+ex.getMessage());
            comm.closeConnection();
        }
    }
}
