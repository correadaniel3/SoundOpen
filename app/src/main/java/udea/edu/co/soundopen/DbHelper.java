package udea.edu.co.soundopen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by corre on 9/10/2016.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();

    public DbHelper(Context context) {
        super(context, StatusContract.DB_NAME, null, StatusContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlUser = String
                .format("create table %s (%s int primary key autoincrement, %s text, %s text unique, %s blob)",
                        StatusContract.TABLE_USER,
                        StatusContract.Column_soundApps.ID,
                        StatusContract.Column_soundApps.sound,
                        StatusContract.Column_soundApps.app,
                        StatusContract.Column_soundApps.icon);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + StatusContract.TABLE_USER);
    }
}
