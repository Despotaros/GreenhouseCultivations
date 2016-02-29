package atzios.greenhouse.cultivations;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * CustomTextView class
 * Ειναι μια κλαση TextView με την δικια της γραμματοσειρα
 * Created by Atzios on 30/10/2015.
 */
public class CustomTextView extends TextView {

    public CustomTextView(Context context) {
        super(context);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Δημιουργουμε την γραμματοσειρα απο την αποθηκευμενη που εχουμε στα assets
     */
    private void init() {
        if(!this.isInEditMode()) {
            Typeface ctf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-LightItalic.ttf");

            if (ctf != null)
                this.setTypeface(ctf);
        }
    }
}
