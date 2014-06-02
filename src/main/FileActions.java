/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Owner
 */
public class FileActions {
    private File file;
    private RandomAccessFile devices;
    private RandomAccessFile licence;
    
    FileActions(){
        try {
            devices = new RandomAccessFile("lib/devices.jar","rw");
            licence = new RandomAccessFile("lib/PortDetector.jar","rw");
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
    
    public boolean isExistDLL(){
        file = new File("/WINDOWS/System32/rxtxSerial.dll");
        return file.exists();
    }
    
    public boolean licence(String key){
        String data = new Base64Coder().Decode(key);
        
        if(data.indexOf(";")>-1){
            addCount(Integer.parseInt(data.substring(0,data.indexOf(";"))));
            addLicence(data.substring(data.indexOf(";")+1));
            return true;
        }        
        return false;
    }
    
    public void addLicence(String name){
        try{
            licence.seek(0);
            licence.readInt();
            licence.writeUTF(name);
        }catch(Exception ex){
            System.out.println("ERROR >> "+ex.getMessage());
        }
    }
    
    public String getLicence(){
        try{
            licence.seek(0);
            licence.readInt();
            return licence.readUTF();
        }catch(Exception ex){
            System.out.println("ERROR >> "+ex.getMessage());
            return null;
        }
    }
    
    public boolean addCount(int n){
        try{
            licence.seek(0);
            licence.writeInt(n-1);
            return true;
        }catch(Exception ex){
            System.out.println("ERROR >> "+ex.getMessage());
            return false;
        }
    }
    
    public int getCount(){
        try{
            licence.seek(0);
            return licence.readInt();
        }catch(Exception ex){
            System.out.println("ERROR >> "+ex.getMessage());
            return 0;
        }
    }
    
    public boolean addDevice(String[] data){
        try {
            int index = 1+(int)(indexDevice()); 
            devices.seek(devices.length());
            devices.writeUTF(data[0]);
            devices.writeUTF(data[1]);
            devices.writeUTF(data[2]);
            devices.writeInt(index);
            devices.writeBoolean(true);
            return true;
        } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            return false;
        }
    }
    
    public boolean disableDevice(int index){
        try {
            devices.seek(0);
            while(devices.getFilePointer()<devices.length()){
                devices.readUTF();
                devices.readUTF();
                devices.readUTF();
                if(devices.readInt()==index){
                    devices.writeBoolean(false);
                    return true;
                }
                devices.readBoolean();
            }
            return false;
        } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            return false;
        }
    }
    
    public List getDevices(){
        try {
            List devicesList = new ArrayList<>();
            devices.seek(0);
            String[] data;
            while(devices.getFilePointer()<devices.length()){
                data = new String[4];
                data[0] = devices.readUTF();
                data[1] = devices.readUTF();
                data[2] = devices.readUTF();
                data[3] = "" + devices.readInt();
                if(devices.readBoolean()){
                    devicesList.add(data);
                }
            }
            return devicesList;
        } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            return null;
        }
    }
    
    public int indexDevice(){
        try {
            int n = 0;
            devices.seek(0);
            while(devices.getFilePointer()<devices.length()){
                devices.readUTF();
                devices.readUTF();
                devices.readUTF();
                devices.readInt();
                devices.readBoolean();
                n++;
            }
            return n;
        } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            return -1;
        }
    }
    
    public String[] getComboList(List<String[]> data){
        String[] list = new String[data.size()];
        for(int i=0;i<data.size();i++){
            String[] deviceMetadata = data.get(i);
            list[i] = deviceMetadata[0] + " [" + deviceMetadata[2] + "]";
        }
        return list;
    }
    
    public void printDevices(){
       
        System.out.println("=======================================================");
        List data = getDevices();
        for(int x=0;x<data.size();x++){
            String[] dev = (String[]) data.get(x);
            System.out.println("[" + dev[2] + "] " + dev[1] + " | " + dev[3] + " | " + dev[0]);
        }
    }
}
