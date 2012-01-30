package gov.hhs.fha.nhinc.kmr2.simulatorAgent;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Results implements Serializable {

    private String resultId;
    private String type;
    private String configId;
    
    private String status;

    private List<String> tableHeadings;
    private Map<String,String> tableOutputs;

    public Results() {
    }

    public Results(String resultId) {
        this.resultId = resultId;
    }

    public Results(String resultId, String type, String status, String configId, List<String> tableHeadings, Map<String, String> tableOutputs) {
        this.resultId = resultId;
        this.type = type;
        this.status = status;
        this.configId = configId;
        this.tableHeadings = tableHeadings;
        this.tableOutputs = tableOutputs;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public List<String> getTableHeadings() {
        return tableHeadings;
    }

    public void setTableHeadings(List<String> tableHeadings) {
        this.tableHeadings = tableHeadings;
    }

    public Map<String, String> getTableOutputs() {
        return tableOutputs;
    }

    public void setTableOutputs(Map<String, String> tableOutputs) {
        this.tableOutputs = tableOutputs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Results)) return false;

        Results results = (Results) o;

        if (resultId != null ? !resultId.equals(results.resultId) : results.resultId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return resultId != null ? resultId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Results{" +
                "resultId='" + resultId + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", configId='" + configId + '\'' +
                ", tableHeadings=" + tableHeadings +
                ", tableOutputs=" + tableOutputs +
                '}';
    }
}
