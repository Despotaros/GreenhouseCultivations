package atzios.greenhouse.cultivations.contents;

/**
 * Content class gia to idos ergasias
 * Periexei oles tis idiotites enos idous ergarsias
 * Created by Atzios Vasilis on 16/12/2014.
 */
public class ContentJob {
    private int id; //To id tis ergasias
    private String name; //To onoma tis ergasias
    private String comments; //Sxolia

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ContentJob() {
        id = 0;
        name = null;
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
}
