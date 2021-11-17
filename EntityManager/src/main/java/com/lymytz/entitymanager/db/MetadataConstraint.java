package com.lymytz.entitymanager.db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.lymytz.entitymanager.annotations.Transient;
import com.lymytz.entitymanager.tools.Classe;
import com.lymytz.entitymanager.annotations.Column;
import com.lymytz.entitymanager.annotations.Entity;
import com.lymytz.entitymanager.annotations.Table;
import com.lymytz.entitymanager.tools.Constantes;
import com.lymytz.entitymanager.tools.IClasse;
import com.lymytz.entitymanager.tools.Messages;
import com.lymytz.entitymanager.tools.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "metadata_constraint")
public class MetadataConstraint extends Classe implements IClasse, Serializable {
    @Column(name = "column_name")
    private String columnName;
    @Column(name = "table_name")
    private String tableName;
    @Column(name = "type")
    private String type;

    @Column(name = "primary_key")
    private Boolean primaryKey;
    @Column(name = "auto_increment")
    private Boolean autoIncrement;

    @Column(name = "foreign_key")
    private Boolean foreignKey;
    @Column(name = "contraint_name")
    private String contraintName;
    @Column(name = "column_fkey")
    private String columnFkey;
    @Column(name = "table_fkey")
    private String tableFkey;
    @Column(name = "on_delete_action")
    private String onDeleteAction = Constantes.ACTION_NO_ACTION;
    @Column(name = "on_update_action")
    private String onUpdateAction = Constantes.ACTION_CASCADE;
    @Transient
    private Object value;

    public MetadataConstraint() {

    }

    public MetadataConstraint(String column) {
        this.columnName = column;
    }

    public MetadataConstraint(String table, String column) {
        this(column);
        this.tableName = table;
    }

    public MetadataConstraint(String columnName, String tableName, String type) {
        this(tableName, columnName);
        this.type = type;
    }

    public MetadataConstraint(String columnName, String tableName, String type, String contraintName) {
        this(columnName, tableName, type);
        this.contraintName = contraintName;
    }

    public MetadataConstraint(String columnName, String tableName, String type, String contraintName, Boolean primaryKey, Boolean autoIncrement) {
        this(columnName, tableName, type, contraintName);
        this.primaryKey = primaryKey;
        this.autoIncrement = autoIncrement;
    }

    public MetadataConstraint(String columnName, String tableName, String type, String contraintName, Boolean foreignKey, String columnFkey, String tableFkey) {
        this(columnName, tableName, type, contraintName);
        this.foreignKey = foreignKey;
        this.columnFkey = columnFkey;
        this.tableFkey = tableFkey;
        this.onDeleteAction = Constantes.ACTION_NO_ACTION;
        this.onUpdateAction = Constantes.ACTION_CASCADE;
    }

    public MetadataConstraint(String columnName, String tableName, String type, String contraintName, Boolean foreignKey, String columnFkey, String tableFkey, String onDeleteAction, String onUpdateAction) {
        this(columnName, tableName, type, contraintName, foreignKey, columnFkey, tableFkey);
        this.onDeleteAction = onDeleteAction;
        this.onUpdateAction = onUpdateAction;
    }

    @Override
    public Object getId() {
        return null;
    }

