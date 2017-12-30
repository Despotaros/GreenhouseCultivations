package atzios.greenhouse.cultivations;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class Preferences
 * Αποθηκευει και ανακτα τις ρυθμισεις της εφαρμογης
 * Created by Atzios on 30/10/2015.
 */
public class Preferences {
    /**
     * Τιμη που αναφερεται οταν τρεχει πρωτη φορα η εφαρμογη μας
     */
    public final static String FIRST_RUN = "first_run";
    /**
     * Τιμη που αναφερεται στο αν εχουμε δειξει στον χρηστη το πλευρικο συρταρι
     */
    public final static String DRAWER_STATE = "drawer_state";
    public final static String DAYS_BEFORE_NOTIFY = "days_before_notify";
    public final static String NOTIFICATIONS_STATUS = "notification_status";
    public final static String NAME_OF_USER = "name_of_user";
    public final static String LASTNAME_OF_USER = "last_name_of_user";
    /**
     * Το ονομα του αρχειου οπου θα αποθηκευονται η ρυθμισεις μας
     */
    private String pref_name = "shared_pref";
    /**
     * Application context
     */
    private Context context;

    /**
     * Constructor
     * @param context Context
     */
    public Preferences(Context context) {

        this.context = context;
    }

    /**
     * Επιστρεφει μια μεταβλητη boolean που αντιστοιχει στο κλειδι
     * @param key Το κλειδι
     * @param defaultValue προκαθορισμενη τιμη
     * @return Η τιμη του κλειδιου αν βρεθηκε αλλιως η προκαθορισμενη τιμη
     */
    public boolean getBoolean(String key,boolean defaultValue) {
        return context.getSharedPreferences(pref_name,Context.MODE_PRIVATE).getBoolean(key,defaultValue);
    }
    /**
     * Επιστρεφει μια μεταβλητη String που αντιστοιχει στο κλειδι
     * @param key Το κλειδι
     * @param defaultValue προκαθορισμενη τιμη
     * @return Η τιμη του κλειδιου αν βρεθηκε αλλιως η προκαθορισμενη τιμη
     */
    public String getString(String key,String defaultValue) {
        return context.getSharedPreferences(pref_name,Context.MODE_PRIVATE).getString(key, defaultValue);
    }
    /**
     * Επιστρεφει μια μεταβλητη Int που αντιστοιχει στο κλειδι
     * @param key Το κλειδι
     * @param defaultValue προκαθορισμενη τιμη
     * @return Η τιμη του κλειδιου αν βρεθηκε αλλιως η προκαθορισμενη τιμη
     */
    public int getInt(String key,int defaultValue) {
        return context.getSharedPreferences(pref_name,Context.MODE_PRIVATE).getInt(key, defaultValue);
    }
    /**
     * Επιστρεφει μια μεταβλητη Float που αντιστοιχει στο κλειδι
     * @param key Το κλειδι
     * @param defaultValue προκαθορισμενη τιμη
     * @return Η τιμη του κλειδιου αν βρεθηκε αλλιως η προκαθορισμενη τιμη
     */
    public float getFloat(String key,float defaultValue) {
        return context.getSharedPreferences(pref_name,Context.MODE_PRIVATE).getFloat(key,defaultValue);
    }
    /**
     * Αποθηκευει την τιμη που περνει το value στο αντιστοιχο key
     * @param key Το κλειδι
     * @param value Η τιμη του
     */
    public void put(String key,boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(pref_name,Context.MODE_PRIVATE).edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    /**
     * Αποθηκευει την τιμη που περνει το value στο αντιστοιχο key
     * @param key Το κλειδι
     * @param value Η τιμη του
     */
    public void put(String key,String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(pref_name,Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }
    /**
     * Αποθηκευει την τιμη που περνει το value στο αντιστοιχο key
     * @param key Το κλειδι
     * @param value Η τιμη του
     */
    public void put(String key,int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(pref_name,Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }
    /**
     * Αποθηκευει την τιμη που περνει το value στο αντιστοιχο key
     * @param key Το κλειδι
     * @param value Η τιμη του
     */
    public void put(String key,float value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(pref_name,Context.MODE_PRIVATE).edit();
        editor.putFloat(key,value);
        editor.apply();
    }
}
