/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.mas;

import java.io.Serializable;

/**
 *
 * @author salaboy
 */
public class MockFactUI implements Serializable{
    private Long id;
    private String name;
    private String descr;
    private String prop1;
    private String prop2;

    public MockFactUI(Long id, String name, String descr, String prop1, String prop2) {
        this.id = id;
        this.name = name;
        this.descr = descr;
        this.prop1 = prop1;
        this.prop2 = prop2;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }

    public String getProp2() {
        return prop2;
    }

    public void setProp2(String prop2) {
        this.prop2 = prop2;
    }

    @Override
    public String toString() {
        return "MockFact{" + "id=" + id + ", name=" + name + ", descr=" + descr + ", prop1=" + prop1 + ", prop2=" + prop2 + '}';
    }
    
    
    
}
