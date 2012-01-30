package gov.hhs.fha.nhinc.kmr2.simulatorAgent;


import java.io.Serializable;

public class ScoreDetails implements Serializable {

    private String constraintId;
    private String type;
    private String description;

    private Integer violations;
    private Integer violationLimit;

    private String penalties;
    private String relevance;

    private String aggregateScore;
    private String constraintScore;

    public ScoreDetails() {
    }

    public ScoreDetails(String constraintId) {
        this.constraintId = constraintId;
    }

    public ScoreDetails(String constraintId, String type, String description, Integer violations, Integer violationLimit, String penalties, String relevance, String aggregateScore, String constraintScore) {
        this.constraintId = constraintId;
        this.type = type;
        this.description = description;
        this.violations = violations;
        this.violationLimit = violationLimit;
        this.penalties = penalties;
        this.relevance = relevance;
        this.aggregateScore = aggregateScore;
        this.constraintScore = constraintScore;
    }

    public String getConstraintId() {
        return constraintId;
    }

    public void setConstraintId(String constraintId) {
        this.constraintId = constraintId;
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

    public Integer getViolations() {
        return violations;
    }

    public void setViolations(Integer violations) {
        this.violations = violations;
    }

    public Integer getViolationLimit() {
        return violationLimit;
    }

    public void setViolationLimit(Integer violationLimit) {
        this.violationLimit = violationLimit;
    }

    public String getPenalties() {
        return penalties;
    }

    public void setPenalties(String penalties) {
        this.penalties = penalties;
    }

    public String getRelevance() {
        return relevance;
    }

    public void setRelevance(String relevance) {
        this.relevance = relevance;
    }

    public String getAggregateScore() {
        return aggregateScore;
    }

    public void setAggregateScore(String aggregateScore) {
        this.aggregateScore = aggregateScore;
    }

    public String getConstraintScore() {
        return constraintScore;
    }

    public void setConstraintScore(String constraintScore) {
        this.constraintScore = constraintScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScoreDetails)) return false;

        ScoreDetails that = (ScoreDetails) o;

        if (constraintId != null ? !constraintId.equals(that.constraintId) : that.constraintId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return constraintId != null ? constraintId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ScoreDetails{" +
                "constraintId='" + constraintId + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", violations=" + violations +
                ", violationLimit=" + violationLimit +
                ", penalties='" + penalties + '\'' +
                ", relevance='" + relevance + '\'' +
                ", aggregateScore='" + aggregateScore + '\'' +
                ", constraintScore='" + constraintScore + '\'' +
                '}';
    }
}

