package udea.edu.co.soundopen;

import android.app.Fragment;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    Fragment listaApps;
    Spinner spin;
    private ImageView targetImageR;
    DbHelper dbH;
    SQLiteDatabase db;
    Uri contact = null;
    Button btnR;
    Button appl;
    private static final int REQUEST_CODE_APP=1;
    EditText[] txtValidateR = new EditText[4];
    View view;
    TextView aplicacion;
    List<ApplicationInfo> apps;
    ApplicationInfo target;
    ApplicationInfo[] info;
    String[]name;
    Intent startIntent;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dbH=new DbHelper(getActivity().getBaseContext());
        view=inflater.inflate(R.layout.fragment_add_sound,container,false);
        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        targetImageR = (ImageView)view.findViewById(R.id.appIcon);
        targetImageR.setImageBitmap(icon);
        txtValidateR[0]=(EditText)view.findViewById(R.id.editTextSName);
        btnR = (Button)view.findViewById(R.id.buttonSound);
        btnR.setEnabled(true);
        spin=(Spinner)view.findViewById(R.id.spinner);
        play=(ImageButton)view.findViewById(R.id.playButton);
        stop=(ImageButton)view.findViewById(R.id.stopButton);
        record=(ImageButton)view.findViewById(R.id.recordButton);
        //appl=(Button)view.findViewById(R.id.buttonApp);
        stop.setEnabled(false);
        play.setEnabled(false);
        aplicacion=(TextView)view.findViewById(R.id.aplicacion);


        //---------------------------------------------spinner---------------------------------

        final PackageManager pm = this.getActivity().getPackageManager();
        apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        //String[] apps={"uno","dos"};

        info=new ApplicationInfo[apps.size()];
        name=new String[apps.size()];
        for(int i=0;i<apps.size();i++){
            info[i]=apps.get(i);
        }
        for(int i=0;i<apps.size();i++){
            name[i]=apps.get(i).loadLabel(pm).toString();
        }
        /*System.out.println(name[0]+" "+info[0]);
        ArrayList<String> apps2=new ArrayList<>();
        for(ApplicationInfo apli:apps){
            apps2.add(apli.loadLabel(pm).toString());
        }
        Collections.sort(apps2.subList(1, apps2.size()));*/

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                getActivity(),           //contexto
                R.layout.items,   //layout a usar
                name             //items
        );

        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String a=(String) spin.getSelectedItem();
                int posi=0;
                for(int i=0;i<name.length;i++){
                    if(name[i].equals(a)){
                        posi=i;
                    }
                }
                target=info[posi];
                aplicacion.setText(a);
                String packageName = target.packageName;
                startIntent = pm.getLaunchIntentForPackage(packageName);
                /*if(startIntent != null){
                    startActivity(startIntent);
                }*/
                targetImageR.setImageDrawable(target.loadIcon(pm));
                if(startIntent != null){
                    Toast.makeText(getActivity(),startIntent.toString(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });




        //---------------------------------------------fin spinner-----------------------------


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

        /*appl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getActivity(),"App Seleccionada "+String.valueOf(spin.getSelectedItem()), Toast.LENGTH_LONG).show();
                String a=(String) spin.getSelectedItem();
                aplicacion.setText(a);


            }
        });*/

        btnR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //Toast.makeText(getActivity(),"App Seleccionada "+String.valueOf(spin.getSelectedItem()), Toast.LENGTH_LONG).show();
                //ValidarRace();
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+txtValidateR[0].getText()+".wav";

                //outputFile ="res/raw/uno.wav";

                myAudioRecorder=new MediaRecorder();
                myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                myAudioRecorder.setOutputFile(outputFile);
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
                Toast.makeText(getActivity(),"Recording started", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.stop();
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
                myAudioRecorder.release();
                myAudioRecorder  = null;

                stop.setEnabled(false);
                play.setEnabled(true);

                Toast.makeText(getActivity(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Toast.makeText(getActivity(), "Playing audio", Toast.LENGTH_LONG).show();
            }
        });
/*        for (int n = 0; n < txtValidateR.length; n++)
        {
            txtValidateR[n].addTextChangedListener(btnActivation);
        }*/
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
        values.put(StatusContract.Column_soundApps.sound,Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+txtValidateR[0].getText()+".wav"); //cambiar por la URI del sonido
        values.put(StatusContract.Column_soundApps.app,startIntent.toString()); //Cambiar por la actividad de la aplicacion
        values.put(StatusContract.Column_soundApps.icon,getBitmapAsByteArray(icon));
        db.insertWithOnConflict(StatusContract.TABLE_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }


}
