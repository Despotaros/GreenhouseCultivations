package atzios.greenhouse.cultivations;


import android.util.Log;

import java.util.ArrayList;

/**
 * StoreFragmentClass
 * Ειναι μια κλαση βοηθος οπου αποθηκευει fragments,titles και τα αντιστοιχει με
 * ενα κλειδι ωστε να ειναι ευκολοτερη η αποθηκευση και η ανακτηση.
 * Created by Atzios on 16/12/2015.
 */
public class StoreFragmentManager<Value> {
    /* Αποθηκευονται τα fragments σαν γενικος τυπος value γιατι αποθηκευουμε διαφορετικους
     * τυπους fragment */
    private ArrayList<Value> fragments;
    /* Λιστα που κραταει τα titles των fragments*/
    private ArrayList<String> titles;
    /* Λιστα που κραταει τα keys των fragment*/
    private ArrayList<String> keys;

    /*Αναφορα αντιστοιχίσεις των keys,fragments και titles ειναι το positions τους */


    /*
     * Constructor
     */
    public StoreFragmentManager() {
        fragments = new ArrayList<>();
        keys = new ArrayList<>();
        titles = new ArrayList<>();
    }

    /**
     * Προσθετη ενα νεο fragment στον manager
     * @param fragment Το fragment
     * @param key To μοναδικο κλειδι του
     * @param title Ο τιτλος του fragment
     */
    public void addFragment(Value fragment,String key,String title) {
        /*Κλειδια με null value δεν επιτρεπονται*/
        if(key == null) {
            Log.e("StoreFragmentManager","Null key is not allowed");
            return;
        }
        /* Ελεχγουμε το κλειδι οτι ειναι μοναδικο */
        boolean unique = true;
        for(int i = 0 ; i <keys.size();i++) {
            if(keys.get(i).equals(key)) {
                unique = false;
                break;
            }
        }
        /* Εαν ειναι μοναδικο τοτε μπορεις να το αποθηκευσεις */
        if(unique) {
            titles.add(title);
            fragments.add(fragment);
            keys.add(key);
        }
        else
            Log.e("StoreFragmentManager", "Key:" + key + " Already exists.Try to remove first before add" +
                    "a fragment with the same key!!!");
    }

    /**
     * Επιστρεφει το key στην αντιστοιχη θεση
     * @param position η θεση που ψαχνουμε
     * @return το κλειδι αν υπαρχει αλλιως επιστρεφει null
     */
    public String getKey(int position) {
        try {
            return keys.get(position);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            Log.e("StoreFragmentManager","Cannot find the key in the given position:"+position);
            return null;
        }
    }

    /**
     * Επιστρεφει το fragment με το αντιστοιχο κλειδι
     * @param key το κλειδι
     * @return το fragment αν υπαρχει το κλειδι αλλιως
     */
    public Value getFragment(String key) {
        Value f = null;
        int pos = -1;
        for(int i=0 ; i<keys.size(); i++) {
            if(keys.get(i).equals(key)) {
                pos = i;
                break;
            }
        }
        //Fragment found...
        if(pos >=0 ) {
            f = fragments.get(pos);
        }
        else {
            Log.e("StoreFragmentManager","I cannot found a fragment with the given key to return...");
        }
        return f;

    }

    /**
     * Το πληθος των αποθηκευμενων fragment
     * @return το πληθος
     */
    public int size() {
        return keys.size();
    }

    /**
     * Επιστρεφει τον τιτλο του fragment που αντιστοιχει στο κλειδι
     * @param key το κλειδι
     * @return ο τιτλος του fragments
     */
    public String getTitle(String key) {

        int pos = -1;
        for(int i=0 ; i<keys.size(); i++) {
            if(keys.get(i).equals(key)) {
                pos = i;
                break;
            }
        }
        String title = "";
        //Fragment found...
        if(pos >=0 ) {
            title = titles.get(pos);
        }
        else {
            Log.e("StoreFragmentManager","I cannot found a title with the given key to return...");
        }
        return title;
    }

    /**
     * Καθαριζει ολα τα keys,fragments,titles.
     */
    public void clear() {
        fragments.clear();
        keys.clear();
        titles.clear();
    }

    /**
     * Αφαιρει ενα fragment,title με το αντιστοιχο κλειδι
     * @param key Το κλειδι
     * @return true αν αφαιρεθηκε το κλειδι αλλιως false .
     */
    public boolean removeFragment(String key) {

        int pos = -1;
        for(int i=0 ; i<keys.size(); i++) {
            if(keys.get(i).equals(key)) {
                pos = i;
                break;
            }
        }
        //Fragment found...
        if(pos >=0 ) {
            fragments.remove(pos);
            keys.remove(pos);
            titles.remove(pos);
            return true;
        }
        else {
            Log.e("StoreFragmentManager","I cannot found a fragment with the given key to remove...");
            return  false;
        }

    }
}
