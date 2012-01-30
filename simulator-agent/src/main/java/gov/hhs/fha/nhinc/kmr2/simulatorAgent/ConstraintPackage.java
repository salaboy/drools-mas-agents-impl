package gov.hhs.fha.nhinc.kmr2.simulatorAgent;

import java.io.Serializable;
import java.util.List;

public class ConstraintPackage implements Serializable {
    
    private String name;
    private String type;
    private String description;

    private List<Constraint> constraints;

    public ConstraintPackage() {
    }

    public ConstraintPackage(String name) {
        this.name = name;
    }

    public ConstraintPackage(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public ConstraintPackage(String name, String type, String description, List<Constraint> constraints) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.constraints = constraints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstraintPackage)) return false;

        ConstraintPackage that = (ConstraintPackage) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConstraintPackage{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", constraints=" + constraints +
                '}';
    }
}
    
