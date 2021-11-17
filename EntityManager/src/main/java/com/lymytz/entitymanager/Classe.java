package com.lymytz.entitymanager;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

public abstract class Classe implements Serializable {


    protected Context context;

    protected String TABLE_NAME;

    protected String QUERY;
    protected String[] COLUMNS;

    public abstract boolean save();

    public abstract boolean update();

    public abstract boolean delete();

    public abstract Classe one(Object id);



}
