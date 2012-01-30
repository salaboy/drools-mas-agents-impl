package gov.hhs.fha.nhinc.kmr2.simulatorAgent;


import java.io.Serializable;
import java.util.List;

public class Constraint implements Serializable {
    
    private String constraintId;
    private String name;
    private String description;
    
    private List<String> importances;
    private String importance;
    
    private Boolean required;
    private Boolean included;

    public Constraint() {
    }

    public Constraint(String constraintId) {
        this.constraintId = constraintId;
    }

    public Constraint(String constraintId, String name, String description, List<String> importances, String importance, Boolean required, Boolean included) {
        this.constraintId = constraintId;
        this.name = name;
        this.description = description;
        this.importances = importances;
        this.importance = importance;
        this.required = required;
        this.included = included;
    }

    public String getConstraintId() {
        return constraintId;
    }

    public void setConstraintId(String constraintId) {
        this.constraintId = constraintId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImportances() {
        return importances;
    }

    public void setImportances(List<String> importances) {
        this.importances = importances;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getIncluded() {
        return included;
    }

    public void setIncluded(Boolean included) {
        this.included = included;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Constraint)) return false;

        Constraint that = (Constraint) o;

        if (constraintId != null ? !constraintId.equals(that.constraintId) : that.constraintId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return constraintId != null ? constraintId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Constraint{" +
                "constraintId='" + constraintId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", importances=" + importances +
                ", importance='" + importance + '\'' +
                ", required=" + required +
                ", included=" + included +
                '}';
    }
}

