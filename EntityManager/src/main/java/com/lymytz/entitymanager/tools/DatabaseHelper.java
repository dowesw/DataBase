package com.lymytz.entitymanager.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.lymytz.entitymanager.db.MetadataConstraint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;

    String TABLE_NAME;
    String QUERY;

    static String DATABASE_NAME;

    private static DatabaseHelper INSTANCE;
    private static SQLiteDatabase db;

    private static final String TAG = DatabaseHelper.class.getName();

    public static synchronized DatabaseHelper getHelper(Context CONTEXT, String TABLE_NAME, int VERSION) {
        String QUERY = "CREATE TABLE IF NOT EXISTS android_metadata (locale TEXT)";
        return getHelper(CONTEXT, TABLE_NAME, QUERY, VERSION);
    }

    public static synchronized DatabaseHelper getHelper(Context CONTEXT, String TABLE_NAME, String QUERY, int VERSION) {
        if (INSTANCE == null) {
            DATABASE_NAME = Constantes.DATABASENAME;
            INSTANCE = new DatabaseHelper(CONTEXT, TABLE_NAME, QUERY, VERSION);
            if (Constantes.CONSTRAINT == null) {
                Constantes.CONSTRAINT = new MetadataConstraint().Open();
            }
        }
        return INSTANCE;
    }

    private DatabaseHelper(Context CONTEXT, String TABLE_NAME, String QUERY, int VERSION) {
        super(CONTEXT, DATABASE_NAME, null, VERSION);
        this.context = CONTEXT;
        this.TABLE_NAME = TABLE_NAME;
        this.QUERY = QUERY;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            execSQL(db, "PRAGMA foreign_keys=ON;");
        }
    }

    public SQLiteDatabase Open() {
        if (db != null ? !db.isOpen() : true) {
            try {
                db = getWritableDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return db;
    }

    public SQLiteDatabase Open(String QUERY) {
        db = Open();
        if (db != null && Utils.asString(QUERY)) {
            execSQL(db, QUERY);
        }
        return db;
    }

    public void Close() {
        db.close();
    }

    public void onDestroy() {
        INSTANCE = null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        execSQL(db, QUERY);
    }

    public void execSQL(SQLiteDatabase db, String sql){
        db.beginTransaction();
        try {
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Messages.Exception(ex);
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(newVersion);
    }

    @SuppressLint("ShowToast")
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "Mise a jour de la base de donnee version " + oldVersion + " to " + newVersion);
        // You will not need to modify this unless you need to do some android specific things.
        // When upgrading the database, all you need to do is add a file to the assets folder and name it:
        // from_1_to_2.sql with the version that you are upgrading to as the last version.
        try {
            for (int i = oldVersion; i < newVersion; ++i) {
                String migrationName = String.format("database_from_%d_to_%d.sql", i, (i + 1));
                Log.d(TAG, "Looking for migration file: " + migrationName);
                readAndExecuteSQLScript(db, context, migrationName);
            }
        } catch (Exception exception) {
            Log.e(TAG, "Exception running upgrade script:", exception);
        }
    }

    private void readAndExecuteSQLScript(SQLiteDatabase db, Context ctx, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            Log.d(TAG, "SQL script file name is empty");
            return;
        }

        Log.d(TAG, "Script found. Executing...");
        AssetManager assetManager = ctx.getAssets();
        BufferedReader reader = null;

        try {
            InputStream is = assetManager.open(fileName);
            InputStreamReader isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            executeSQLScript(db, reader);
        } catch (IOException e) {
            Log.e(TAG, "IOException:", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException:", e);
                }
            }
        }

    }

    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        String line;
        StringBuilder statement = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            statement.append(line);
            statement.append("\n");
            if (line.endsWith(";")) {
                execSQL(db, statement.toString());
                statement = new StringBuilder();
            }
        }
    }
}