    @Override
    public void setId(Long id_) {

    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getPrimaryKey() {
        return primaryKey != null ? primaryKey : false;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Boolean getForeignKey() {
        return foreignKey != null ? foreignKey : false;
    }

    public void setForeignKey(Boolean foreignKey) {
        this.foreignKey = foreignKey;
    }

    public String getContraintName() {
        return contraintName;
    }

    public void setContraintName(String contraintName) {
        this.contraintName = contraintName;
    }

    public String getColumnFkey() {
        return columnFkey;
    }

    public void setColumnFkey(String columnFkey) {
        this.columnFkey = columnFkey;
    }

    public String getTableFkey() {
        return tableFkey;
    }

    public void setTableFkey(String tableFkey) {
        this.tableFkey = tableFkey;
    }

    public Boolean getAutoIncrement() {
        return autoIncrement != null ? autoIncrement : false;
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public String getOnDeleteAction() {
        return onDeleteAction;
    }

    public void setOnDeleteAction(String onDeleteAction) {
        this.onDeleteAction = onDeleteAction;
    }

    public String getOnUpdateAction() {
        return onUpdateAction;
    }

    public void setOnUpdateAction(String onUpdateAction) {
        this.onUpdateAction = onUpdateAction;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetadataConstraint that = (MetadataConstraint) o;
        return Objects.equals(columnName, that.columnName) && Objects.equals(tableName, that.tableName);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(columnName, tableName);
    }

    @Override
    public MetadataConstraint Open() {
        return (MetadataConstraint) super.Open();
    }

    @Override
    public boolean save() {
        try {
            ContentValues value = Utils.addParam(this);
            return Utils.getDatabase().insert(TABLE_NAME, null, value) > 0;
        } catch (Exception ex) {
            Messages.Exception(getClass().getSimpleName() + " (save)", ex);
        }
        return false;
    }

    @Override
    public boolean update() {
        try {
            ContentValues value = Utils.addParam(this);
            return Utils.getDatabase().update(TABLE_NAME, value, "column_name = ? AND table_name = ?", new String[]{columnName, tableName}) > 0;
        } catch (Exception ex) {
            Messages.Exception(getClass().getSimpleName() + " (update)", ex);
        }
        return false;
    }

    @Override
    public boolean delete() {
        try {
            return Utils.getDatabase().delete(TABLE_NAME, "column_name = ? AND table_name = ?", new String[]{columnName, tableName}) > 0;
        } catch (Exception ex) {
            Messages.Exception(getClass().getSimpleName() + " (delete)", ex);
        }
        return false;
    }

    public boolean delete(String tablename) {
        try {
            Utils.getDatabase().delete(TABLE_NAME, "table_name = ?", new String[]{tablename});
            return true;
        } catch (Exception ex) {
            Messages.Exception(getClass().getSimpleName() + " (delete)", ex);
        }
        return false;
    }

    public boolean canDelete(String tablename, Object value) {
        try {
            MetadataConstraint fKey;
            try (Cursor c = Utils.getDatabase().query(TABLE_NAME, Utils.getColumns(COLUMNS), "table_fkey = ? AND foreign_key = 1 AND on_delete_action = ?", new String[]{tablename, Constantes.ACTION_NO_ACTION}, null, null, null)) {
                while (c.moveToNext()) {
                    fKey = (MetadataConstraint) Utils.getEntity(c, this.COLUMNS, this.getClass());
                    if (fKey != null ? Utils.asString(fKey.getColumnName()) : false) {
                        Long count = verifyContraint(fKey.getTableName(), fKey.getColumnName(), value);
                        if (count != null ? count > 0 : false) {
                            return false;
                        }
                    }
                }
            } catch (Exception ex) {
                Messages.Exception(getClass().getSimpleName() + " (canDelete)", ex);
            }
        } catch (Exception ex) {
            Messages.Exception(getClass().getSimpleName() + " (canDelete)", ex);
        }
        return true;
    }

    public MetadataConstraint one() {
        return one(columnName, tableName);
    }

    public MetadataConstraint one(String column, String tablename) {
        MetadataConstraint s = new MetadataConstraint();
        try {
            try (Cursor c = Utils.getDatabase().query(TABLE_NAME, Utils.getColumns(COLUMNS), "column_name = ? AND table_name = ?", new String[]{column, tablename}, null, null, null)) {
                while (c.moveToNext()) {
                    s = (MetadataConstraint) Utils.getEntity(c, this.COLUMNS, this.getClass());
                }
            } catch (Exception ex) {
                Messages.Exception(getClass().getSimpleName() + " (one)", ex);
            }
        } catch (Exception ex) {
            Messages.Exception(getClass().getSimpleName() + " (one)", ex);
        }
        return s;
    }

    @Override
    public MetadataConstraint one(Object id) {
        MetadataConstraint s = new MetadataConstraint();
        try {
            try (Cursor c = Utils.getDatabase().query(TABLE_NAME, Utils.getColumns(COLUMNS), "id = ?", new String[]{"" + id}, null, null, null)) {
                while (c.moveToNext()) {
                    s = (MetadataConstraint) Utils.getEntity(c, this.COLUMNS, this.getClass());
                }
            } catch (Exception ex) {
                Messages.Exception(getClass().getSimpleName() + " (one)", ex);
            }
        } catch (Exception ex) {
            Messages.Exception(getClass().getSimpleName() + " (one)", ex);
        }
        return s;
    }

    public List<MetadataConstraint> list() {
        List<MetadataConstraint> result = new ArrayList<>();
        try {
            try (Cursor c = Utils.getDatabase().query(TABLE_NAME, Utils.getColumns(COLUMNS), null, null, null, null, null)) {
                while (c.moveToNext()) {
                    result.add((MetadataConstraint) Utils.getEntity(c, this.COLUMNS, this.getClass()));
                }
            } catch (Exception ex) {
                Messages.Exception(getClass().getSimpleName() + " (list)", ex);
            }
        } catch (Exception ex) {
            Messages.Exception(getClass().getSimpleName() + " (list)", ex);
        }
        return result;
    }

    public List<MetadataConstraint> foreingKeys() {
        List<MetadataConstraint> result = new ArrayList<>();
        try {
            try (Cursor c = Utils.getDatabase().query(TABLE_NAME, Utils.getColumns(COLUMNS), " foreign_key = 1", null, null, null, null)) {
                while (c.moveToNext()) {
                    result.add((MetadataConstraint) Utils.getEntity(c, this.COLUMNS, this.getClass()));
                }
            } catch (Exception ex) {
                Messages.Exception(getClass().getSimpleName() + " (foreingKeys)", ex);
            }
        } catch (Exception ex) {
            Messages.Exception(getClass().getSimpleName() + " (foreingKeys)", ex);
        }
        return result;
    }

    public MetadataConstraint primaryKey(String tablename) {
        MetadataConstraint result = new MetadataConstraint();
        try {
            try (Cursor c = Utils.getDatabase().query(TABLE_NAME, Utils.getColumns(COLUMNS), "table_name = ? AND primary_key = 1", new String[]{tablename}, null, null, null, "1")) {
                while (c.moveToNext()) {
                    result = (MetadataConstraint) Utils.getEntity(c, this.COLUMNS, this.getClass());
                }
            } catch (Exception ex) {
                Messages.Exception(getClass().getSimpleName() + " (primaryKey)", ex);
            }
        } catch (Exception ex) {
            Messages.Exception(getClass().getSimpleName() + " (primaryKey)", ex);
        }
        return result;
    }

    public List<MetadataConstraint> foreingKey(String tablename) {
        List<MetadataConstraint> result = new ArrayList<>();
        try {
            try (Cursor c = Utils.getDatabase().query(TABLE_NAME, Utils.getColumns(COLUMNS), "table_fkey = ? AND foreign_key = 1", new String[]{tablename}, null, null, null)) {
                while (c.moveToNext()) {
                    result.add((MetadataConstraint) Utils.getEntity(c, this.COLUMNS, this.getClass()));
                }
            } catch (Exception ex) {
                Messages.Exception(getClass().getSimpleName() + " (foreingKey)", ex);
            }
        } catch (Exception ex) {
            Messages.Exception(getClass().getSimpleName() + " (foreingKey)", ex);
        }
        return result;
    }

    public List<MetadataConstraint> foreingKeyLieCanDelete(String tablename) {
        List<MetadataConstraint> result = new ArrayList<>();
        try {
            try (Cursor c = Utils.getDatabase().query(TABLE_NAME, Utils.getColumns(COLUMNS), "table_fkey = ? AND foreign_key = 1 AND on_delete_action = ?", new String[]{tablename, Constantes.ACTION_CASCADE}, null, null, null)) {
                while (c.moveToNext()) {
                    result.add((MetadataConstraint) Utils.getEntity(c, this.COLUMNS, this.getClass()));
                }
            } catch (Exception ex) {
                Messages.Exception(getClass().getSimpleName() + " (foreingKeyLie)", ex);
            }
        } catch (Exception ex) {
            Messages.Exception(getClass().getSimpleName() + " (foreingKeyLie)", ex);
        }
        return result;
    }

    public List<MetadataConstraint> foreingKeyLieCanNotDelete(String tablename) {
        List<MetadataConstraint> result = new ArrayList<>();
        try {
            try (Cursor c = Utils.getDatabase().query(TABLE_NAME, Utils.getColumns(COLUMNS), "table_fkey = ? AND foreign_key = 1 AND on_delete_action = ?", new String[]{tablename, Constantes.ACTION_NO_ACTION}, null, null, null)) {
                while (c.moveToNext()) {
                    result.add((MetadataConstraint) Utils.getEntity(c, this.COLUMNS, this.getClass()));
                }
            } catch (Exception ex) {
                Messages.Exception(getClass().getSimpleName() + " (foreingKeyLie)", ex);
            }
        } catch (Exception ex) {
            Messages.Exception(getClass().getSimpleName() + " (foreingKeyLie)", ex);
        }
        return result;
    }

    public Long verifyContraint(String tablename, String column, Object value) {
        Long result = 0L;
        try {
            try (Cursor c = Utils.getDatabase().query(tablename, new String[]{"COUNT(*)"}, (column + " = ? "), new String[]{value.toString()}, null, null, null)) {
                while (c.moveToNext()) {
                    result = c.getLong(0);
                }
            } catch (Exception ex) {
                Messages.Exception(getClass().getSimpleName() + " (verifyContraint)", ex);
            }
        } catch (Exception ex) {
            Messages.Exception(getClass().getSimpleName() + " (verifyContraint)", ex);
        }
        return result;
    }

}
