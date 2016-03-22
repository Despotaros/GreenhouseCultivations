package atzios.greenhouse.cultivations.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.InputType;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;



import atzios.greenhouse.cultivations.Greenhouse;
import atzios.greenhouse.cultivations.PermissionManager;
import atzios.greenhouse.cultivations.R;

import atzios.greenhouse.cultivations.contents.ContentGreenhouse;
import atzios.greenhouse.cultivations.contents.ContentGreenhouseReport;
import atzios.greenhouse.cultivations.datahelpers.DataHelperGreenhouse;


/**
 * Fragment info
 * Περιέχει συγκεντρωτικα ολα στοιχεια του επιλεγμενου θερμοκηπιου.
 * Created by Atzios on 8/9/2015.
 */
public class FragmentInfo extends Fragment {
    /* Το TAG του fragment */
    public static final String TAG = "FragmentInfo";
    /* Προκαθορισμενές μεταβλητες του Fragment */
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int RESULT_OK = -1;
    /* Το μονοπάτι της εικονας του θερμοκηπιου */
    private String mCurrentImagePath;
    /* Το θερμοκηπιο */
    private ContentGreenhouse greenhouse = new ContentGreenhouse();
    /* Το View του fragment info */
    private View mView;
    /* Callbacks του fragment info */
    private FragmentInfoCallbacks listener;

    /**
     * Setter για τα callbacks του fragment
     * @param listener  O listener
     */
    public void setFragmentCallbacks(FragmentInfoCallbacks listener) {
        this.listener = listener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Ενημερώνουμε οτι το fragment εχει το δικο του μενου ετσι ωστε να επικαλυψει το μενου
         * του activity */
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Το layout που διχνει το fragment */
        mView = inflater.inflate(R.layout.fragment_info, container, false);

        viewClickListeners();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        /* φωρτωνουμε τα στοιχεια του fragment απο την βαση */
        loadData();
    }

