package th.ac.dusit.dbizcom.bagculate.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import th.ac.dusit.dbizcom.bagculate.model.Bag;
import th.ac.dusit.dbizcom.bagculate.model.History;
import th.ac.dusit.dbizcom.bagculate.model.Object;
import th.ac.dusit.dbizcom.bagculate.model.User;

public class LocalDb {

    private static final String TAG = LocalDb.class.getName();
    public static final String STORED_DATE_FORMAT = "yyyy-MM-dd";

    private static final String DATABASE_NAME = "bagculate.db";
    //เวอร์ชั่น 2 เพิ่มเทเบิล history และ history_details
    private static final int DATABASE_VERSION = 2;

    // เทเบิล user
    // +-----+----------+------+
    // | _id | username | name |
    // +-----+----------+------+
    // |     |          |      |

    private static final String TABLE_USER = "user";
    private static final String COL_ID = "_id";
    private static final String COL_USERNAME = "username";
    private static final String COL_NAME = "name";

    private static final String SQL_CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + "("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_USERNAME + " TEXT, "
                    + COL_NAME + " TEXT "
                    + ")";

    // เทเบิล history
    // +-----+--------+----------+----------+------------+------------+
    // | _id | bag_id | bag_name | bag_type | bag_weight | created_at |
    // +-----+--------+----------+----------+------------+------------+
    // |     |        |          |          |            |            |

    private static final String TABLE_HISTORY = "history";
    private static final String COL_BAG_ID = "bag_id";
    private static final String COL_BAG_NAME = "bag_name";
    private static final String COL_BAG_TYPE = "bag_type";
    private static final String COL_BAG_WEIGHT = "bag_weight";
    private static final String COL_CREATED_AT = "created_at";

    private static final String SQL_CREATE_TABLE_HISTORY =
            "CREATE TABLE " + TABLE_HISTORY + "("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_BAG_ID + " INTEGER, "
                    + COL_BAG_NAME + " TEXT, "
                    + COL_BAG_TYPE + " INTEGER, "
                    + COL_BAG_WEIGHT + " REAL, "
                    + COL_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP "
                    + ")";

    // เทเบิล history_details
    // +-----+------------+-----------+-------------+-------------+---------------+-------+
    // | _id | history_id | object_id | object_name | object_type | object_weight | count |
    // +-----+------------+-----------+-------------+-------------+---------------+-------+
    // |     |            |           |             |             |               |       |

    private static final String TABLE_HISTORY_DETAILS = "history_details";
    private static final String COL_HISTORY_ID = "history_id";
    private static final String COL_OBJECT_ID = "object_id";
    private static final String COL_OBJECT_NAME = "object_name";
    private static final String COL_OBJECT_TYPE = "object_type";
    private static final String COL_OBJECT_WEIGHT = "object_weight";
    private static final String COL_COUNT = "count";

    private static final String SQL_CREATE_TABLE_HISTORY_DETAILS =
            "CREATE TABLE " + TABLE_HISTORY_DETAILS + "("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_HISTORY_ID + " INTEGER, "
                    + COL_OBJECT_ID + " INTEGER, "
                    + COL_OBJECT_NAME + " TEXT, "
                    + COL_OBJECT_TYPE + " TEXT, "
                    + COL_OBJECT_WEIGHT + " INTEGER, "
                    + COL_COUNT + " INTEGER "
                    + ")";

    private static DatabaseHelper sDbHelper;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public LocalDb(Context context) {
        mContext = context.getApplicationContext();

        if (sDbHelper == null) {
            sDbHelper = new DatabaseHelper(context);
        }
        mDatabase = sDbHelper.getWritableDatabase();
    }

    public boolean logUser(User user) {
        clearUser();
        ContentValues cv = new ContentValues();
        cv.put(COL_ID, user.id);
        cv.put(COL_USERNAME, user.username);
        cv.put(COL_NAME, user.name);
        return mDatabase.insert(TABLE_USER, null, cv) != -1;
    }

    public int clearUser() {
        return mDatabase.delete(TABLE_USER, null, null);
    }

    public User getUser() {
        Cursor cursor = mDatabase.query(
                TABLE_USER,
                null,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        } else {
            cursor.moveToFirst();
            User user = new User(
                    cursor.getInt(cursor.getColumnIndex(COL_ID)),
                    cursor.getString(cursor.getColumnIndex(COL_USERNAME)),
                    cursor.getString(cursor.getColumnIndex(COL_NAME))
            );
            cursor.close();
            return user;
        }
    }

    public void addHistory(Bag bag, List<Object> objectList) {
        ContentValues cv = new ContentValues();
        cv.put(COL_BAG_ID, bag.id);
        cv.put(COL_BAG_NAME, bag.name);
        cv.put(COL_BAG_TYPE, bag.type);
        cv.put(COL_BAG_WEIGHT, bag.weight);
        long insertId = mDatabase.insert(TABLE_HISTORY, null, cv);

        for (Object o : objectList) {
            cv = new ContentValues();
            cv.put(COL_HISTORY_ID, insertId);
            cv.put(COL_OBJECT_ID, o.id);
            cv.put(COL_OBJECT_NAME, o.name);
            cv.put(COL_OBJECT_TYPE, o.type);
            cv.put(COL_OBJECT_WEIGHT, o.weight);
            cv.put(COL_COUNT, o.count);
            mDatabase.insert(TABLE_HISTORY_DETAILS, null, cv);
        }
    }

    public List<History> getHistory() {
        List<History> historyList = new ArrayList<>();

        Cursor cursor = mDatabase.query(TABLE_HISTORY, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
            String createdAt = cursor.getString(cursor.getColumnIndex(COL_CREATED_AT));

            int bagId = cursor.getInt(cursor.getColumnIndex(COL_BAG_ID));
            String bagName = cursor.getString(cursor.getColumnIndex(COL_BAG_NAME));
            int bagType = cursor.getInt(cursor.getColumnIndex(COL_BAG_TYPE));
            double bagWeight = cursor.getDouble(cursor.getColumnIndex(COL_BAG_WEIGHT));
            Bag bag = new Bag(bagId, bagName, bagType, bagWeight);

            History history = new History(id, createdAt, bag, new ArrayList<Object>());
            historyList.add(history);

            Cursor objectListCursor = mDatabase.query(
                    TABLE_HISTORY_DETAILS,
                    null,
                    COL_HISTORY_ID + "=?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null
            );
            while (objectListCursor.moveToNext()) {
                int objectId = objectListCursor.getInt(objectListCursor.getColumnIndex(COL_OBJECT_ID));
                String objectName = objectListCursor.getString(objectListCursor.getColumnIndex(COL_OBJECT_NAME));
                String objectType = objectListCursor.getString(objectListCursor.getColumnIndex(COL_OBJECT_TYPE));
                int objectWeight = objectListCursor.getInt(objectListCursor.getColumnIndex(COL_OBJECT_WEIGHT));
                int count = objectListCursor.getInt(objectListCursor.getColumnIndex(COL_COUNT));
                Object object = new Object(objectId, objectName, objectType, objectWeight, count);

                history.objectList.add(object);
            }
            objectListCursor.close();
        }
        cursor.close();

        return historyList;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLE_USER);
            db.execSQL(SQL_CREATE_TABLE_HISTORY);
            db.execSQL(SQL_CREATE_TABLE_HISTORY_DETAILS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY_DETAILS);
            onCreate(db);
        }
    }
}