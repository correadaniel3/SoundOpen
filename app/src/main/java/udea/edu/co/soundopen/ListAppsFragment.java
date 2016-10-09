package udea.edu.co.soundopen;


import android.app.ListFragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class ListAppsFragment extends ListFragment {

    View v;

    public ListAppsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        appsListView();
        return inflater.inflate(R.layout.fragment_list_apps, container, false);

        // String[] apps={"uno","dos"};



    }


    private void appsListView() {

        final PackageManager pm = getContext().getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        // String[] apps={"uno","dos"};

        ArrayAdapter<ApplicationInfo> adapter=new ArrayAdapter<ApplicationInfo>(
                getContext(),           //contexto
                R.layout.items,   //layout a usar
                apps             //items
        );

        ListView lista= (ListView) v.findViewById(R.id.listApps);
        lista.setAdapter(adapter);
    }
}
