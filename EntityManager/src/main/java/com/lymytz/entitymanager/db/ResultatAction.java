package com.lymytz.entitymanager.db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.lymytz.entitymanager.annotations.Column;
import com.lymytz.entitymanager.annotations.Entity;
import com.lymytz.entitymanager.annotations.Table;
import com.lymytz.entitymanager.bean.Columns;
import com.lymytz.entitymanager.tools.Constantes;
import com.lymytz.entitymanager.bean.Tables;
import com.lymytz.entitymanager.tools.Messages;
import com.lymytz.entitymanager.tools.Utils;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "metadata_resultat_action")
public class ResultatAction<T extends Serializable> implements Serializable {

    @Column(name = "result")
    private boolean result = false;
    @Column(name = "continu")
    private boolean continu = true;
    @Column(name = "code_info")
    private int codeInfo;
    @Column(name = "id_entity")
    private Long idEntity;
    @Column(name = "source_entity")
    private String sourceEntity;
    @Column(name = "message")
    private String message = "Pas encore synchronisé";
    @Column(name = "module")
    private String module;
    @Column(name = "fonctionalite")
    private String fonctionalite;
    private Object data;
    public T entity;

    protected String QUERY;
    private String TABLE_NAME;
    private MetadataConstraint ID;
    protected List<Columns> COLUMNS;

    public ResultatAction() {
        if (!Utils.asString(TABLE_NAME)) {
            TABLE_NAME = Utils.getTableName(this.getClass());
            ID = Utils.getPrimaryKey(getClass());
        }
    }

    public ResultatAction(boolean result, Object data) {
        this();
        this.result = result;
        this.data = data;
    }

    public ResultatAction(boolean result, Object data, String message) {
        this(result, data);
        this.message = message;
    }

    public ResultatAction(boolean result, Object data, Long idEntity, String message) {
        this(result, data, message);
        this.idEntity = idEntity;
    }

    public ResultatAction(boolean result, Object data, String message, int codeInfo) {
        this(result, data, message);
        this.codeInfo = codeInfo;
    }

    public ResultatAction(boolean result, Object data, boolean continu, String message) {
        this(result, data, message);
        this.continu = continu;
    }

    public ResultatAction(boolean result, Object data, String message, T entity) {
        this(result, data, message);
        this.entity = entity;
    }

    public ResultatAction(boolean result, Long idEntity, String sourceEntity, String message) {
        this.result = result;
        this.idEntity = idEntity;
        this.sourceEntity = sourceEntity;
        this.message = message;
    }

    public ResultatAction(boolean result, int codeInfo, String message) {
        this();
        this.result = result;
        this.codeInfo = codeInfo;
        this.message = message;
    }

    public ResultatAction(boolean result, int codeInfo, String message, String module, String fonctionalite) {
        this();
        this.result = result;
        this.codeInfo = codeInfo;
        this.message = message;
        this.module = module;
        this.fonctionalite = fonctionalite;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getCodeInfo() {
        return codeInfo;
    }

    public void setCodeInfo(int codeInfo) {
        this.codeInfo = codeInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFonctionalite() {
        return fonctionalite;
    }

    public void setFonctionalite(String fonctionalite) {
        this.fonctionalite = fonctionalite;
    }

    public Long getIdEntity() {
        return idEntity != null ? idEntity : 0;
    }

    public void setIdEntity(Long idEntity) {
        this.idEntity = idEntity;
    }

    public String getSourceEntity() {
        return sourceEntity != null ? sourceEntity : "";
    }

    public void setSourceEntity(String sourceEntity) {
        this.sourceEntity = sourceEntity;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public boolean isContinu() {
        return continu;
    }

    public void setContinu(boolean continu) {
        this.continu = continu;
    }

    public boolean isCorrect() {
        return isResult() || !isContinu();
    }

    public ResultatAction Open() {
        try {
            if (Constantes.PERSISTENCE != null) {
                if (!Utils.asString(TABLE_NAME)) {
                    TABLE_NAME = Utils.getTableName(getClass());
                }
                if (Utils.asString(TABLE_NAME)) {
                    Tables table = Constantes.PERSISTENCE.getTable(TABLE_NAME);
                    if (table != null) {
                        QUERY = table.getQueryCreate();
                        COLUMNS = table.getColumns();
                        ID = table.getId();
                    } else {
                        QUERY = Utils.queryCreateTable(getClass());
                        COLUMNS = Utils.getColumns(getClass());
                        ID = Utils.getPrimaryKey(getClass());

                        table = new Tables(TABLE_NAME);
                        table.setClasse(getClass());
                        table.setQueryCreate(QUERY);
                        table.setColumns(COLUMNS);
                        table.setId(ID);

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
        } catch (Exception ex) {
            Messages.Exception(this.getClass().getSimpleName() + " (Open) ", ex);
        }
        return this;
    }

    public void Truncate() {
        Utils.getDatabase().execSQL("DROP TABLE " + TABLE_NAME);
    }

    public boolean save() {
        try {
            ContentValues value = Utils.addParam(this);
            return Utils.getDatabase().insert(TABLE_NAME, null, value) > 0;
        } catch (Exception ex) {
            Messages.Exception(this.getClass().getSimpleName() + " (save) ", ex);
        }
        return false;
    }

    public boolean update() {
        try {
            ContentValues value = Utils.addParam(this);
            return Utils.getDatabase().update(TABLE_NAME, value, "source_entity = ? AND id_entity = ?", new String[]{sourceEntity, idEntity.toString()}) > 0;
        } catch (Exception ex) {
            Messages.Exception(this.getClass().getSimpleName() + " (update) ", ex);
        }
        return false;
    }

    public boolean delete() {
        try {
            if (Utils.asString(sourceEntity) && idEntity != null) {
                return Utils.getDatabase().delete(TABLE_NAME, "source_entity = ? AND id_entity = ?", new String[]{sourceEntity, idEntity.toString()}) > 0;
            }
        } catch (Exception ex) {
            Messages.Exception(this.getClass().getSimpleName() + " (delete) ", ex);
        }
        return false;
    }

    public boolean delete(String sourceEntity, Long idEntity) {
        try {
            return Utils.getDatabase().delete(TABLE_NAME, "source_entity = ? AND id_entity = ?", new String[]{sourceEntity, idEntity.toString()}) > 0;
        } catch (Exception ex) {
            Messages.Exception(this.getClass().getSimpleName() + " (delete) ", ex);
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public ResultatAction one(String sourceEntity, Long idEntity) {
        ResultatAction s = new ResultatAction();
        try {
            try (Cursor c = Utils.getDatabase().query(TABLE_NAME, Utils.getColumns(COLUMNS), "source_entity = ? AND id_entity = ?", new String[]{sourceEntity, idEntity.toString()}, null, null, null)) {
                while (c.moveToNext()) {
                    s = (ResultatAction) Utils.getEntity(c, this.COLUMNS, this.getClass());
                }
            } catch (Exception ex) {
                Messages.Exception(this.getClass().getSimpleName() + " (one) ", ex);
            }
        } catch (Exception ex) {
            Messages.Exception(this.getClass().getSimpleName() + " (one) ", ex);
        }
        return s;
    }

}
