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

/**
 *
 * @author Owner
 */
public class UpdatePorts implements Runnable{
    Thread list = null;
    
    public UpdatePorts(){
        list = new Thread(this);
        list.start();
    }
    
    @Override
    public void run() {
        List<String> DV = new ArrayList<>();
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        showModems.update.setEnabled(false);
        showModems.show.setEnabled(false);
        showModems.update.setText(". . . .");
        while (portList.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                sms.Device temp = new sms.Device(portId.getName());
                if(temp.isModem()){
                    DV.add(portId.getName());
                }
            }
        }
        showModems.DV = DV;
        showModems.show.setEnabled(true);
        showModems.update.setEnabled(true);
        showModems.update.setText("Actualizar");
        showModems.comm.setModel(new CBModel(DV));
    }
}
