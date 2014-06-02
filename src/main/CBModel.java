/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Owner
 */
class CBModel extends AbstractListModel implements ComboBoxModel {
    private String[] items = null;
    private String seleccionado = null;

    public CBModel(String[] data) {
        if(data.length<1){
            items = new String[1];
            items[0] = null;
        }else{
            items = data;
            seleccionado = items[0];
        }
    }
    
    public CBModel(List data) {
        items = new String[data.size()];
        for(int i=0;i<data.size();i++){
            items[i] = (String) data.get(i);
        }
        seleccionado = items[0];
    }
    
    @Override
    public int getSize() {
        return items.length;
    }

    @Override
    public Object getElementAt(int index) {
        return items[index];
    }

    @Override
    public void setSelectedItem(Object anItem) {
        seleccionado = (String)anItem;
    }

    @Override
    public Object getSelectedItem() {
        return seleccionado;
    }

}
