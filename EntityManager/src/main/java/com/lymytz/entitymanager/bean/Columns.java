package com.lymytz.entitymanager.bean;

import android.annotation.TargetApi;
import android.os.Build;

import com.lymytz.entitymanager.annotations.Temporal;
import com.lymytz.entitymanager.enums.TemporalType;

import java.io.Serializable;
import java.util.Objects;

public class Columns implements Serializable, Cloneable {

    private String columnName;
    private String tableName;
    private String type;
    private boolean primaryKey;
    private boolean foreignKey;
    private String columnNameFK;
    private String tableNameFK;
    private String contraintName;
    private TemporalType temporal;
    private boolean autoIncrement;
    private Object value;
    private Object valueFK;
    private boolean external;
    private boolean force;

    public Columns(String columnName, String tableName) {
        this.columnName = columnName;
        this.tableName = tableName;
    }

    public Columns(String columnName, String tableName, String type) {
        this(columnName, tableName);
        this.type = type;
    }

    public Columns(String columnName, String tableName, String type, Object value) {
        this(columnName, tableName, type);
        this.value = value;
    }

    public Columns(String columnName, String tableName, String type, Object value, TemporalType temporal) {
        this(columnName, tableName, type, value);
        this.temporal = temporal;
    }

    public Columns(String columnName, String tableName, String type, boolean primaryKey) {
        this(columnName, tableName, type);
        this.primaryKey = primaryKey;
    }

    public Columns(String columnName, String tableName, boolean foreignKey, String columnNameFK, String tableNameFK) {
        this(columnName, tableName, "String", foreignKey, columnNameFK, tableNameFK);
    }

    public Columns(String columnName, String tableName, String columnNameFK, String tableNameFK, boolean external) {
        this(columnName, tableName);
        this.columnNameFK = columnNameFK;
        this.tableNameFK = tableNameFK;
        this.external = external;
    }

    public Columns(String columnName, String tableName, String type, boolean foreignKey, String columnNameFK, String tableNameFK) {
        this(columnName, tableName, type);
        this.foreignKey = foreignKey;
        this.columnNameFK = columnNameFK;
        this.tableNameFK = tableNameFK;
    }

    public Columns(String columnName, String tableName, String type, boolean foreignKey, String columnNameFK, String tableNameFK, boolean force) {
        this(columnName, tableName, type, foreignKey, columnNameFK, tableNameFK);
        this.force = force;
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

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
    }

    public String getColumnNameFK() {
        return columnNameFK;
    }

    public void setColumnNameFK(String columnNameFK) {
        this.columnNameFK = columnNameFK;
    }

    public String getTableNameFK() {
        return tableNameFK;
    }

    public void setTableNameFK(String tableNameFK) {
        this.tableNameFK = tableNameFK;
    }

    public String getContraintName() {
        return contraintName;
    }

    public void setContraintName(String contraintName) {
        this.contraintName = contraintName;
    }

    public TemporalType getTemporal() {
        return temporal;
    }

    public void setTemporal(TemporalType temporal) {
        this.temporal = temporal;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }

    public Object getValueFK() {
        return valueFK;
    }

    public void setValueFK(Object valueFK) {
        this.valueFK = valueFK;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Columns columns = (Columns) o;
        return Objects.equals(columnName, columns.columnName) &&
                Objects.equals(tableName, columns.tableName);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(columnName, tableName);
    }

    @Override
    public String toString() {
        return "Columns{" +
                "columnName='" + columnName + '\'' +
                ", tableName='" + tableName + '\'' +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
