/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.List;
import resources.Toast;
import sms.Device;
import sms.SMSClient;

/**
 *
 * @author Owner
 */
public class taskDevice implements Runnable{
    private String[] device;
    private List[] file;
    private String fileName;
    private int time;
    private Toast toast = new Toast();
    
    public taskDevice(){
        device = null;
        file = null;
        fileName = null;
        time = 0;
    }
    
    public void setFile(List[] file,String filename){
        this.file = file;
        this.fileName = filename;
    }
    
    public void setDevice(String[] device,int time){
        this.device = device;
        this.time = time;
    }
    
    public String getListData(){
        return device[0]+" ["+(time/1000)+"s] - "+fileName+" (#"+file.length+")";
    }

    @Override
    public void run() {
        try {
            mainFrame.LOG("Inicializando " + device[0],0);
            boolean showData = new Device(device[2]).isModem();
            UpdateDetails deviceMD = new UpdateDetails(device[2]);
            if(showData){                
                SMSClient smsDevice = new SMSClient(1,device[2],device[1]);
                mainFrame.textLed(device[0]+" ["+device[2]+"]");
                Thread.sleep(1000);
                for(int i=0;i<file.length;i++){
                    if(i!=0){
                        mainFrame.showMetadata((String)file[i].get(0),device[0]);
                        int clock = time/1000;
                        mainFrame.LOG("TAREA #"+(i)+" >> "+(String)file[i].get(0),0);
                        while(clock>-1){
                            mainFrame.setClockData(clock);
                            Thread.sleep(1000);
                            clock--;                        
                        }
                        smsDevice.sendMessage((String)file[i].get(0), (String)file[i].get(1));
                        main.mainFrame.textLed("Enviando Mensaje ... ");
                        Thread.sleep(1000); 
                        mainFrame.setProgressBar(i+1, file.length);
                    }
                }
            }else{
                System.out.println("DEVICE >> REVIEW YOUR DEVICE FOR CONTINUE");
                mainFrame.textLed("Error en el dispositivo " + device[0] + " " + device[2]);
                Thread.sleep(1000);
                mainFrame.LOG("Error en el dispositivo " + device[0] + " " + device[2], 2);
                toast.showItem("Error al Inicializar", "Error en el dispositivo " + device[0] + " " + device[2], "error");
            }
            mainFrame.clearDeviceBar();
        } catch (Exception ex) {
            System.out.println("ERROR TASK >> "+ex.getMessage());
            mainFrame.LOG("Error en el dispositivo " + device[0] + " " + device[2], 2);
            toast.showItem("Error al Inicializar", "Error en el dispositivo " + device[0] + " " + device[2], "error");
        }
    }
    
    
}
