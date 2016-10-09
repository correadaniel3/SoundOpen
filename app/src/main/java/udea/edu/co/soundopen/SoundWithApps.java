package udea.edu.co.soundopen;

import android.app.ListFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by corre on 9/10/2016.
 */

public class SoundWithApps extends ListFragment {
    DbHelper dbH;
    SQLiteDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbH=new DbHelper(getActivity().getBaseContext());
        ListarSonidosApps();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void ListarSonidosApps() {
        ArrayList<String> sound = new ArrayList();
        ArrayList<String> app = new ArrayList();
        ArrayList icon = new ArrayList();
        boolean control=false;
        db=dbH.getReadableDatabase();
        Cursor test=db.rawQuery("select * from "+StatusContract.TABLE_USER+" order by "+StatusContract.Column_soundApps.ID, null);

        if (test.moveToFirst()) {
            do {
                sound.add(test.getString(1));
                app.add(test.getString(2));
                icon.add(test.getBlob(3));
            }while(test.moveToNext());
            control=true;
        } else{
            Toast.makeText(getActivity().getBaseContext(),getString(R.string.no_sounds),Toast.LENGTH_LONG).show();
        }
        db.close();

        if(control) {
            ArrayList aList=new ArrayList();
            for (int i = 0; i < sound.size(); i++) {
                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("name", "Nombre: " + sound.get(i));
                hm.put("app", "Aplicacion : " + app.get(i));
                hm.put("icon", BitmapFactory.decodeByteArray((byte[]) icon.get(i), 0, ((byte[]) icon.get(i)).length));
                aList.add(hm);
            }
            String from[];
            int to[];
            from = new String[]{"name", "app", "icon"};
            to = new int[]{R.id.name, R.id.app, R.id.icono};
            ExtendedSimpleAdapter adapter = new ExtendedSimpleAdapter(getActivity().getBaseContext(), aList, R.layout.content_main, from, to);
            setListAdapter(adapter);
        }
    }
}
