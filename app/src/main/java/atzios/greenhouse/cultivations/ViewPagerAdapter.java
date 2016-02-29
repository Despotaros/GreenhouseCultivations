package atzios.greenhouse.cultivations;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.view.ViewGroup;



/**
 * ViewPagerAdapter
 * Ειναι μια κλαση βοηθος οπου προσαρμοζει τα fragment μας σε ενα view pager αντικειμενο
 * Created by Atzios on 9/9/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter  {

    private  Activity mContext;
    /* Τα fragment οπου περιεχει ο adapter μας */
    private StoreFragmentManager<Fragment> mFragments;
    /* Listener οπου ενεργοποιειται καθε φορα που ενα fragment αλλαζει στον adapter μας */
    private onFinishUpdate listener;

    /**
     * Constructor
     * @param fm Fragment Manager
     * @param activity Activity
     */
    public ViewPagerAdapter(FragmentManager fm,Activity activity) {
        super(fm);
        mContext = activity;
        mFragments = new StoreFragmentManager<>();

    }

    /**
     * Setter για τον listener
     * @param listener
     */
    public void setOnFinishUpdateListener(onFinishUpdate listener) {
        this.listener = listener;
    }

    /**
     * Προσθετη ενα fragment στον adapter μας
     * @param fragment Το fragment
     * @param key Το κλειδι
     * @param title Ο τιτλος
     */
    public void addFragment(Fragment fragment,String key,String title){
        mFragments.addFragment(fragment,key,title);
        notifyDataSetChanged();

    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if(listener!=null)
            listener.onFinishUpdate();
        super.finishUpdate(container);
    }

    @Override
    public Fragment getItem(int position) {
        String key = mFragments.getKey(position);
        if(key!=null)
            return mFragments.getFragment(key);
        else
            return null;

    }

    @Override
    public int getItemPosition(Object object) {
        /* θΕΤΟΥΜΕ ΤΟ POSITION ΠΑΝΤΑ ΣΤΗΝ ΘΕΣΗ NONE ΓΙΑ ΝΑ ΑΝΑΓΚΑΖΟΥΜΕ ΤΟΝ ADAPTER,
         * ΚΑΘΕ ΦΟΡΑ ΠΟΥ ΚΑΛΕΙΤΕ Η NOTIFY DATA CHANGE ΝΑ ΕΝΗΜΕΡΩΝΕΙ ΟΛΑ ΤΑ FRAGMENT,
         * TOY ADAPTER.
         */
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String key = mFragments.getKey(position);
        if(key!=null)
            return mFragments.getTitle(key);
        else
            return "";
    }

    /**
     * Event listener οπου ενεργοποιειται οταν αλλαζει ενα fragment μεσα στο viewpager
     */
    public interface onFinishUpdate {
        void onFinishUpdate();
    }


}
