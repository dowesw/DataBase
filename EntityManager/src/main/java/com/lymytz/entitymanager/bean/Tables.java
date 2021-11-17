package com.lymytz.entitymanager.bean;

import android.content.Context;
import android.os.Build;

import com.lymytz.entitymanager.db.MetadataConstraint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.RequiresApi;

public class Tables implements Serializable, Cloneable {

    private String name;
    private String libelle;
    private String groupe;
    private String classeName;
    private Class classe;
    private String queryCreate;
    private String sequence;
    private List<Columns> columns;
    private Object instance;
    private Context context;
    private MetadataConstraint id;
    private List<String> descriptors;

    private List<Rows> values;

    public Tables(String name) {
        this.name = name;
        columns = new ArrayList<>();
        values = new ArrayList<>();
        descriptors = new ArrayList<>();
    }

    public Tables(String name, String classeName) {
        this(name);
        this.classeName = classeName;
    }

    public Tables(String name, Object instance) {
        this(name);
        this.instance = instance;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public String getClasseName() {
        return classeName;
    }

    public void setClasseName(String classeName) {
        this.classeName = classeName;
    }

    public Class getClasse() {
        return classe;
    }

    public void setClasse(Class classe) {
        this.classe = classe;
    }

    public String getQueryCreate() {
        return queryCreate;
    }

    public void setQueryCreate(String queryCreate) {
        this.queryCreate = queryCreate;
    }

    public List<Columns> getColumns() {
        return columns;
    }

    public void setColumns(List<Columns> columns) {
        this.columns = columns;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public MetadataConstraint getId() {
        return id;
    }

    public void setId(MetadataConstraint id) {
        this.id = id;
    }

    public List<Rows> getValues() {
        return values;
    }

    public void setValues(List<Rows> values) {
        this.values = values;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public List<String> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List<String> descriptors) {
        this.descriptors = descriptors;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tables tables = (Tables) o;
        return Objects.equals(name, tables.name);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Tables{name='" + name + "'}";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
