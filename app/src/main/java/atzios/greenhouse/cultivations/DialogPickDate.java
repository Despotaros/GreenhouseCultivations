package atzios.greenhouse.cultivations;




import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

/**
 * DialogPickDate
 * Διαλογος οπου εμφανιζει και επισρεφει την ημερομηνια που επιλεγει ο χρηστης
 * Created by Atzios on 27/12/2015.
 */
public class DialogPickDate {
    /* Ο διαλογος */
    private Dialog dialog;
    /* Context της εφαρμογης */
    private Context context;
    /* Ο γονεας οπου θα εμφανιζει τον διαλογο */
    private View parent;
    /* Listener οπου επισρεφει την ημερομηνια που επιλεχθηκε */
    private PickDateListener listener;

    /**
     * Constructor
     * @param context Context
     * @param parent Parent
     */
    public DialogPickDate(Context context,View parent) {
        this.context = context;
        this.parent = parent;
    }

    /**
     * Δημιουργει και επιστρεφει τον διαλογο
     * @return Το διαλογο
     */
    public Dialog create() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_pick_date,(ViewGroup)parent,false);
        alertDialog.setView(dialogView);
        final DatePicker datePicker = (DatePicker)dialogView.findViewById(R.id.datePicker);

        alertDialog.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener!=null)
                    listener.onDatePicked(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
            }
        });

        alertDialog.setNegativeButton(R.string.cancel, null);

        dialog = alertDialog.create();
        return dialog;
    }
    public void setOnDatePickListener(PickDateListener listener) {
        this.listener = listener;
    }

    /**
     * Listener που επισρεφει την ημερομηνια
     */
    public interface PickDateListener {
        void onDatePicked(int year,int month,int day);
    }

}
