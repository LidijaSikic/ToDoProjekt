package vsite.hr.todoprojekt.db;

import android.provider.BaseColumns;

public class zcContract {
    public static final String DB_NAME = "vsite.hr.todoprojekt.db";
    public static final int DB_VERSION = 2;

    public class UlazniPodaci implements BaseColumns {
        public static final String TABLE_1 = "zadaci";
        public static final String TABLE_2 = "citati";

        public static final String COL_Z_TASK_TITLE = "zadatak";
        public static final String COL_C_QUOTE_TITLE = "citat";

    }


}

