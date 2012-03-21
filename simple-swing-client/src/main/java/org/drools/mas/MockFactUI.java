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
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MockFactUI other = (MockFactUI) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.descr == null) ? (other.descr != null) : !this.descr.equals(other.descr)) {
            return false;
        }
        if ((this.prop1 == null) ? (other.prop1 != null) : !this.prop1.equals(other.prop1)) {
            return false;
        }
        if ((this.prop2 == null) ? (other.prop2 != null) : !this.prop2.equals(other.prop2)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 31 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 31 * hash + (this.descr != null ? this.descr.hashCode() : 0);
        hash = 31 * hash + (this.prop1 != null ? this.prop1.hashCode() : 0);
        hash = 31 * hash + (this.prop2 != null ? this.prop2.hashCode() : 0);
        return hash;
    }
    
}
