package gov.hhs.fha.nhinc.kmr2.simulatorAgent;


import java.io.Serializable;
import java.util.List;

public class Agent implements Serializable {
    
    private String agentId;
    private String name;
    private String type;
    
    private String table;
    private Integer population;
    
    private List<String> subFilters;    
    private String subFilter;    
    private Boolean filtered;
    
    private Integer populationRange;


    public Agent() {
    }

    public Agent(String agentId) {
        this.agentId = agentId;
    }

    public Agent(String agentId, String name, String type, String table, Integer population, List<String> subFilters, String subFilter, Boolean filtered, Integer populationRange) {
        this.agentId = agentId;
        this.name = name;
        this.type = type;
        this.table = table;
        this.population = population;
        this.subFilters = subFilters;
        this.subFilter = subFilter;
        this.filtered = filtered;
        this.populationRange = populationRange;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
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

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public List<String> getSubFilters() {
        return subFilters;
    }

    public void setSubFilters(List<String> subFilters) {
        this.subFilters = subFilters;
    }

    public String getSubFilter() {
        return subFilter;
    }

    public void setSubFilter(String subFilter) {
        this.subFilter = subFilter;
    }

    public Boolean getFiltered() {
        return filtered;
    }

    public void setFiltered(Boolean filtered) {
        this.filtered = filtered;
    }

    public Integer getPopulationRange() {
        return populationRange;
    }

    public void setPopulationRange(Integer populationRange) {
        this.populationRange = populationRange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agent)) return false;

        Agent agent = (Agent) o;

        if (agentId != null ? !agentId.equals(agent.agentId) : agent.agentId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return agentId != null ? agentId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "agentId='" + agentId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", table='" + table + '\'' +
                ", population=" + population +
                ", subFilters=" + subFilters +
                ", subFilter='" + subFilter + '\'' +
                ", filtered=" + filtered +
                ", populationRange=" + populationRange +
                '}';
    }
}