/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.mas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import urn.gov.hhs.fha.nhinc.adapter.fact.Fact;
import urn.gov.hhs.fha.nhinc.adapter.fact.IndividualFactory;

/**
 *
 * @author salaboy
 */
public class MockFactTableModel extends AbstractTableModel {

    String[] columnNames = {
        "Name",
        "Created",
        "Confirmed?"};
    private List<Fact> facts = new ArrayList<Fact>(10);
    private List<String> names = new ArrayList<String>();
    private Boolean[] informedFacts;

    public MockFactTableModel() {
        Map<String, Object> map = IndividualFactory.getNamedIndividuals();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Fact) {
                names.add(entry.getKey());
                facts.add((Fact) entry.getValue());
            }
            informedFacts = new Boolean[facts.size()];
            for(int i = 0; i < informedFacts.length; i++){
                informedFacts[i] = false;
            }
//            System.out.println("Entry: "+entry.getValue());
//            System.out.println("Entry: "+entry.getKey());
//            ((Fact)object).getClass().getName(); --> remove Impl
//                ((Fact)object).date


        }

    }

    public void informFact(int index) {
        informedFacts[index] = true;
    }

    public void disconfirmFact(int index) {
        informedFacts[index] = false;
    }

    public boolean isInformed(int index) {
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
        Fact fact = facts.get(i);
        switch (i1) {
            case 0:
                return names.get(i);
            case 1:
                return fact.getDateTimeCreatedDate();
            case 2:
                return informedFacts[i];
            default:
                return "Wrong Index Bro??";
        }

    }

    public List<Fact> getFacts() {
        return facts;
    }
}
