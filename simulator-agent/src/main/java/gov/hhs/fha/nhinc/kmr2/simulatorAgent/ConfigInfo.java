package gov.hhs.fha.nhinc.kmr2.simulatorAgent;


import java.io.Serializable;
import java.util.Date;

public class ConfigInfo implements Serializable {
    
    private String configId;
    
    private String name;
    private String author;
    private String description;
    
    private Date createdDate;
    private Date lastSavedDate;
    private Date lastRunDate;
    
    private String goal;
    private Double score;
    
    private Date startDate;
    private Date endDate;
    private Long duration;

    public ConfigInfo() {
    }

    public ConfigInfo(String configId) {
        this.configId = configId;
    }

    public ConfigInfo(String configId, String name, String author, String description, Date createdDate, Date lastSavedDate, Date lastRunDate, String goal, Double score, Date startDate, Date endDate, Long duration) {
        this.configId = configId;
        this.name = name;
        this.author = author;
        this.description = description;
        this.createdDate = createdDate;
        this.lastSavedDate = lastSavedDate;
        this.lastRunDate = lastRunDate;
        this.goal = goal;
        this.score = score;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }


    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastSavedDate() {
        return lastSavedDate;
    }

    public void setLastSavedDate(Date lastSavedDate) {
        this.lastSavedDate = lastSavedDate;
    }

    public Date getLastRunDate() {
        return lastRunDate;
    }

    public void setLastRunDate(Date lastRunDate) {
        this.lastRunDate = lastRunDate;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "ConfigInfo{" +
                "configId='" + configId + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
                ", lastSavedDate=" + lastSavedDate +
                ", lastRunDate=" + lastRunDate +
                ", goal='" + goal + '\'' +
                ", score=" + score +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigInfo)) return false;

        ConfigInfo that = (ConfigInfo) o;

        if (configId != null ? !configId.equals(that.configId) : that.configId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return configId != null ? configId.hashCode() : 0;
    }
}
