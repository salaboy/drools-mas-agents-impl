/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.mas;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author salaboy
 */
public class MockFactTableModel extends AbstractTableModel {

    String[] columnNames = {"Id",
        "Name",
        "Descr",
        "prop1",
        "prop2", "Confirmed?"};
    private List<MockFactUI> facts = new ArrayList<MockFactUI>(10);
    private Boolean[] informedFacts = new Boolean[10];
    public MockFactTableModel() {
        for(int i = 0; i <10; i ++){
            facts.add(new MockFactUI(new Long(i), "name"+i, "descr"+i, "prop1"+i, "prop2"+i));
            informedFacts[i]=false;
        }
        
    }
    
    public void informFact(int index){
        informedFacts[index] = true;
    }
    public void disconfirmFact(int index){
        informedFacts[index] = false;
    }
    
    public boolean isInformed(int index){
        return informedFacts[index];
    }
   
    @Override
    public String getColumnName(int i) {
        return columnNames[i];
    }

    public int getRowCount() {
        return facts.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int i, int i1) {
        MockFactUI fact = facts.get(i);
        switch(i1){
            case 0:
                return fact.getId();
            case 1:
                return fact.getName();
            case 2:
                return fact.getDescr();
            case 3:
                return fact.getProp1();    
            case 4:
                return fact.getProp2(); 
            case 5:
                return informedFacts[i];   
            default:
                  return "Wrong Index Bro??";
        }
        
    }

    public List<MockFactUI> getFacts() {
        return facts;
    }
    
    
}
