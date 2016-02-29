package atzios.greenhouse.cultivations;

import atzios.greenhouse.cultivations.contents.ContentGreenhouse;
import atzios.greenhouse.cultivations.contents.ContentGreenhouseReport;

/**
 * Κραταμε το ενεργο θερμοκηπιο με sigleton
 * για να εχουμε προσβαση σε αυτο απο οποιοδηποτε fragment ή activity
 * Created by Atzios on 28/12/2015.
 */
public class Greenhouse {
    /* Το θερμοκηπιο */
    private ContentGreenhouse content;
    private ContentGreenhouseReport report;
    /* Instance του θερμοκηπιου */
    private static Greenhouse instance;

    /**
     * Ιδιωτικος constructor για να μην επιτρεπει την κατασκευη αλλου θερμοκηπιου
     */
    private Greenhouse() {
        content = new ContentGreenhouse();
        report = new ContentGreenhouseReport();

    }

    /**
     * Δημιουργει και επισρεφει το θερμοκηπιο
     * @return
     */
    public static Greenhouse getInstance() {
        if(instance == null)
            instance = new Greenhouse();
        return instance;
    }

    public ContentGreenhouseReport getReport() {
        return report;
    }

    public void setReport(ContentGreenhouseReport report) {
        this.report = report;
    }

    public void setContent(ContentGreenhouse content) {
        this.content = content;
    }
    public ContentGreenhouse getContent() {
        return content;
    }
}
