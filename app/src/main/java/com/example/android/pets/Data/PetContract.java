package com.example.android.pets.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dhanya on 22/05/17.
 */

public final class PetContract {

        public final static String CONTENT_AUTHORITY="com.example.android.pets";
        public final static String PATH_PETS="pets";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        private PetContract(){}
        public static final class PetEntry implements BaseColumns
        {
            public final static Uri Content_urI= Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);
            public final static String TABLE_NAME="pets";

            public final static String COLUMN_PET_ID=BaseColumns._ID;
            public final static String COLUMN_PET_NAME="name";
            public final static String COLUMN_PET_BREED="breed";
            public final static String COLUMN_PET_GENDER="gender";
            public final static String COLUMN_PET_WEIGHT="weight";

            public static final int GENDER_UNKNOWN=0;
            public static final int GENDER_MALE=1;
            public static final int GENDER_FEMALE=2;



        }

}
