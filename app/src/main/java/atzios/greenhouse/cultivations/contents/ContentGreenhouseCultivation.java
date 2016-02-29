package atzios.greenhouse.cultivations.contents;

/**
 * Content Class Greenhouse Cultivation
 * Contains all properties of greenhouse cultivation
 * Created by Atzios Vasilis on 22/12/2014.
 */
public class ContentGreenhouseCultivation {

    private int id;
    private int greenhouseId;
    private int cultivationId;
    private long date;
    private String comments;
    private boolean active;
    private int duration;


    public ContentGreenhouseCultivation() {
        id = -1;
        duration = 0;
        active = true;
        comments = "";
        date = 0 ;
        cultivationId = -1;
        greenhouseId = -1;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGreenhouseId() {
        return greenhouseId;
    }

    public void setGreenhouseId(int greenhouseId) {
        this.greenhouseId = greenhouseId;
    }

    public int getCultivationId() {
        return cultivationId;
    }

    public void setCultivationId(int cultivationId) {
        this.cultivationId = cultivationId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isActive() {
        return active;
    }
    public int getActive() {
        return active? 1 : 0 ;
    }
    public void setActive(int active) {
        this.active = active == 1? true:false;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
