package udea.edu.co.soundopen;

import android.app.Fragment;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;

import android.support.v4.app.FragmentActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by corre on 9/10/2016.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class AddSoundAppFragment extends Fragment {
    ImageButton play,stop,record;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    Bitmap icon;
    FragmentTransaction manager;
    Fragment listApps;
    private ImageView targetImageR;
    DbHelper dbH;
    SQLiteDatabase db;
    Uri contact = null;
    Button btnR;
    private static final int REQUEST_CODE_APP=1;
    EditText[] txtValidateR = new EditText[4];
    View view;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dbH=new DbHelper(getActivity().getBaseContext());
        view=inflater.inflate(R.layout.fragment_add_sound,container,false);
        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        targetImageR = (ImageView)view.findViewById(R.id.appIcon);
        targetImageR.setImageBitmap(icon);
        txtValidateR[0]=(EditText)view.findViewById(R.id.editTextSName);
        btnR = (Button)view.findViewById(R.id.buttonSound);
        btnR.setEnabled(false);
        play=(ImageButton)view.findViewById(R.id.playButton);
        stop=(ImageButton)view.findViewById(R.id.stopButton);
        record=(ImageButton)view.findViewById(R.id.recordButton);

        stop.setEnabled(false);
        play.setEnabled(false);
        //outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ txtValidateR[0]+".wav";

        outputFile ="android.resource://udea.edu.co.soundopen/raw/"+ txtValidateR[0]+".wav";

        myAudioRecorder=new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

        TextWatcher btnActivation = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(verificarVaciosSinMessageR(txtValidateR)){btnR.setEnabled(true);}
                else{btnR.setEnabled(false);}
            }
        };
        for (int n = 0; n < txtValidateR.length; n++)
        {
            txtValidateR[n].addTextChangedListener(btnActivation);
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == getActivity().RESULT_OK && (requestCode==REQUEST_CODE_APP)){
            try {
                Uri targetUri = data.getData();
                icon = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                targetImageR.setImageBitmap(icon);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean verificarVaciosSinMessageR(EditText[] txtValidate)
    {
        for(int i=0; i<txtValidate.length;i++)
        {
            if((txtValidate[i].getText().toString()).isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
    public void ValidarRace() {
        db = dbH.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor search = db.rawQuery("select count(*) from " + StatusContract.TABLE_USER, null);
        search.moveToFirst();
        values.put(StatusContract.Column_soundApps.sound,txtValidateR[0].getText().toString()); //cambiar por la URI del sonido
        values.put(StatusContract.Column_soundApps.app,txtValidateR[1].getText().toString()); //Cambiar por la actividad de la aplicacion
        values.put(StatusContract.Column_soundApps.icon,getBitmapAsByteArray(icon));
        db.insertWithOnConflict(StatusContract.TABLE_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }

    public void recordClic() {
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        }

        catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        record.setEnabled(false);
        stop.setEnabled(true);


        //Toast.makeText(getContext(),"Recording started", Toast.LENGTH_LONG).show();
    }
    public void stopClic() {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder  = null;

        stop.setEnabled(false);
        play.setEnabled(true);

       // Toast.makeText(getContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
    }

    public void playClic()throws IllegalArgumentException,SecurityException,IllegalStateException{
        MediaPlayer m = new MediaPlayer();

        try {
            m.setDataSource(outputFile);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            m.prepare();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        m.start();
       // Toast.makeText(getContext(), "Playing audio", Toast.LENGTH_LONG).show();
    }
    public void AppClic(){
        manager = getFragmentManager().beginTransaction();
        listApps =  new ListAppsFragment();
        manager.replace(R.id.fragment_container,listApps);
    }

}