    /**
     * Δημιουργουμε ολα τα events των views του fragment (onclicks,touch κτλπ).
     */
    private void viewClickListeners() {
        ImageButton btn = (ImageButton)mView.findViewById(R.id.btnCamera);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Ελεγχουμε πρωτα αν εχουμε το δικαιωμα να διαβασουμε και να γραψουμε **/
                if(PermissionManager.checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    dispatchTakePictureIntent();
                }
                else if (PermissionManager.requestPermission(getActivity(), FragmentInfo.this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        PermissionManager.PERMISSION_CODE_REQUEST_STORAGE)) {
                    dispatchTakePictureIntent();
                }
            }
        });

        View view = mView.findViewById(R.id.cvName);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.update);
                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit, (ViewGroup) mView, false);
                builder.setView(view);

                final EditText ed = (EditText) view.findViewById(R.id.editText);
                ed.setHint(R.string.hint_greenhouse_name);
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        greenhouse.setName(ed.getText().toString());
                        loadData();
                        updateGreenhouse();
                        if(listener != null)
                            listener.onNameUpdated();
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.create().show();
            }
        });


        view = mView.findViewById(R.id.cvAddress);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dimiourgoume ton dialogo
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //Titlos
                builder.setTitle(R.string.update);

                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit,(ViewGroup)mView,false);
                builder.setView(view);

                final EditText ed = (EditText)view.findViewById(R.id.editText);
                ed.setHint(R.string.hint_greenhouse_address);
               // ed.setText(greenhouse.getName());
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        greenhouse.setAddress(ed.getText().toString());
                        loadData();
                        updateGreenhouse();

                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.create().show();
            }
        });

        view = mView.findViewById(R.id.cvArea);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dimiourgoume ton dialogo
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //Titlos
                builder.setTitle(R.string.update);

                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit,(ViewGroup)mView,false);
                builder.setView(view);

                final EditText ed = (EditText)view.findViewById(R.id.editText);
                ed.setHint(R.string.hint_greenhouse_area);
                //Thetoume ton typo pliktrologiou telos
                ed.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                // ed.setText(greenhouse.getName());
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        try {
                            greenhouse.setArea(Double.parseDouble(ed.getText().toString()));
                            loadData();
                            updateGreenhouse();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }


                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.create().show();
            }
        });

        view = mView.findViewById(R.id.cvCultivations);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onFragmentFocus(R.id.drawer_cultivations);
            }
        });
        view = mView.findViewById(R.id.cvWorks);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onFragmentFocus(R.id.drawer_work);
            }
        });

        FloatingActionButton fab =  (FloatingActionButton) mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.delete);
                builder.setMessage(R.string.confirm_delete);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataHelperGreenhouse gHelper = new DataHelperGreenhouse(getActivity());
                        if(gHelper.getNames().size() > 1) {
                            gHelper.delete(Greenhouse.getInstance().getContent().getId(),true);
                            loadData();
                            updateGreenhouse();
                            if (listener != null)
                                listener.onNameUpdated();
                        }

                    }
                });
                builder.setNegativeButton(R.string.cancel,null);
                builder.create().show();
            }
        });
    }

    /**
     * LoadData
     * Φορτωνει ολα τα δεδομενα στα TextViews απο την βαση
     */
    private void loadData() {
        if(greenhouse != null) {
            TextView tv = (TextView) mView.findViewById(R.id.tvGH);
            tv.setText(greenhouse.getName());
            tv = (TextView) mView.findViewById(R.id.tvAddress);
            tv.setText(greenhouse.getAddress());
            tv = (TextView) mView.findViewById(R.id.tvArea);
            tv.setText(Double.toString(greenhouse.getArea()));
            //Pernoume to report tou thermokipiou

           // Context context = getActivity().getApplicationContext();

            ContentGreenhouseReport report = Greenhouse.getInstance().getReport();

            tv =(TextView)mView.findViewById(R.id.tvTotalCultivations);
            tv.setText(Integer.toString(report.getTotalCultivations()));

            tv =(TextView)mView.findViewById(R.id.tvActiveCultivations);
            tv.setText(Integer.toString(report.getActiveCultivations()));

            tv =(TextView)mView.findViewById(R.id.tvCompletedCultvations);
            tv.setText(Integer.toString(report.getCompletedCultivations()));

            tv =(TextView)mView.findViewById(R.id.tvTotalWorks);
            tv.setText(Integer.toString(report.getTotalWorks()));

            tv =(TextView)mView.findViewById(R.id.tvCompletedWorks);
            tv.setText(Integer.toString(report.getCompletedWorks()));

            tv =(TextView)mView.findViewById(R.id.tvPendingWorks);
            tv.setText(Integer.toString(report.getPendingWorks()));

            if(listener!=null)
                listener.onPictureTaken(greenhouse.getImagePath());
        }
    }

    /**
     * Ενημερωνει το επιλεγμενο θερμοκηπιο
     */
    private void updateGreenhouse() {
        Context context = getActivity().getApplicationContext();
        DataHelperGreenhouse dHelper = new DataHelperGreenhouse(context);
        dHelper.update(greenhouse);
    }
    /**
     * Θετει ποιο θερμοκηπιο ειναι το επιλεγμενο
     */
    public void refresh() {
        greenhouse = Greenhouse.getInstance().getContent();
        loadData();

    }

    /**
     * Καλουμε το activity για να τραβηξουμε μια φωτογραφια
     */
    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
        else
            Toast.makeText(getActivity(),"null",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionManager.PERMISSION_CODE_REQUEST_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted
                    dispatchTakePictureIntent();
                }
                else //Toast a message to the user that permission denied(By him)
                    Toast.makeText(getActivity(),R.string.permission_denied,Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    /**
     * Δημιουργουμε ενα αρχειο για να αποθηκευσουμε την εικονα
     * @return Το αρχειο
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
       // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String path = Environment.getExternalStorageDirectory().toString() + "/Greenhouse cultivations/"+greenhouse.getId()+"-"+greenhouse.getName();
        File storageDir = new File(path) ;
        if(!storageDir.exists())
            storageDir.mkdirs();
        storageDir = new File(path+"/image.jpeg");
        if(storageDir.createNewFile())
            mCurrentImagePath = storageDir.getAbsolutePath();
        return storageDir;
    }
    /**
     * Αν τραβηξαμε σωστα την φωτογραφεια ενημερωσε την εικονα στην βαση, και στο drawer
     * @param requestCode Ο κωδικος της εκκινησης
     * @param resultCode Ο κωδικος του αποτελεσματος
     * @param data Τα επιστεφομενα δεδομενα
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            greenhouse.setImagePath(mCurrentImagePath);
            updateGreenhouse();
            if(listener!=null)
                listener.onPictureTaken(mCurrentImagePath);

        }
    }

    /**
     * Τα callbacks του activity
     */
    public interface FragmentInfoCallbacks {
        void onNameUpdated();
        void onPictureTaken(String path);
        void onFragmentFocus(int id);
    }


}
