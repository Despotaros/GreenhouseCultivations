package atzios.greenhouse.cultivations.contents;

/**
 * Content Class Krataei to plithos twn kaliergion kai ton douleion enos thermokipiou
 * Created by Ατζιος on 19/11/2015.
 */
public class ContentGreenhouseReport {
    private int totalCultivations;
    private int activeCultivations;
    private int totalWorks;
    private int pendingWorks;



    public int getActiveCultivations() {
        return activeCultivations;
    }

    public void setActiveCultivations(int activeCultivations) {
        this.activeCultivations = activeCultivations;
    }
    public int getCompletedCultivations() {
        return totalCultivations - activeCultivations;
    }
    public int getPendingWorks() {
        return pendingWorks;
    }
    public int getCompletedWorks() {
        return totalWorks - pendingWorks;
    }

    public void setPendingWorks(int pendingWorks) {
        this.pendingWorks = pendingWorks;
    }

    public int getTotalCultivations() {
        return totalCultivations;
    }

    public void setTotalCultivations(int totalCultivations) {
        this.totalCultivations = totalCultivations;
    }

    public int getTotalWorks() {
        return totalWorks;
    }

    public void setTotalWorks(int totalWorks) {
        this.totalWorks = totalWorks;
    }
}
