package com.lymytz.entitymanager.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lymytz.entitymanager.Persistence;
import com.lymytz.entitymanager.db.MetadataConstraint;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Constantes {
    public static int VERSION = 1;
    public static String DATABASENAME;
    public static DatabaseHelper DBHelper;
    public static SQLiteDatabase DATABASE;
    public static Context CONTEXT;
    public static Persistence PERSISTENCE;
    public static MetadataConstraint CONSTRAINT;

    public static final String FILE_SEPARATOR = File.separator;

    public static final String ACTION_CASCADE = "CASCADE";
    public static final String ACTION_NO_ACTION = "NO ACTION";
    public static final String ACTION_SET_NULL = "SET NULL";

    public static final String ACTION_INSERT = "INSERT";
    public static final String ACTION_UPDATE = "UPDATE";
    public static final String ACTION_DELETE = "DELETE";
    public static final String ACTION_TRUNCATE = "TRUNCATE";

    public static final List<String> TABLES_SYSTEM = new ArrayList<String>() {
        {
            add("android_metadata");
            add("metadata_constraint");
            add("metadata_resultat_action");
        }
    };

    public static final DateFormat dm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
}
