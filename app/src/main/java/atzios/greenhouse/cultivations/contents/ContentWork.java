package atzios.greenhouse.cultivations.contents;


/**
 * Content class for Work
 * Contains all work's properties
 * Created by Panos on 16/12/2014.
 */
public class ContentWork {
    private int id; //Work id
    private int userId; //User id
    private int jobId; //Job id
    private int greenhouseId; //Greenhouse id
    private int cultivationId; //Cultivation id
    private boolean pending; //if work is completed
    private long date;
    private String comments;

    public ContentWork() {
        comments = "";
        date = -1;
        id = -1;
        userId = -1;
        jobId = -1;
        greenhouseId = -1;
        cultivationId = -1;
        pending = false;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
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

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public void setPending(int pending) {
        this.pending = pending == 1 ? true : false ;
    }

    public int getPending() {
        return pending? 1:0;
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
}
