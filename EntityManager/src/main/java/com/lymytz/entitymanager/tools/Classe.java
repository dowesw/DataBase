package com.lymytz.entitymanager.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.util.Log;

import com.lymytz.entitymanager.Persistence;
import com.lymytz.entitymanager.annotations.Transient;
import com.lymytz.entitymanager.annotations.XmlTransient;
import com.lymytz.entitymanager.bean.Columns;
import com.lymytz.entitymanager.bean.Synchronise;
import com.lymytz.entitymanager.db.MetadataConstraint;
import com.lymytz.entitymanager.db.ResultatAction;
import com.lymytz.entitymanager.bean.Tables;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Classe implements Serializable, Cloneable {

    private String adresseServeur;
    private boolean reload;

    private Long idDistant;

    @Transient
    protected long countExport = 0;

    @Transient
    protected boolean synchronised = true;

    @Transient
    protected boolean clearForeigns = true;

    @Transient
    protected boolean removeIsLost = false;

    @Transient
    protected boolean updateFormDistant = false;

    @Transient
    protected transient Field KEY;

    @Transient
    protected transient MetadataConstraint ID;

    @Transient
    protected Context CONTEXT;

    @Transient
    protected String TABLE_NAME;

    @Transient
    protected String QUERY;

    @Transient
    protected String SEQUENCE;

    @Transient
    public List<Columns> COLUMNS;

    @Transient
    public List<String> DESCRIPTORS;

    @Transient
    protected ResultatAction resultat = new ResultatAction();

    @Transient
    protected Synchronise distant;

    @XmlTransient
    public Context getCONTEXT() {
        return CONTEXT;
    }

    @XmlTransient
    public String getTABLE_NAME() {
        return TABLE_NAME;
    }

    @XmlTransient
    public String getSEQUENCE() {
        return SEQUENCE;
    }

    @XmlTransient
    public String getQUERY() {
        return QUERY;
    }

    @XmlTransient
    public List<Columns> getCOLUMNS() {
        return COLUMNS;
    }

    @XmlTransient
    public ResultatAction getResultat() {
        return resultat;
    }

    public void setResultat(ResultatAction resultat) {
        this.resultat = resultat;
    }

    public String getAdresseServeur() {
        return adresseServeur;
    }

    public void setAdresseServeur(String adresseServeur) {
        this.adresseServeur = adresseServeur;
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    public Synchronise getDistant() {
        return distant;
    }

    public void setDistant(Synchronise distant) {
        this.distant = distant;
    }

    public Long getIdDistant() {
        return idDistant != null ? idDistant : 0;
    }

    public void setIdDistant(Long idDistant) {
        this.idDistant = idDistant;
    }

    public long getCountExport() {
        return countExport;
    }

    public void setCountExport(long countExport) {
        this.countExport = countExport;
    }

    public abstract Object getId();

    public abstract void setId(Long id);

    public boolean isRemoveIsLost() {
        return removeIsLost;
    }

    public void setRemoveIsLost(boolean removeIsLost) {
        this.removeIsLost = removeIsLost;
    }

    public boolean isUpdateFormDistant() {
        return updateFormDistant;
    }

    public void setUpdateFormDistant(boolean updateFormDistant) {
        this.updateFormDistant = updateFormDistant;
    }

    public Classe() {
        if (!Utils.asString(TABLE_NAME)) {
            TABLE_NAME = getClass().equals(MetadataConstraint.class) ? "metadata_constraint" : Utils.getTableName(getClass());
            KEY = Utils.getId(getClass());
            ID = Utils.getPrimaryKey(KEY, TABLE_NAME);
            SEQUENCE = Utils.getSequenceName(KEY);
        }
    }

    public Classe Open() {
        return Open(true);
    }

    public Classe Open(boolean load) {
        try {
            if (Constantes.PERSISTENCE != null) {
                if (!Utils.asString(TABLE_NAME)) {
                    TABLE_NAME = Utils.getTableName(getClass());
                }
                if (Utils.asString(TABLE_NAME) && load) {
                    Tables table = Constantes.PERSISTENCE.getTable(TABLE_NAME);
                    if (table != null) {
                        QUERY = table.getQueryCreate();
                        COLUMNS = table.getColumns();
                        DESCRIPTORS = table.getDescriptors();
                        CONTEXT = table.getContext();
                        SEQUENCE = table.getSequence();
                        ID = table.getId();
                    } else {
                        QUERY = Utils.queryCreateTable(getClass());
                        if (Utils.asString(QUERY)) {
                            COLUMNS = Utils.getColumns(getClass());
                            ID = Utils.getPrimaryKey(getClass());
                            DESCRIPTORS = Utils.getTableDescriptor(getClass());
                            CONTEXT = Constantes.CONTEXT;
                            SEQUENCE = Utils.getSequenceName(KEY);
                            String libelle = Utils.getTableLibelle(getClass());
                            String groupe = Utils.getTableGroupe(getClass());

                            table = new Tables(TABLE_NAME);
                            table.setClasse(getClass());
                            table.setQueryCreate(QUERY);
                            table.setColumns(COLUMNS);
                            table.setContext(CONTEXT);
                            table.setDescriptors(DESCRIPTORS);
                            table.setSequence(SEQUENCE);
                            table.setId(ID);
                            table.setLibelle(libelle);
                            table.setGroupe(groupe);

                            if (!Utils.existTable(TABLE_NAME)) {
                                boolean create = Utils.createTable(table.getQueryCreate(), Constantes.VERSION);
                                if (!create) {
                                    return this;
                                }
                            }
                            Constantes.PERSISTENCE.add(table);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Messages.Exception(this.getClass().getSimpleName() + " (Open) ", ex);
        }
        return this;
    }

    public boolean truncate() {
        try {
            boolean truncate = true;
            if (Constantes.CONSTRAINT != null) {
                truncate = Constantes.CONSTRAINT.delete(TABLE_NAME);
            }
            if (truncate) {
                List<MetadataConstraint> contraints = new MetadataConstraint().Open().foreingKey(TABLE_NAME);
                for (MetadataConstraint constraint : contraints) {
                    try {
                        if (com.lymytz.entitymanager.tools.Constantes.PERSISTENCE != null) {
                            Tables table = com.lymytz.entitymanager.tools.Constantes.PERSISTENCE.getTable(constraint.getTableName());
                            if (table != null) {
                                if (table.getInstance() != null ? table.getInstance() instanceof Classe : false) {
                                    if (((Classe) table.getInstance()).truncate())
                                        continue;
                                } else if (table.getClasse() != null) {
                                    Object instance = table.getClasse().newInstance();
                                    if (instance != null ? instance instanceof Classe : false) {
                                        if (((Classe) instance).Open().truncate())
                                            continue;
                                    }
                                }
                            }
                        }

                        Persistence.execSQL("DELETE FROM " + constraint.getTableName());
                        Persistence.execSQL("DELETE FROM yvs_synchro_data_synchro WHERE id_listen IN (SELECT id FROM yvs_synchro_listen_table WHERE name_table = '" + constraint.getTableName() + "')");
                        Persistence.execSQL("DELETE FROM yvs_synchro_listen_table WHERE name_table = '" + constraint.getTableName() + "'");
                    } catch (Exception ex) {
                        Messages.Exception(this.getClass().getSimpleName() + " (Truncate) " + constraint.getTableName(), ex);
                    }
                }
                Persistence.execSQL("DELETE FROM " + TABLE_NAME);
                Persistence.execSQL("DELETE FROM yvs_synchro_data_synchro WHERE id_listen IN (SELECT id FROM yvs_synchro_listen_table WHERE name_table = '" + TABLE_NAME + "')");
                Persistence.execSQL("DELETE FROM yvs_synchro_listen_table WHERE name_table = '" + TABLE_NAME + "'");
            }
            return truncate;
        } catch (Exception ex) {
            Messages.Exception(this.getClass().getSimpleName() + " (Truncate) " + TABLE_NAME, ex);
        }
        return false;
    }

    public boolean importer(Synchronise.Server serveur) {
        return importer(serveur, false);
    }

    public boolean importer(Synchronise.Server serveur, boolean removeIsLost) {
        setDistant(new Synchronise(getId(), serveur));
        setRemoveIsLost(removeIsLost);
        setId(null);
        return save();
    }

    public boolean save(boolean synchronised) {
        this.synchronised = synchronised;
        boolean succes = save();
        this.synchronised = true;
        return succes;
    }

    public boolean update(boolean synchronised) {
        this.synchronised = synchronised;
        boolean succes = update();
        this.synchronised = true;
        return succes;
    }

    public boolean delete(boolean synchronised) {
        return delete(synchronised, false);
    }

    public boolean delete(boolean synchronised, boolean clearForeigns) {
        this.synchronised = synchronised;
        this.clearForeigns = clearForeigns;
        boolean succes = delete();
        this.synchronised = true;
        this.clearForeigns = false;
        return succes;
    }

    public boolean save() {
        ContentValues values = null;
        try {
            try {
                values = Utils.addParam(this);
            } catch (IllegalAccessException ex) {
                Messages.Exception(getClass().getSimpleName() + " (save) ", ex);
            }
            if (values == null) {
                return false;
            }
            long id = Persistence.insert(TABLE_NAME, null, values);
            setId(id);
            if (id > 0) {
                if (synchronised || distant != null) {
                    if (getId() != null) {
                        new ResultatAction().Open().delete(TABLE_NAME, Long.valueOf(getId().toString()));
                    }
                    if (!insert_synchro(Constantes.ACTION_INSERT, values.get("author")) && removeIsLost) {
                        if (getId() != null) {
                            Persistence.delete(TABLE_NAME, "id = ?", new String[]{getId() + ""});
                        }
                    }
                }
            }
            return id > 0;
        } catch (Exception ex) {
            Messages.Exception(" values " + values, ex);
        }
        return false;
    }

    public boolean update() {
        ContentValues values = null;
        try {
            if (getId() != null) {
                values = Utils.addParam(this);
                try {
                    values = Utils.addParam(this);
                } catch (IllegalAccessException ex) {
                    Messages.Exception(getClass().getSimpleName() + " (save) ", ex);
                }
                if (values == null) {
                    return false;
                }
                boolean succes = Persistence.update(TABLE_NAME, values, "id = ?", new String[]{"" + getId()});
                if (succes) {
                    if (synchronised) {
                        if (getId() != null) {
                            new ResultatAction().Open().delete(TABLE_NAME, Long.valueOf(getId().toString()));
                        }
                        insert_synchro(Constantes.ACTION_UPDATE, values.get("author"));
                    }
                }
                return succes;
            }
        } catch (Exception ex) {
            Messages.Exception(" values " + values, ex);
        }
        return false;
    }

    public boolean delete() {
        return delete(getId());
    }

    public boolean delete(Object id) {
        return delete(id, synchronised, clearForeigns);
    }

    public boolean delete(Object id, boolean synchronised, boolean clearForeigns) {
        try {
            if (id != null) {
                if (clearForeigns) {
                    List<MetadataConstraint> contraints = new MetadataConstraint().Open().foreingKeyLieCanNotDelete(TABLE_NAME);
                    Tables table = null;
                    for (MetadataConstraint constraint : contraints) {
                        try (Cursor cursor = Persistence.query(constraint.getTableName(), new String[]{"id"}, constraint.getColumnName() + " = ?", new String[]{id + ""})) {
                            while (cursor.moveToNext()) {
                                Long foreign = cursor.getLong(0);
                                if (foreign < 1) {
                                    continue;
                                }
                                if (Constantes.PERSISTENCE != null) {
                                    table = Constantes.PERSISTENCE.getTable(constraint.getTableName());
                                }
                                if (table == null) {
                                    continue;
                                }
                                if (table.getInstance() != null ? table.getInstance() instanceof Classe : false) {
                                    ((Classe) table.getInstance()).Open().delete(foreign, synchronised, clearForeigns);
                                } else {
                                    Object instance = table.getClasse().newInstance();
                                    if (instance != null ? instance instanceof Classe : false) {
                                        ((Classe) instance).Open().delete(foreign, synchronised, clearForeigns);
                                    }
                                }
                            }
                        }
                    }
                }
                List<MetadataConstraint> contraints = new ArrayList<>();
                if (!synchronised) {
                    contraints = new MetadataConstraint().Open().foreingKeyLieCanDelete(TABLE_NAME);
                    for (MetadataConstraint constraint : contraints) {
                        try (Cursor cursor = Persistence.query(constraint.getTableName(), new String[]{"id"}, constraint.getColumnName() + " = ?", new String[]{id + ""})) {
                            while (cursor.moveToNext()) {
                                Long foreign = cursor.getLong(0);
                                if (foreign < 1) {
                                    continue;
                                }
                                constraint.setValue(foreign);
                            }
                        }
                    }
                }
                boolean succes = Persistence.delete(TABLE_NAME, "id = ?", new String[]{id + ""});
                if (succes) {
                    if (id != null) {
                        new ResultatAction().Open().delete(TABLE_NAME, Long.valueOf(id.toString()));
                    }
                    if (synchronised) {
                        ContentValues values = Utils.addParam(this);
                        insert_synchro(Constantes.ACTION_DELETE, values.get("author"));
                    } else {
                        Persistence.execSQL("DELETE FROM yvs_synchro_data_synchro WHERE id_listen IN (SELECT id FROM yvs_synchro_listen_table WHERE name_table = '" + TABLE_NAME + "' AND id_source = " + id + ")");
                        Persistence.execSQL("DELETE FROM yvs_synchro_listen_table WHERE name_table = '" + TABLE_NAME + "' AND id_source = " + id + "");
                        for (MetadataConstraint constraint : contraints) {
                            if (constraint.getValue() != null ? Utils.asNumeric(constraint.getValue().toString()) : false) {
                                continue;
                            }
                            Persistence.execSQL("DELETE FROM yvs_synchro_data_synchro WHERE id_listen IN (SELECT id FROM yvs_synchro_listen_table WHERE name_table = '" + constraint.getTableName() + "' AND id_source = " + constraint.getValue() + ")");
                            Persistence.execSQL("DELETE FROM yvs_synchro_listen_table WHERE name_table = '" + constraint.getTableName() + "' AND id_source = " + constraint.getValue() + "");
                        }
                    }
                }
                return succes;
            }
        } catch (Exception ex) {
            Messages.Exception(getClass().getSimpleName() + " (delete) ", ex);
        }
        return false;
    }

    public long countLocal() {
        return 0;
    }

    public long countDistant() {
        return 0;
    }

    public Classe one(Object id) {
        return one(" id = ?", new String[]{id != null ? id.toString() : "0"});
    }

    public Classe one(String whereClause, String[] whereArgs, String orderBy) {
        return one(true, TABLE_NAME, Utils.getColumns(COLUMNS), whereClause, whereArgs, null, null, orderBy);
    }

    public Classe one(String table, String[] columns, String whereClause, String[] whereArgs) {
        return one(true, table, columns, whereClause, whereArgs, null, null, null);
    }

    public Classe one(String table, String[] columns, String whereClause, String[] whereArgs, String orderBy) {
        return one(true, table, columns, whereClause, whereArgs, null, null, orderBy);
    }

    public Classe one(String whereClause, String[] whereArgs) {
        return one(true, TABLE_NAME, Utils.getColumns(COLUMNS), whereClause, whereArgs, null, null, null);
    }

    public Classe one(boolean distint, String table, String[] columns, String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {
        try {
            try (Cursor c = Utils.getDatabase().query(distint, table, columns, whereClause, whereArgs, groupBy, having, orderBy, "1")) {
                while (c.moveToNext()) {
                    return (Classe) Utils.getEntity(c, this.COLUMNS, this.getClass());
                }
            } catch (Exception ex) {
                Messages.Exception(this.getClass().getSimpleName() + " (one) ", ex);
            }
        } catch (Exception ex) {
            Messages.Exception(this.getClass().getSimpleName() + " (one) ", ex);
        }
        return null;
    }

    public List<Classe> list(String orderBy) {
        return list(TABLE_NAME, Utils.getColumns(COLUMNS), null, null, null, null, orderBy);
    }

    public List<Classe> list(String whereClause, String[] whereArgs) {
        return list(TABLE_NAME, Utils.getColumns(COLUMNS), whereClause, whereArgs, null, null, null);
    }

    public List<Classe> list(String whereClause, String[] whereArgs, String orderBy) {
        return list(TABLE_NAME, Utils.getColumns(COLUMNS), whereClause, whereArgs, null, null, orderBy);
    }

    public List<Classe> list(String whereClause, String[] whereArgs, String orderBy, String limit) {
        return list(TABLE_NAME, Utils.getColumns(COLUMNS), whereClause, whereArgs, null, null, orderBy, limit);
    }

    public List<Classe> list(String table, String[] columns, String whereClause, String[] whereArgs) {
        return list(table, columns, whereClause, whereArgs, null, null, null);
    }

    public List<Classe> list(String table, String[] columns, String whereClause, String[] whereArgs, String orderBy) {
        return list(table, columns, whereClause, whereArgs, null, null, orderBy);
    }

    public List<Classe> list(String table, String[] columns, String whereClause, String[] whereArgs, String orderBy, String limit) {
        return list(table, columns, whereClause, whereArgs, null, null, orderBy, limit);
    }

    public List<Classe> list(String table, String[] columns, String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {
        return list(false, table, columns, whereClause, whereArgs, groupBy, having, orderBy, null);
    }

    public List<Classe> list(String table, String[] columns, String whereClause, String[] whereArgs, String groupBy, String having, String orderBy, String limit) {
        return list(false, table, columns, whereClause, whereArgs, groupBy, having, orderBy, limit);
    }

    public List<Classe> list(boolean distint, String table, String[] columns, String whereClause, String[] whereArgs, String groupBy, String having, String orderBy, String limit) {
        List<Classe> result = new ArrayList<>();
        try {
            try (Cursor c = Persistence.query(distint, table, columns, whereClause, whereArgs, groupBy, having, orderBy, limit)) {
                while (c.moveToNext()) {
                    result.add((Classe) Utils.getEntity(c, this.COLUMNS, this.getClass()));
                }
            } catch (Exception ex) {
                Messages.Exception(this.getClass().getSimpleName() + " (list) ", ex);
            }
        } catch (Exception ex) {
            Messages.Exception(this.getClass().getSimpleName() + " (list) ", ex);
        }
        return result;
    }

    private boolean insert_synchro(String action_name, Object author) {
        return insert_synchro(distant, action_name, author, synchronised);
    }

    public boolean insert_synchro(Synchronise distant, String action_name, Object author, boolean synchronised) {
        try {
            if (!synchronised) {
                return false;
            }
            if (!Utils.asString(TABLE_NAME)) {
                return false;
            }
            if (getId() == null) {
                return false;
            }
            if (!Utils.existTable("yvs_synchro_listen_table")) {
                return false;
            }
            ContentValues values = new ContentValues();
            values.put("action_name", action_name);
            values.put("id_source", Long.valueOf(getId().toString()));
            values.put("name_table", TABLE_NAME);
            values.put("groupe_table", "");
            values.put("to_listen", distant == null);
            values.put("ordre", 0);
            values.put("date_save", Utils.formatDateTime.format(new Date()));
            values.put("author", author != null ? Long.valueOf(author.toString()) : null);
            values.put("locked", false);
            Persistence.execSQL("DELETE FROM yvs_synchro_data_synchro WHERE id_listen IN (SELECT id FROM yvs_synchro_listen_table WHERE action_name = ? AND id_source = ? AND name_table = ?)", new String[]{action_name, getId().toString(), TABLE_NAME});
            Persistence.delete("yvs_synchro_listen_table", "action_name = ? AND id_source = ? AND name_table = ?", new String[]{action_name, getId().toString(), TABLE_NAME});
            long listen = Persistence.insert("yvs_synchro_listen_table", null, values);
            boolean succes = listen > 0;
            if (succes ? distant != null ? Utils.existTable("yvs_synchro_data_synchro") : false : false) {
                values = new ContentValues();
                values.put("id_distant", Long.valueOf(distant.getDistant().toString()));
                values.put("id_listen", listen);
                values.put("serveur", distant.getServeur().getId());
                values.put("date_save", Utils.formatDateTime.format(new Date()));
                long synchro = Persistence.insert("yvs_synchro_data_synchro", null, values);
                succes = synchro > 0;
            }
            return succes;
        } catch (Exception ex) {
            Messages.Exception(ex);
        }
        return false;
    }

    @Override
    public Classe clone() throws CloneNotSupportedException {
        Classe clone = (Classe) super.clone();
        for (Field field : getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(XmlTransient.class) ? !field.isAnnotationPresent(Transient.class) : false) {
                if (field.getType().equals(List.class) || field.getType().equals(ArrayList.class)) {
                    field.setAccessible(true);
                    try {
                        Object list = field.get(this);
                        if (list != null) {
                            field.set(clone, clone((List) list));
                        }
                    } catch (IllegalAccessException ex) {
                        Messages.Exception(ex);
                    }
                }
            }
        }
        return clone;
    }

    private static <T> List<T> clone(List<T> original) throws CloneNotSupportedException {
        List<T> copy = new ArrayList<>();
        for (T item : original) {
            copy.add(item instanceof Classe ? ((T) ((Classe) item).clone()) : item);
        }
        return copy;
    }

    public Class getClassList(Field field) {
        try {
            Field listField = field;
            ParameterizedType listType = (ParameterizedType) listField.getGenericType();
            return (Class) listType.getActualTypeArguments()[0];
        } catch (Exception ex) {
            Messages.Exception(ex);
        }
        return null;
    }

    public Classe deepClone() {
        try {
            String jsonString = Utils.gson.toJson(this, getClass());
            JSONObject request = new JSONObject(jsonString);
            return Utils.gson.fromJson(request.toString(), getClass());
        } catch (JSONException ex) {
            Log.e("CLONE EXCEPTION ", "CLASS_NAME : " + getClass().getSimpleName());
            Messages.Exception(ex);
        }
        return null;
    }

}
