package atzios.greenhouse.cultivations.contents;

/**
 * Content class for cultivation
 * Contains all cultivation's properties
 * Created by Atzios Vasilis on 16/12/2014.
 */
public class ContentCultivation {
    private int id; //Cultivation id
    private int categoryId; //Cultivation category id
    private String name; //Cultivation name
    private String comments;
    private int monthDuration; //Cultivation duration in months

    public ContentCultivation() {
        id = -1;
        name = null;
        comments = null;
        monthDuration = 0;
        categoryId = -1;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getMonthDuration() {
        return monthDuration;
    }

    public void setMonthDuration(int monthDuration) {
        this.monthDuration = monthDuration;
    }
}
