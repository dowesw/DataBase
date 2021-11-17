package com.lymytz.entitymanager;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Build;
import android.os.Environment;

import com.lymytz.entitymanager.annotations.Table;
import com.lymytz.entitymanager.tools.Chemins;
import com.lymytz.entitymanager.tools.Constantes;
import com.lymytz.entitymanager.bean.Tables;
import com.lymytz.entitymanager.tools.Messages;
import com.lymytz.entitymanager.tools.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Persistence {

    public static List<Tables> TABLES = new ArrayList<>();
    static List<String> class_maps;
    static Persistence instantce;

    public static Persistence newInstance(List<String> peristence, Context context, String databaseName, int version) {
        if (instantce == null) {
            instantce = new Persistence();
            class_maps = peristence;
        }
        Constantes.CONTEXT = context;
        Constantes.DATABASENAME = databaseName;
        Constantes.VERSION = version;
        return instantce;
    }

    private Persistence() {
    }

    public void fixCursorWindowSize() {
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 102400 * 1024); //the 102400 is the new size added
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create() {
        new Thread(() -> {
            try {
                for (String value : class_maps) {
                    Class class_map = Class.forName(value);
                    if (class_map.isAnnotationPresent(Table.class)) {
                        String name = Utils.getTableName(class_map);
                        if (Utils.asString(name)) {
                            List<String> descriptors = Utils.getTableDescriptor(class_map);
                            Field key = Utils.getId(class_map);
                            String libelle = Utils.getTableLibelle(class_map);
                            String groupe = Utils.getTableGroupe(class_map);
                            String sequence = Utils.getSequenceName(key);
                            int index = TABLES.indexOf(new Tables(name));
                            if (index < 0) {
                                Tables table = new Tables(name, value);
                                table.setLibelle(libelle);
                                table.setGroupe(groupe);
                                table.setSequence(sequence);
                                table.setClasse(class_map);
                                table.setContext(Constantes.CONTEXT);
                                table.setQueryCreate(Utils.queryCreateTable(class_map));
                                table.setColumns(Utils.getColumns(class_map));
                                table.setDescriptors(descriptors);
                                table.setId(Utils.getPrimaryKey(key, name));
                                if (!Utils.existTable(name)) {
                                    boolean create = Utils.createTable(table.getQueryCreate(), Constantes.VERSION);
                                    if (!create) {
                                        continue;
                                    }
                                }
                                TABLES.add(table);
                            } else {
                                TABLES.get(index).setLibelle(libelle);
                                TABLES.get(index).setGroupe(groupe);
                                TABLES.get(index).setSequence(sequence);
                                TABLES.get(index).setClasse(class_map);
                                TABLES.get(index).setClasseName(value);
                                TABLES.get(index).setDescriptors(descriptors);
                                TABLES.get(index).setContext(Constantes.CONTEXT);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean delete() {
        try {
            if (Constantes.CONTEXT != null ? Utils.asString(Constantes.DATABASENAME) : false) {
                execSQL("DELETE FROM yvs_autorisation_module");
                execSQL("DELETE FROM yvs_autorisation_page_module");
                execSQL("DELETE FROM yvs_autorisation_ressources_page");
                execSQL("DELETE FROM yvs_setting_compte");
                execSQL("DELETE FROM yvs_setting_serveur");
                boolean delete = Constantes.CONTEXT.deleteDatabase(Constantes.DATABASENAME);
                if (!delete) {
                    File data = Environment.getDataDirectory();
                    File dbFile = new File(data, "//data//" + "com.lymytz.erp" + "//databases//" + Constantes.DATABASENAME);
                    if (dbFile.exists()) {
                        dbFile.delete();
                    }
                    TABLES.clear();
                    Constantes.DBHelper.onDestroy();
                    Constantes.DBHelper.Close();
                    Constantes.DATABASE = null;
                }
                return delete;
            }
        } catch (Exception ex) {
           Messages.Exception(ex);
        }
        return false;
    }

    public int getPosition(String tablename) {
        return getPosition(new Tables(tablename));
    }

    public int getPosition(Tables item) {
        return TABLES.indexOf(item);
    }

    public Tables getTable(String tablename) {
        return getTable(new Tables(tablename));
    }

    public Tables getTable(Tables item) {
        int position = getPosition(item);
        if (position > -1) {
            return TABLES.get(position);
        }
        return null;
    }

    public Tables getTable(int position) {
        if (position > -1) {
            return TABLES.get(position);
        }
        return null;
    }

    public void add(Tables item) {
        if (!TABLES.contains(item)) {
            TABLES.add(item);
        }
    }

    public int size() {
        return TABLES.size();
    }

    public static long insert(String table, String nullColumnHack, ContentValues values) {
        long id = -1;
        Utils.getDatabase().beginTransaction();
        try {
            if (values == null) {
                return -1;
            }
            values.remove("id");
            if (values.size() < 1) {
                return -1;
            }
            if (values.size() == 1 ? values.containsKey("null") : false) {
                return -1;
            }
            id = Utils.getDatabase().insert(table, nullColumnHack, values);
            Utils.getDatabase().setTransactionSuccessful();
        } catch (Exception ex) {
            Messages.Exception(ex);
        } finally {
            Utils.getDatabase().endTransaction();
        }
        System.err.println("ID (" + table + ") : " + id);
        return id;
    }

    public static boolean update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        boolean result = false;
        Utils.getDatabase().beginTransaction();
        try {
            result = Utils.getDatabase().update(table, values, whereClause, whereArgs) > 0;
            Utils.getDatabase().setTransactionSuccessful();
        } catch (Exception ex) {
            Messages.Exception(ex);
        } finally {
            Utils.getDatabase().endTransaction();
        }
        return result;
    }

    public static boolean delete(String table, String whereClause, String[] whereArgs) {
        boolean result = false;
        Utils.getDatabase().beginTransaction();
        try {
            result = Utils.getDatabase().delete(table, whereClause, whereArgs) > 0;
            Utils.getDatabase().setTransactionSuccessful();
        } catch (Exception ex) {
            Messages.Exception(ex);
        } finally {
            Utils.getDatabase().endTransaction();
        }
        return result;
    }

    public static Cursor query(String table, String[] columns, String whereClause, String[] whereArgs) {
        return query(false, table, columns, whereClause, whereArgs, null, null, null, null);
    }

    public static Cursor query(String table, String[] columns, String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {
        return query(false, table, columns, whereClause, whereArgs, groupBy, having, orderBy, null);
    }

    public static Cursor query(boolean distint, String table, String[] columns, String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {
        return query(distint, table, columns, whereClause, whereArgs, groupBy, having, orderBy, null);
    }

    public static Cursor query(String table, String[] columns, String whereClause, String[] whereArgs, String groupBy, String having, String orderBy, String limit) {
        return query(false, table, columns, whereClause, whereArgs, groupBy, having, orderBy, limit);
    }

    public static Cursor query(boolean distint, String table, String[] columns, String whereClause, String[] whereArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor result = Utils.DefautlCursor();
        Utils.getDatabase().beginTransaction();
        try {
            result = Utils.getDatabase().query(distint, table, columns, whereClause, whereArgs, groupBy, having, orderBy, limit);
            Utils.getDatabase().setTransactionSuccessful();
        } catch (Exception ex) {
            Messages.Exception(ex);
        } finally {
            Utils.getDatabase().endTransaction();
        }
        return result;
    }

    public static Cursor rawQuery(String sql, String[] selectionArgs) {
        Cursor result = Utils.DefautlCursor();
        Utils.getDatabase().beginTransaction();
        try {
            result = Utils.getDatabase().rawQuery(sql, selectionArgs);
            Utils.getDatabase().setTransactionSuccessful();
        } catch (Exception ex) {
            Messages.Exception(ex);
        } finally {
            Utils.getDatabase().endTransaction();
        }
        return result;
    }

    public static String stringOne(String query) {
        try {
            try (Cursor c = rawQuery(query, new String[]{})) {
                while (c.moveToNext()) {
                    return c.getString(0);
                }
            } catch (Exception ex) {
                Messages.Exception(ex);
            }
        } catch (Exception ex) {
            Messages.Exception(ex);
        }
        return null;
    }

    public static void execSQL(String sql, Object[] whereArgs) {
        Utils.getDatabase().beginTransaction();
        try {
            Utils.getDatabase().execSQL(sql, whereArgs);
            Utils.getDatabase().setTransactionSuccessful();
        } catch (Exception ex) {
            Messages.Exception(ex);
        } finally {
            Utils.getDatabase().endTransaction();
        }
    }

    public static void execSQL(String sql) {
        Utils.getDatabase().beginTransaction();
        try {
            Utils.getDatabase().execSQL(sql);
            Utils.getDatabase().setTransactionSuccessful();
        } catch (Exception ex) {
            Messages.Exception(ex);
        } finally {
            Utils.getDatabase().endTransaction();
        }
    }

    public void importDB(String backupDBPath) {
        try {
            String currentDBPath = "//data//" + "PackageName" + "//databases//" + "DatabaseName";

            File data = Environment.getDataDirectory();
            File backupDB = new File(data, currentDBPath);
            File currentDB = new File(backupDBPath);
            if (backupDB.exists()) {
                backupDB.delete();
            }

            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();

            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
        } catch (Exception ex) {
            Messages.Exception(ex);
        }
    }

    public String exportDB(Context context) {
        try {
            String dataBaseName = "lymytz_demo_0";
            String currentDBPath = "//data//" + "com.lymytz.erp" + "//databases//" + dataBaseName;

            File data = Environment.getDataDirectory();
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(Chemins.Backup(context), dataBaseName);
            if (backupDB.exists()) {
                backupDB.renameTo(new File(Chemins.Backup(context), "lymytz_demo_" + new Date().getTime()));
            }

            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();

            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
            return backupDB.toString();
        } catch (Exception ex) {
            Messages.Exception(ex);
        }
        return null;
    }
}
