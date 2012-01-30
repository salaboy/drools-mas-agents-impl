package gov.hhs.fha.nhinc.kmr2.simulatorAgent;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Simulation implements Serializable {

    private String simulationId;
    private String status = "Running";

    private Date createdDate;
    private Long averageIterationTime;
    private String elapsedTime;
    private String timeUnit;

    private Double currentRelativeOevrallScore;
    private Integer currentIteration;
    private Double currentImprovement;
    private Double currentAbsoluteScore;

    private Configuration configuration;

    private List<ScoreDetails> scoreDetails = new ArrayList<ScoreDetails>();

    private List<ScoreStep> chartingData = new ArrayList<ScoreStep>();



    public Simulation() {
    }

    public Simulation(String simulationId) {
        this.simulationId = simulationId;
    }

    public String getSimulationId() {
        return simulationId;
    }

    public void setSimulationId(String simulationId) {
        this.simulationId = simulationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getAverageIterationTime() {
        return averageIterationTime;
    }

    public void setAverageIterationTime(Long averageIterationTime) {
        this.averageIterationTime = averageIterationTime;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Double getCurrentRelativeOevrallScore() {
        return currentRelativeOevrallScore;
    }

    public void setCurrentRelativeOevrallScore(Double currentRelativeOevrallScore) {
        this.currentRelativeOevrallScore = currentRelativeOevrallScore;
    }

    public Integer getCurrentIteration() {
        return currentIteration;
    }

    public void setCurrentIteration(Integer currentIteration) {
        this.currentIteration = currentIteration;
    }

    public Double getCurrentImprovement() {
        return currentImprovement;
    }

    public void setCurrentImprovement(Double currentImprovement) {
        this.currentImprovement = currentImprovement;
    }

    public Double getCurrentAbsoluteScore() {
        return currentAbsoluteScore;
    }

    public void setCurrentAbsoluteScore(Double currentAbsoluteScore) {
        this.currentAbsoluteScore = currentAbsoluteScore;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public List<ScoreDetails> getScoreDetails() {
        return scoreDetails;
    }

    public void setScoreDetails(List<ScoreDetails> scoreDetails) {
        this.scoreDetails = scoreDetails;
    }

    public List<ScoreStep> getChartingData() {
        return chartingData;
    }

    public void setChartingData(List<ScoreStep> chartingData) {
        this.chartingData = chartingData;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Simulation)) return false;

        Simulation that = (Simulation) o;

        if (simulationId != null ? !simulationId.equals(that.simulationId) : that.simulationId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return simulationId != null ? simulationId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Simulation{" +
                "simulationId='" + simulationId + '\'' +
                ", status='" + status + '\'' +
                ", createdDate=" + createdDate +
                ", averageIterationTime=" + averageIterationTime +
                ", elapsedTime='" + elapsedTime + '\'' +
                ", timeUnit='" + timeUnit + '\'' +
                ", currentRelativeOevrallScore=" + currentRelativeOevrallScore +
                ", currentIteration=" + currentIteration +
                ", currentImprovement=" + currentImprovement +
                ", currentAbsoluteScore=" + currentAbsoluteScore +
                ", configuration=" + configuration +
                ", scoreDetails=" + scoreDetails +
                ", chartingData=" + chartingData +
                '}';
    }




    public void start() {
        setStatus( "Running" );
        System.out.println( ">> STARTING SIM " + getSimulationId() );
    }

    public void pause() {
        setStatus( "Paused" );
        System.out.println( ">> PAUSING SIM " + getSimulationId() );
    }

    public void stop() {
        setStatus( "Stopped" );
        System.out.println( ">> STOPPING SIM " + getSimulationId() );
    }




    public static class ScoreStep implements Comparable<ScoreStep>, Serializable {
        private Integer step;
        private Double score;

        public ScoreStep(Integer step, Double score) {
            this.step = step;
            this.score = score;
        }

        public Integer getStep() {
            return step;
        }

        public void setStep(Integer step) {
            this.step = step;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

        public int compareTo(ScoreStep o) {
            return this.step - o.step;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ScoreStep)) return false;

            ScoreStep scoreStep = (ScoreStep) o;

            if (score != null ? !score.equals(scoreStep.score) : scoreStep.score != null) return false;
            if (step != null ? !step.equals(scoreStep.step) : scoreStep.step != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = step != null ? step.hashCode() : 0;
            result = 31 * result + (score != null ? score.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ScoreStep{" +
                    "step=" + step +
                    ", score=" + score +
                    '}';
        }
    }


}

