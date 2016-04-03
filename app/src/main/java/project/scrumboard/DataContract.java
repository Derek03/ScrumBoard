package project.scrumboard;

import android.provider.BaseColumns;

/**
 * Created by Spencer on 3/29/2016.
 */
public final class DataContract
{
    private DataContract()
    {
    }

    public static abstract class DataEntry
            implements BaseColumns
    {
        public static final String TABLE_NAME = "data";
        public static final String COLUMN_NAME_VALUE = "value";
    }
}