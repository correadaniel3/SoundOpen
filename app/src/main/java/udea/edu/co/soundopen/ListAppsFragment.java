package udea.edu.co.soundopen;


import android.app.Application;
import android.app.ListFragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Console;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class ListAppsFragment extends ListFragment {
    ListView listaApps;
    View view;

    public ListAppsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_list_apps,container,false);
        listaApps= (ListView)view.findViewById(R.id.list);
        System.out.println("view "+listaApps.getClass());
        appsListView();
        return inflater.inflate(R.layout.fragment_list_apps, container, false);

        // String[] apps={"uno","dos"};



    }


    private void appsListView() {

        final PackageManager pm = this.getActivity().getPackageManager();

        List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
         //String[] apps={"uno","dos"};

        ArrayAdapter<ApplicationInfo> adapter=new ArrayAdapter<ApplicationInfo>(
                getActivity(),           //contexto
                R.layout.items,   //layout a usar
                apps             //items
        );
        listaApps.setAdapter(adapter);
    }
}
