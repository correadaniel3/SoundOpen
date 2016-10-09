package udea.edu.co.soundopen;

/**
 * Created by correa on 9/10/2016.
 */

import android.provider.BaseColumns;
public class StatusContract {

    public static final String DB_NAME = "soundopen.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_USER= "sound";

    public class Column_soundApps {
        public static final String ID = BaseColumns._ID;
        public static final String sound = "sound";
        public static final String app = "app";
        public static final String icon = "icon";
    }

}
