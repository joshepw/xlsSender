/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.List;

/**
 *
 * @author Owner
 */
public class taskList implements Runnable{
    
    private static List<taskDevice> list;
    
    public taskList(List<taskDevice> data){
        list = data;
    }
    
    @Override
    public void run() {
        try{
            mainFrame.setButtom(false);
            mainFrame.textLed("Inicializando Tareas");
            Thread.sleep(300);
            for(int i=0;i<list.size();i++){
                list.get(i).run();
            }
            mainFrame.clearLed();
            mainFrame.setButtom(true);
        }catch(Exception ex){
            System.out.println("ERROR >> "+ex.getMessage());
        }
    }
    
}
