package com.lymytz.entitymanager.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rows implements Serializable, Cloneable {
    private List<Columns> columns;

    public Rows() {
        this.columns = new ArrayList<>();
    }

    public Rows(List<Columns> columns) {
        this();
        this.columns = columns;
    }

    public List<Columns> getColumns() {
        return columns;
    }

    public void setColumns(List<Columns> columns) {
        this.columns = columns;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
