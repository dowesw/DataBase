package com.lymytz.entitymanager.tools;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lymytz.entitymanager.Persistence;
import com.lymytz.entitymanager.annotations.Column;
import com.lymytz.entitymanager.annotations.Descriptor;
import com.lymytz.entitymanager.annotations.Entity;
import com.lymytz.entitymanager.annotations.Id;
import com.lymytz.entitymanager.annotations.JoinColumn;
import com.lymytz.entitymanager.annotations.ManyToOne;
import com.lymytz.entitymanager.annotations.OneToOne;
import com.lymytz.entitymanager.annotations.SequenceGenerator;
import com.lymytz.entitymanager.annotations.Table;
import com.lymytz.entitymanager.annotations.Temporal;
import com.lymytz.entitymanager.annotations.Transient;
import com.lymytz.entitymanager.annotations.XmlTransient;
import com.lymytz.entitymanager.bean.Columns;
import com.lymytz.entitymanager.component.DoubleAdapter;
import com.lymytz.entitymanager.component.android.MySimpleDateFormat;
import com.lymytz.entitymanager.db.MetadataConstraint;
import com.lymytz.entitymanager.enums.FetchType;
import com.lymytz.entitymanager.enums.TemporalType;
import com.lymytz.entitymanager.bean.Tables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {
    public static Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    if (f.getName().equals("serialVersionUID")) {
                        return true;
                    }
                    if (f.getAnnotation(Transient.class) != null) {
                        return true;
                    }
                    if (f.getAnnotation(XmlTransient.class) != null) {
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            }).excludeFieldsWithModifiers(Modifier.TRANSIENT).setDateFormat("dd-MM-yyyy HH:mm:ss")
            .registerTypeAdapter(Double.class, new DoubleAdapter())
            .create();

    public static final DateFormat formatDateOld = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat formatDateTimeOld = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat formatTimeOld = new SimpleDateFormat("HH:mm:ss");

    public static final SimpleDateFormat formatDate = MySimpleDateFormat.get("yyyy-MM-dd");
    public static final SimpleDateFormat formatDateTime = MySimpleDateFormat.get("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat formatTime = MySimpleDateFormat.get("HH:mm:ss");

    static List<Tables> tables = new ArrayList<>();
    static List<Columns> foreigns = new ArrayList<>();

    public static String dateTimeStr(Date date) {
        return Constantes.dm.format(date);
    }

    public static String queryCreateTable(Entity entity) {
        return queryCreateTable(entity.getClass());
    }

    public static Cursor DefautlCursor(){
        return new Cursor() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public boolean move(int offset) {
                return false;
            }

            @Override
            public boolean moveToPosition(int position) {
                return false;
            }

            @Override
            public boolean moveToFirst() {
                return false;
            }

            @Override
            public boolean moveToLast() {
                return false;
            }

            @Override
            public boolean moveToNext() {
                return false;
            }

            @Override
            public boolean moveToPrevious() {
                return false;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean isBeforeFirst() {
                return false;
            }

            @Override
            public boolean isAfterLast() {
                return false;
            }

            @Override
            public int getColumnIndex(String columnName) {
                return 0;
            }

            @Override
            public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
                return 0;
            }

            @Override
            public String getColumnName(int columnIndex) {
                return null;
            }

            @Override
            public String[] getColumnNames() {
                return new String[0];
            }

            @Override
            public int getColumnCount() {
                return 0;
            }

            @Override
            public byte[] getBlob(int columnIndex) {
                return new byte[0];
            }

            @Override
            public String getString(int columnIndex) {
                return null;
            }

            @Override
            public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

            }

            @Override
            public short getShort(int columnIndex) {
                return 0;
            }

            @Override
            public int getInt(int columnIndex) {
                return 0;
            }

            @Override
            public long getLong(int columnIndex) {
                return 0;
            }

            @Override
            public float getFloat(int columnIndex) {
                return 0;
            }

            @Override
            public double getDouble(int columnIndex) {
                return 0;
            }

            @Override
            public int getType(int columnIndex) {
                return 0;
            }

            @Override
            public boolean isNull(int columnIndex) {
                return false;
            }

            @Override
            public void deactivate() {

            }

            @Override
            public boolean requery() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public void registerContentObserver(ContentObserver observer) {

            }

            @Override
            public void unregisterContentObserver(ContentObserver observer) {

            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void setNotificationUri(ContentResolver cr, Uri uri) {

            }

            @Override
            public Uri getNotificationUri() {
                return null;
            }

            @Override
            public boolean getWantsAllOnMoveCalls() {
                return false;
            }

            @Override
            public void setExtras(Bundle extras) {

            }

            @Override
            public Bundle getExtras() {
                return null;
            }

            @Override
            public Bundle respond(Bundle extras) {
                return null;
            }
        };
    }

    public static boolean asString(String value) {
        return value != null ? value.trim().length() > 0 : false;
    }

    public static boolean asBoolean(String value) {
        try {
            if (asString(value) ? value.toLowerCase().equals("true") || value.toLowerCase().equals("false") : false)
                return true;
        } catch (Exception ex) {

        }
        return false;
    }

    public static boolean asNumeric(String value) {
        try {
            Float.valueOf(value);
            return true;
        } catch (Exception ex) {

        }
        return false;
    }

    public static boolean asInteger(String valeur) {
        try {
            if (asString(valeur)) {
                Float _float_ = Float.valueOf(valeur);
                Integer _integer_ = Integer.valueOf(valeur);
                return Math.abs(_float_) - Math.abs(_integer_) <= 0;
            }
        } catch (NumberFormatException ex) {

        }
        return false;
    }

    public static boolean asDate(String value) {
        try {
            new SimpleDateFormat("dd-MM-yyyy").parse(value);
            return true;
        } catch (Exception ex) {

        }
        return false;
    }

    public static Date getPreviewDate(Date date) {
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH, -1);
            return c.getTime();
        }
        return new Date();
    }

    public static Object loadObject(String query, String[] selectionArgs, Class type) {
        try {
            Cursor c = getDatabase().rawQuery(query, selectionArgs);
            try {
                while (c.moveToNext()) {
                    if (type.equals(Double.class)) {
                        return c.getDouble(0);
                    } else if (type.equals(Integer.class)) {
                        return c.getInt(0);
                    } else if (type.equals(String.class)) {
                        return c.getString(0);
                    } else if (type.equals(Boolean.class)) {
                        return c.getInt(0);
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (c != null)
                    c.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean existTable(String table) {
//        String query = "SELECT * FROM sqlite_master WHERE type = 'table'";
        String QUERY = "CREATE TABLE IF NOT EXISTS android_metadata (locale TEXT)";
        try {
            Cursor c = getDatabase().query("sqlite_master", new String[]{"name"}, "type = 'table' AND name = ?", new String[]{table}, null, null, null, "1");
            try {
                while (c.moveToNext()) {
                    return Utils.asString(c.getString(0));
                }
            } catch (Exception ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (c != null)
                    c.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static SQLiteDatabase getDatabase() {
        if (Constantes.DATABASE != null ? !Constantes.DATABASE.isOpen() : true) {
            if (Constantes.DBHelper == null) {
                Constantes.DBHelper = DatabaseHelper.getHelper(Constantes.CONTEXT, Constantes.DATABASENAME, Constantes.VERSION);
            }
            Constantes.DATABASE = Constantes.DBHelper.Open();
        }
        return Constantes.DATABASE;
    }

    public static boolean createTable(String query, int version) {
        try {
            if (Constantes.DBHelper == null) {
                Constantes.DBHelper = DatabaseHelper.getHelper(Constantes.CONTEXT, Constantes.DATABASENAME, query, version);
            }
            Constantes.DATABASE = Constantes.DBHelper.Open(query);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String queryCreateTable(Class classe) {
        String query = "";
        try {
            if (classe != null) {
                if (classe.isAnnotationPresent(Table.class)) {
                    boolean first = true;
                    String table = getTableName(classe);
                    query = "create table if not exists " + table + " (";
                    for (Field field : classe.getDeclaredFields()) {
                        if (field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(JoinColumn.class)) {
                            String column = getColumnName(field);
                            boolean foreign_key = false;
                            boolean auto_increment = false;
                            boolean primary_key = field.isAnnotationPresent(Id.class);
                            String table_join = "";
                            String onDeleteAction = Constantes.ACTION_NO_ACTION;
                            String onUpdateAction = Constantes.ACTION_CASCADE;
                            String column_join = "";
                            Object foreign_instance = null;
                            if (field.isAnnotationPresent(Column.class)) {
                                auto_increment = getAutoIncrement(field);
                            } else {
                                table_join = getTableName(field.getType());
                                try {
                                    if (asString(table_join)) {
                                        foreign_key = true;
                                        int index = tables.indexOf(new Tables(table_join));
                                        if (!existTable(table_join)) {
                                            if (index < 0) {
                                                tables.add(new Tables(table_join, foreign_instance));
                                                foreign_instance = field.getType().newInstance();
                                                index = tables.indexOf(new Tables(table_join));
                                                if (index > -1) {
                                                    tables.set(index, new Tables(table_join, foreign_instance));
                                                }
                                            } else {
                                                foreign_instance = tables.get(index).getInstance();
                                            }
                                        }
                                    }
                                } catch (Exception ex) {
                                    Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                if (!field.getType().isAnnotationPresent(Table.class)) {
                                    continue;
                                }
                            }
                            String type = field.getType().getSimpleName();
                            if (foreign_key) {
                                Field foreing_field = getId(field.getType());
                                type = foreing_field.getType().getSimpleName();
                                column_join = getColumnName(foreing_field);
                                if (Utils.asString(column_join) ? (Constantes.CONTEXT != null && !table.equals("metadata_constraint")) : false) {
                                    String contraintName = table + "_" + column + "_fkey";
                                    if (field.isAnnotationPresent(ManyToOne.class)) {
                                        ManyToOne annotation = field.getAnnotation(ManyToOne.class);
                                        onDeleteAction = annotation.delete().value();
                                        onUpdateAction = annotation.update().value();
                                    }
                                    MetadataConstraint contraint = new MetadataConstraint(column, table, type, contraintName, true, column_join, table_join, onDeleteAction, onUpdateAction).Open();
                                    if (contraint.one().getColumnName() == null) {
                                        contraint.save();
                                    }
                                }
                            } else if (primary_key) {
                                if (Constantes.CONTEXT != null && !table.equals("metadata_constraint")) {
                                    String contraintName = table + "_" + column + "_pkey";
                                    MetadataConstraint contraint = new MetadataConstraint(column, table, type, contraintName, true, auto_increment).Open();
                                    if (contraint.one().getColumnName() == null) {
                                        contraint.save();
                                    }
                                }
                            }
                            if (type.equals("String") || type.equals("Character") || type.equals("Date")) {
                                type = "Text";
                            }
                            query += (first ? "" : ", ") + column + " " + (primary_key ? "INTEGER" : type);
                            if (primary_key) {
                                query += " PRIMARY KEY" + (auto_increment ? " AUTOINCREMENT" : "");
                            } else if (foreign_key) {
                                query += " REFERENCES " + table_join + "(" + column_join + ") ON DELETE " + onDeleteAction + " ON UPDATE " + onUpdateAction;
                            }
                            first = false;
                        }
                    }
                    query += ")";
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return query;
    }

    public static boolean getAutoIncrement(Field field) {
        return field != null ? field.isAnnotationPresent(SequenceGenerator.class) : false;
    }

    public static String getSequenceName(Field field) {
        return field != null ? field.isAnnotationPresent(SequenceGenerator.class) ? field.getAnnotation(SequenceGenerator.class).name() : "" : "";
    }

    public static Field getId(Classe classe) {
        return getId(classe.getClass());
    }

    public static Field getId(Class classe) {
        for (Field field : classe.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        return null;
    }

    public static MetadataConstraint getPrimaryKey(Class classe) {
        if (classe.equals(MetadataConstraint.class)) {
            return null;
        }
        Field field = getId(classe);
        if (field != null) {
            String table = getTableName(classe);
            return getPrimaryKey(field, table);
        }
        return null;
    }

    public static MetadataConstraint getPrimaryKey(Field field, String table) {
        if (field != null && !table.equals("metadata_constraint")) {
            boolean autoIncrement = Utils.getAutoIncrement(field);
            String constraintName = table + "_" + field.getName() + "_pkey";
            return new MetadataConstraint(field.getName(), table, field.getType().getSimpleName(), constraintName, true, autoIncrement);
        }
        return null;
    }

    public static String getTableName(Entity classe) {
        return getTableName(classe.getClass());
    }

    public static String getTableName(Class classe) {
        if (classe.equals(MetadataConstraint.class)) {
            return "metadata_constraint";
        }
        return classe.isAnnotationPresent(Table.class) ? ((Table) classe.getAnnotation(Table.class)).name() : "";
    }

    public static String getTableLibelle(Entity classe) {
        return getTableLibelle(classe.getClass());
    }

    public static String getTableLibelle(Class classe) {
        if (classe.equals(MetadataConstraint.class)) {
            return "metadata_constraint";
        }
        String libelle = classe.isAnnotationPresent(Table.class) ? ((Table) classe.getAnnotation(Table.class)).libelle() : "";
        if (!Utils.asString(libelle)) {
            libelle = getTableName(classe);
        }
        return libelle;
    }

    public static String getTableGroupe(Class classe) {
        if (classe.equals(MetadataConstraint.class)) {
            return "metadata_constraint";
        }
        return classe.isAnnotationPresent(Table.class) ? ((Table) classe.getAnnotation(Table.class)).groupe() : "";
    }

    public static List<String> getTableDescriptor(Class classe) {
        List<String> descriptor = new ArrayList<>();
        if (classe.isAnnotationPresent(Descriptor.class)) {
            descriptor.addAll(Arrays.asList(((Descriptor) classe.getAnnotation(Descriptor.class)).value()));
        }
        return descriptor;
    }

    public static String getColumnName(Field field) {
        Annotation annotation = field.getAnnotation(Column.class);
        String column = null;
        if (annotation != null) {
            column = ((Column) annotation).name();
        } else {
            annotation = field.getAnnotation(JoinColumn.class);
            if (annotation != null) {
                column = ((JoinColumn) annotation).name();
            }
        }
        return column;
    }

    public static boolean isForce(Field field) {
        Annotation annotation = field.getAnnotation(ManyToOne.class);
        boolean force = false;
        if (annotation != null) {
            force = ((ManyToOne) annotation).force();
        }
        return force;
    }

    public static String getForeignTableName(Field field) {
        return getTableName(field.getType());
    }

    public static String getForeignColumnName(Field field) {
        Annotation annotation = field.getAnnotation(JoinColumn.class);
        String column = null;
        if (annotation != null) {
            column = ((JoinColumn) annotation).referencedColumnName();
        }
        return column;
    }

    public static String getForeignType(Field field) {
        if (field.getType().isAnnotationPresent(Table.class)) {
            Field fKey = getId(field.getType());
            if (fKey != null) {
                return fKey.getType().getSimpleName();
            }
        }
        return null;
    }

    public static String[] getColumns(List<Columns> columns) {
        String[] result = null;
        if (columns != null ? !columns.isEmpty() : false) {
            result = new String[columns.size()];
            for (int i = 0; i < columns.size(); i++) {
                result[i] = columns.get(i).getColumnName();
            }
        }
        return result;
    }

    public static String[] getColumnsWithAlias(List<Columns> columns, String alias) {
        String[] result = null;
        if (columns != null ? !columns.isEmpty() : false) {
            result = new String[columns.size()];
            for (int i = 0; i < columns.size(); i++) {
                result[i] = (Utils.asString(alias) ? alias + "." : "") + columns.get(i).getColumnName();
            }
        }
        return result;
    }

    public static List<Columns> getColumns(Entity classe) {
        return getColumns(classe.getClass());
    }

    public static List<Columns> getColumns(List<Columns> columns, String alias) {
        return getColumns(columns, alias, null);
    }

    public static List<Columns> getColumns(List<Columns> columns, String alias, Columns[] otherColumn) {
        try {
            List<Columns> result = new ArrayList<>();
            if (columns != null) {
                for (Columns column : columns) {
                    Columns instance = (Columns) column.clone();
                    if (asString(alias)) {
                        if (!instance.getColumnName().contains(alias + ".")) {
                            instance.setColumnName(alias + "." + instance.getColumnName());
                        }
                    }
                    result.add(instance);
                }
            }
            if (otherColumn != null) {
                for (Columns other : otherColumn) {
                    result.add(other);
                }
            }
            return result;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return columns;
    }

    public static List<Columns> getColumns(Class classe) {
        return getColumns(classe, null);
    }

    public static List<Columns> getColumns(Class classe, String alias) {
        String tableName = getTableName(classe);
        if (Constantes.PERSISTENCE != null) {
            if (Utils.asString(tableName)) {
                Tables table = Constantes.PERSISTENCE.getTable(tableName);
                if (table != null) {
                    if (!table.getColumns().isEmpty()) {
                        Columns column = table.getColumns().get(0);
                        if (Utils.asString(alias) ? !column.getColumnName().contains(alias + ".") : false) {
                            return getColumns(table.getColumns(), alias);
                        }
                    }
                    return table.getColumns();
                }
            }
        }
        List<Columns> columns = new ArrayList<>();
        for (Field field : classe.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(JoinColumn.class)) {
                String columnName = getColumnName(field);
                if (Utils.asString(alias) ? !columnName.contains(alias + ".") : false) {
                    columnName = alias + "." + columnName;
                }
                Columns column = new Columns(columnName, tableName);
                if (field.isAnnotationPresent(Id.class)) {
                    column.setPrimaryKey(true);
                    column.setContraintName(tableName + "_" + columnName + "_pkey");
                }
                if (field.isAnnotationPresent(JoinColumn.class)) {
                    column.setForeignKey(true);
                    column.setColumnNameFK(getForeignColumnName(field));
                    column.setTableNameFK(getForeignTableName(field));
                    column.setType(getForeignType(field));
                    column.setContraintName(tableName + "_" + columnName + "_fkey");
                } else {
                    column.setType(field.getType().getSimpleName());
                }
                columns.add(column);
            }
        }
        return columns;
    }

    public static ContentValues addParam(Object object) throws IllegalAccessException {
        ContentValues values = new ContentValues();
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(JoinColumn.class)) {
                field.setAccessible(true);
                Object value = field.get(object);
                String type = field.getType().getSimpleName();
                if (value != null) {
                    String column = getColumnName(field);
                    if (field.getType().isAnnotationPresent(Table.class)) {
                        Field foreing = getId(field.getType());
                        foreing.setAccessible(true);
                        value = foreing.get(value);
                        type = foreing.getType().getSimpleName();
                    }
                    switch (type) {
                        case "String":
                            values.put(column, (String) value);
                            break;
                        case "int":
                        case "Integer":
                            values.put(column, (Integer) value);
                            break;
                        case "long":
                        case "Long":
                            values.put(column, (Long) value);
                            break;
                        case "double":
                        case "Double":
                            values.put(column, (Double) value);
                            break;
                        case "boolean":
                        case "Boolean":
                            values.put(column, (Boolean) value);
                            break;
                        case "char":
                        case "Character":
                            Character caract = value.toString().charAt(0);
                            values.put(column, caract.toString());
                            break;
                        case "Date":
                            Annotation annotation = field.getAnnotation(Temporal.class);
                            if (annotation != null) {
                                TemporalType temporal = ((Temporal) annotation).value();
                                switch (temporal) {
                                    case TIMESTAMP:
                                        values.put(column, formatDateTime.format(value));
                                        break;
                                    case TIME:
                                        values.put(column, formatTime.format(value));
                                        break;
                                    default:
                                        values.put(column, formatDate.format(value));
                                        break;
                                }
                            } else {
                                values.put(column, formatDate.format(value));
                            }
                            break;
                    }
                }
            }
        }
        return values;
    }

    public static Field getField(String columnName, Class classe) {
        for (Field field : classe.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(JoinColumn.class)) {
                String column = getColumnName(field);
                if (column.equals(columnName)) {
                    return field;
                }
            }
        }
        return null;
    }

    public static Class getClass(String tableName) {
        try {
            if (Utils.asString(tableName) && com.lymytz.entitymanager.tools.Constantes.PERSISTENCE != null) {
                Tables table = com.lymytz.entitymanager.tools.Constantes.PERSISTENCE.getTable(tableName);
                if (table != null) {
                    return table.getClasse();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Field getFieldColumn(Class classe, String columnName) {
        try {
            if (classe != null) {
                for (Field field : classe.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(JoinColumn.class)) {
                        String column = com.lymytz.entitymanager.tools.Utils.getColumnName(field);
                        if (columnName.equals(column)) {
                            return field;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Object getEntity(Cursor cursor, List<Columns> columns, Class classe) {
        return getEntity(cursor, columns, classe, new Class[]{});
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Object getEntity(Cursor cursor, List<Columns> columns, Class classe, Class[] includes) {
        return getEntity(cursor, columns, classe, Arrays.asList(includes));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Object getEntity(Cursor cursor, List<Columns> columns, Class classe, List<Class> includes) {
        try {
            Object object = classe.newInstance();
            if (columns != null ? columns.isEmpty() : true) {
                columns = getColumns(classe);
            }
            for (int i = 0; i < columns.size(); i++) {
                Columns column = columns.get(i);
                Field field = getField(column.getColumnName(), classe);
                if (field != null) {
                    field.setAccessible(true);
                    if (field.getType().isAnnotationPresent(Table.class)) {
                        Field key = getId(field.getType());
                        if (key != null) {
                            Object value = value(cursor, key, i);
                            if (value != null) {
                                Object foreign = field.getType().newInstance();
                                boolean load = includes.contains(field.getType());
                                Annotation annotation = null;
                                if (field.isAnnotationPresent(ManyToOne.class)) {
                                    annotation = field.getAnnotation(ManyToOne.class);
                                } else if (field.isAnnotationPresent(OneToOne.class)) {
                                    annotation = field.getAnnotation(OneToOne.class);
                                }
                                if (annotation != null ? !load : false) {
                                    if (annotation instanceof ManyToOne) {
                                        ManyToOne manyToOne = (ManyToOne) annotation;
                                        load = manyToOne.fetch().equals(FetchType.EAGER);
                                    } else {
                                        OneToOne oneToOne = (OneToOne) annotation;
                                        load = oneToOne.fetch().equals(FetchType.EAGER);
                                    }
                                }
                                if (foreign instanceof Classe && load) {
                                    Classe entity = (Classe) foreign;
                                    entity = entity.Open().one(value);
                                    field.set(object, entity);
                                } else {
                                    boolean primitif = false;
                                    String alias = "";
                                    if (annotation != null) {
                                        if (annotation instanceof ManyToOne) {
                                            ManyToOne manyToOne = (ManyToOne) annotation;
                                            primitif = manyToOne.fetch().equals(FetchType.PRIMITIF);
                                            alias = manyToOne.alias();
                                        } else {
                                            OneToOne oneToOne = (OneToOne) annotation;
                                            primitif = oneToOne.fetch().equals(FetchType.PRIMITIF);
                                            alias = oneToOne.alias();
                                        }
                                    }
                                    if (primitif) {
                                        foreign = getEntity(cursor, field.getType(), alias);
                                        key.setAccessible(true);
                                        key.set(foreign, value);
                                        field.set(object, foreign);
                                    } else {
                                        key.setAccessible(true);
                                        key.set(foreign, value);
                                        field.set(object, foreign);
                                    }
                                }
                            }
                        }
                    } else {
                        field.set(object, value(cursor, field, i));
                    }
                }
            }
            if (object instanceof Classe) {
                Object id = ((Classe) object).getId();
                ((Classe) object).setIdDistant(id != null ? Long.valueOf(id.toString()) : null);
            }
            return object;
        } catch (IllegalAccessException | InstantiationException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Object getEntity(Cursor cursor, Class classe, String alias) {
        try {
            Object object = classe.newInstance();
            List<Columns> columns = getColumns(classe);
            for (int i = 0; i < columns.size(); i++) {
                Columns column = columns.get(i);
                int index = getColumnIndex(cursor, column.getColumnName());
                if (index > -1) {
                    Field field = getField(column.getColumnName(), classe);
                    if (field != null) {
                        field.setAccessible(true);
                        field.set(object, value(cursor, field, index));
                    }
                }
            }
            return object;
        } catch (IllegalAccessException | InstantiationException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static int getColumnIndex(Cursor cursor, String columnName) {
        try {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String column = cursor.getColumnName(i);
                if (columnName.equals(column)) {
                    return i;
                }
            }
        } catch (Exception ex) {
        }
        return -1;
    }

    public static Object value(Cursor cursor, Field field, int i) {
        try {
            if (field != null) {
                String type = field.getType().getSimpleName();
                Object value = value(cursor, type, i);
                if (type.equals("Date")) {
                    if (value == null) {
                        return null;
                    }
                    try {
                        if (Utils.asString(value.toString())) {
                            try {
                                return formatDateTime.parse(value.toString());
                            } catch (Exception ex_s) {
                                Log.e("Exception", "Ex (formatDateTime) : " + field.getName() + ", " + field.getType().getSimpleName() + " [" + value + "]");
                                try {
                                    return formatDate.parse(value.toString());
                                } catch (Exception ex_d) {
                                    Log.e("Exception", "Ex (formatDate) : " + field.getName() + ", " + field.getType().getSimpleName() + " [" + value + "]");
                                    try {
                                        return formatTime.parse(value.toString());
                                    } catch (Exception ex_t) {
                                        Log.e("Exception", "Ex (formatTime) : " + field.getName() + ", " + field.getType().getSimpleName() + " [" + value + "]");
                                        Messages.Exception(ex_t);
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Log.e("Exception", "Ex : " + field.getName() + ", " + field.getType().getSimpleName() + " [" + value + "]");
                        Messages.Exception(ex);
                    }
                    return null;
                }
                return value;
            }
        } catch (Exception ex) {
            Messages.Exception(ex);
        }
        return null;
    }

    public static Object value(Cursor cursor, String type, int i) {
        try {
            if (Utils.asString(type) ? cursor.getColumnCount() >= i ? !cursor.isNull(i) : false : false) {
                switch (type) {
                    case "String":
                    case "Date":
                        return cursor.getString(i);
                    case "int":
                    case "Integer":
                        return cursor.getInt(i);
                    case "long":
                    case "Long":
                        return cursor.getLong(i);
                    case "double":
                    case "Double":
                        return cursor.getDouble(i);
                    case "boolean":
                    case "Boolean":
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            return Objects.equals(cursor.getString(i), "1");
                        } else {
                            return cursor.getString(i) != null ? cursor.getString(i).equals("1") : false;
                        }
                    case "char":
                    case "Character":
                        String v = cursor.getString(i);
                        if (v != null) {
                            return v.charAt(0);
                        }
                        return null;
                    default:
                        return null;
                }
            }
        } catch (Exception ex) {
            Messages.Exception(ex);
        }
        return null;
    }

    public static int compare(MetadataConstraint pKey, Object currentVal, Object nextVal) {
        try {
            if (nextVal == null) {
                return 1;
            }
            if (currentVal == null) {
                return -11;
            }
            switch (pKey.getType()) {
                case "Long":
                    return Long.valueOf(currentVal.toString()).compareTo(Long.valueOf(nextVal.toString()));
                case "Integer":
                    return Integer.valueOf(currentVal.toString()).compareTo(Integer.valueOf(nextVal.toString()));
                default:
                    return (currentVal.toString()).compareTo(nextVal.toString());
            }
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public static Object value(MetadataConstraint key, Cursor cursor, int index) {
        if (cursor != null && index > -1) {
            switch (key.getType()) {
                case "Long":
                    return (Long) cursor.getLong(index);
                case "Integer":
                    return (Integer) cursor.getInt(index);
                default:
                    return cursor.getString(index);
            }
        } else {
            switch (key.getType()) {
                case "Long":
                    return (Long) 0L;
                case "Integer":
                    return 0;
                default:
                    return "";
            }
        }
    }

    public static File write(String fileDirectory, InputStream input) {
        if (input != null) {
            File file = new File(fileDirectory);
            if (file.exists())
                file.delete();
            try (FileOutputStream fos = new FileOutputStream(fileDirectory)) {
                byte[] buf = new byte[8192];
                int len;
                while ((len = input.read(buf)) >= 0) {
                    fos.write(buf, 0, len);
                }
                file = new File(fileDirectory);
                if (file.exists())
                    return file;

            } catch (Exception ex) {
                Messages.Exception(ex);
            }
        }
        return null;
    }
}
