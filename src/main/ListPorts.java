/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import gnu.io.CommPortIdentifier;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public final class ListPorts implements Runnable{
    
    Thread list = null;
    
    public ListPorts(){
        list = new Thread(this);
        list.start();
    }
    
    @Override
    public void run() {
        mainFrame.textLed("Buscando puertos disponibles, porfavor espere!");
        mainFrame.colorLed(Color.RED);
        List<String> DV = new ArrayList<>();
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        mainFrame.textLed("Cargando modulos . . .");
        showModems.update.setEnabled(false);
        showModems.show.setEnabled(false);
        mainFrame.showDevices.setEnabled(false);
        mainFrame.addDevice.setEnabled(false);
        showModems.update.setText(". . . .");
        int count = 0;
        while (portList.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
            mainFrame.textLed("Verificando puerto "+portId.getName());
            mainFrame.colorLed(Color.RED);
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {                
                sms.Device temp = new sms.Device(portId.getName());
                if(temp.isModem()){
                    DV.add(portId.getName());
                    count++;
                }
            }
        }
        if(count==0){
            DV.add("- - - - -");
        }
        mainFrame.clearLed();
        mainFrame.colorLed(Color.BLACK);
        mainFrame.portListCombo = DV;
        mainFrame.comm.setModel(new CBModel(DV));
        showModems.DV = DV;
        showModems.show.setEnabled(true);
        showModems.update.setEnabled(true);
        mainFrame.showDevices.setEnabled(true);
        mainFrame.addDevice.setEnabled(true);
        showModems.update.setText("Actualizar");
        showModems.comm.setModel(new CBModel(DV));
    }
}