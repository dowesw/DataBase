package com.lymytz.entitymanager.db;

import android.database.Cursor;
import android.os.Build;

import com.lymytz.entitymanager.annotations.Table;
import com.lymytz.entitymanager.bean.Columns;
import com.lymytz.entitymanager.tools.Classe;
import com.lymytz.entitymanager.tools.Messages;
import com.lymytz.entitymanager.tools.Utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import androidx.annotation.RequiresApi;

import static com.lymytz.entitymanager.tools.Constantes.DATABASE;

public class Paginator<T extends Serializable> implements Serializable {

    private Class<T> CLass;
    private String[] selectionArgs;
    private long count;
    private List<T> result;
    private String tableName;
    private String[] columnsName = null;
    public long offset = 0, limit = 10, max = 0;
    private List<ParametrePaginator> parametres;
    private List<Columns> columns = null;

    public Paginator(Class<T> CLass) {
        this.CLass = CLass;
        result = new ArrayList<>();
        parametres = new ArrayList<>();
    }

    public Class<T> getCLass() {
        return CLass;
    }

    public long getCount() {
        return count;
    }

    public List<T> getResult() {
        return result;
    }

    public String[] getSelectionArgs() {
        return selectionArgs;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String[] getColumnsName() {
        return columnsName;
    }

    public void setColumnsName(String[] columnsName) {
        this.columnsName = columnsName;
    }

    public List<Columns> getColumns() {
        return columns;
    }

    public void setColumns(List<Columns> columns) {
        this.columns = columns;
    }

    public boolean contains(ParametrePaginator parametre) {
        return parametres.contains(parametre);
    }

    public int getPosition(ParametrePaginator parametre) {
        return parametres.indexOf(parametre);
    }

    public void addAll(List<ParametrePaginator> parametres) {
        if (parametres != null ? !parametres.isEmpty() : false) {
            this.parametres.addAll(parametres);
        }
    }

    public void addParam(ParametrePaginator parametre) {
        int position = getPosition(parametre);
        if (parametre.getValue() == null) {
            if (position > -1) {
                parametres.remove(position);
            }
        } else {
            if (position > -1) {
                parametres.set(position, parametre);
            } else {
                parametres.add(parametre);
            }
        }
    }

    public void clear() {
        parametres.clear();
    }

    public List<T> execute(String tableName, String orderBy, long offset, long limit) {
        return execute(tableName, "y.id", orderBy, offset, limit);
    }

    public List<T> execute(String tableName, String[] columns, String orderBy, long offset, long limit) {
        return execute(false, tableName, columns, "y.id", orderBy, offset, limit);
    }

    public List<T> execute(String tableName, List<Columns> columns, String orderBy, long offset, long limit) {
        return execute(false, tableName, columns, orderBy, offset, limit);
    }

    public List<T> execute(boolean distinct, String tableName, List<Columns> columns, String orderBy, long offset, long limit) {
        this.columns = columns;
        return execute(distinct, tableName, Utils.getColumns(columns), "y.id", orderBy, offset, limit);
    }

    public List<T> execute(boolean distinct, String tableName, String[] columns, String orderBy, long offset, long limit) {
        return execute(distinct, tableName, columns, "y.id", orderBy, offset, limit);
    }

    public List<T> execute(String tableName, String fieldCount, String orderBy, long offset, long limit) {
        return execute(false, tableName, Utils.getColumns(Utils.getColumns(CLass, "y")), fieldCount, orderBy, offset, limit);
    }

    public List<T> execute(String orderBy, long offset, long limit) {
        return execute(false, tableName, columnsName, "y.id", orderBy, offset, limit);
    }

    public List<T> execute(boolean distinct, String tableName, String[] columns, String fieldCount, String orderBy, long offset, long limit) {
        result = new ArrayList<>();
        try {
            this.offset = offset;
            this.limit = limit;
            tableName = tableName.contains("JOIN") ? tableName : tableName + " y";
            String select = "SELECT ";
            String colonne = "COUNT(" + (distinct ? "DISTINCT (" : "") + fieldCount + (distinct ? ") " : "") + ") ";
            String selection = buildSelection(parametres);
            String query = select + colonne + " FROM " + tableName + " WHERE " + selection;
            System.err.println("QUERY COUNT : " + query);
            DATABASE.beginTransaction();
            try (Cursor cursor = DATABASE.rawQuery(query, null)) {
                while (cursor.moveToNext()) {
                    count = cursor.getLong(0);
                }
                DATABASE.setTransactionSuccessful();
            } catch (Exception ex) {
                Logger.getLogger(Paginator.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DATABASE.endTransaction();
            }
            if (distinct) {
                select += "DISTINCT ";
            }
            colonne = "";
            for (String column : columns) {
                if (Utils.asString(colonne)) {
                    colonne += ", " + column;
                } else {
                    colonne = column;
                }
            }
            query = select + colonne + " FROM " + tableName + " WHERE " + selection + " ORDER BY " + orderBy;
            if (limit > 0) {
                query += " LIMIT " + limit + " OFFSET " + offset;
            }
            System.err.println("QUERY : " + query);
            DATABASE.beginTransaction();
            try (Cursor cursor = DATABASE.rawQuery(query, null)) {
                while (cursor.moveToNext()) {
                    T entity = (T) Utils.getEntity(cursor, Utils.getColumns(CLass), CLass);
                    if (this.columns != null ? !this.columns.isEmpty() ? entity instanceof Classe : false : false) {
                        for (int i = 0; i < this.columns.size(); i++) {
                            Columns column = this.columns.get(i);
                            if (column.isExternal()) {
                                Field field = Utils.getField(column.getTableNameFK(), entity.getClass());
                                if (field != null) {
                                    field.setAccessible(true);
                                    Object foreing = field.get(entity);
                                    if (foreing != null) {
                                        Field field_foreing = Utils.getField(column.getColumnNameFK(), foreing.getClass());
                                        if (field_foreing != null) {
                                            field_foreing.setAccessible(true);
                                            if (field_foreing.getType().isAnnotationPresent(Table.class)) {
                                                Field key = Utils.getId(field_foreing.getType());
                                                if (key != null) {
                                                    key.setAccessible(true);
                                                    Object foreign = field_foreing.getType().newInstance();
                                                    key.set(foreign, Utils.value(cursor, key, i));
                                                    field_foreing.set(foreing, foreign);
                                                }
                                            } else {
                                                field_foreing.set(foreing, Utils.value(cursor, field_foreing, i));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    result.add(entity);
                }
                DATABASE.setTransactionSuccessful();
            } catch (Exception ex) {
                Logger.getLogger(Paginator.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DATABASE.endTransaction();
            }
        } catch (Exception ex) {
            Logger.getLogger(Paginator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public String buildSelection(List<ParametrePaginator> params) {
        String selection = "";
        if (params != null ? !params.isEmpty() : false) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                params.stream().forEach(x -> {
                    x.setLastParam(false);
                });
            } else {
                for (ParametrePaginator x : params) {
                    x.setLastParam(false);
                }
            }
            params.get(params.size() - 1).setLastParam(true);
            for (ParametrePaginator p : params) {
                selection += buildSelection(p) + (p.isLastParam() ? "" : (" " + p.getPredicat() + " "));
            }
        }
        return selection;
    }

    public String buildSelection(ParametrePaginator p) {
        String selection = "";
        if (Utils.asString(p.getColonne()) && !p.isCompareAttribut()) {
            if (p.getOperateur().trim().equals("IS NOT NULL") || p.getOperateur().trim().equals("IS NULL")) {
                selection += "" + p.getColonne() + " " + p.getOperateur();
            } else {
                if (p.getOperateur().trim().equals("BETWEEN") && !p.getOperateur().trim().equals("NOT BETWEEN")) {
                    selection += "" + p.getColonne() + " " + p.getOperateur() + " " + getValue(p.getValue()) + " AND " + getValue(p.getOtherValue());
                } else if (p.getOperateur().trim().equals("IN") && !p.getOperateur().trim().equals("NOT IN")) {
                    selection += "" + p.getColonne() + " " + p.getOperateur() + " (" + p.getValue().replace("[", "").replace("]", "") + ")";
                } else {
                    selection += "" + p.getColonne() + " " + p.getOperateur() + " " + getValue(p.getValue());
                }
            }
        }
        if (!p.getOtherExpression().isEmpty()) {
            p.getOtherExpression().get(p.getOtherExpression().size() - 1).setLastParam(true);
            if (p.getColonne() != null) {
                selection += " " + p.getPredicat() + " ";
            }
            selection += "(" + buildSelection(p.getOtherExpression()) + ")";
        }
        return selection;
    }

    private String getValue(String value) {
        String reponse = value;
        try {
            if (Utils.asDate(value)) {
                reponse = "'" + Utils.formatDate.format(new SimpleDateFormat("dd-MM-yyyy").parse(value)) + "'";
            } else if (Utils.asBoolean(value)) {
                reponse = (Boolean.valueOf(value) ? 1 : 0) + "";
            } else if (!Utils.asNumeric(value)) {
                if (value.split(",").length > 1) {
                    boolean is_numeric = true;
                    for (String v : value.split(",")) {
                        if (!Utils.asNumeric(v)) {
                            is_numeric = false;
                            break;
                        }
                    }
                    if (!is_numeric) {
                        reponse = "";
                        for (String v : value.split(",")) {
                            if (Utils.asString(reponse)) {
                                reponse += ",'" + v + "'";
                            } else {
                                reponse = "'" + v + "'";
                            }
                        }
                    }
                } else {
                    reponse = "'" + value + "'";
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(Paginator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reponse;
    }

    //reconstruit la  requête en y appliquant la chaine de paramètres dynamiques  préalablement construite au niveau des formulaires
    public String buildDynamicQuery(boolean jdbc) {
        String query = "";
        try {
            query = buildDynamicQuery(parametres, jdbc);
            for (String value : selectionArgs) {
                int index = query.indexOf("?");
                if (index > -1) {
                    String startQuery = query.substring(0, index);
                    String endQuery = query.substring(index + 1, query.length());
                    query = startQuery;
                    if (Utils.asNumeric(value)) {
                        query += value;
                    } else {
                        query += "'" + value + "'";
                    }
                    query += endQuery;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Paginator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return query;
    }

    public String buildDynamicQuery(List<ParametrePaginator> params, boolean jdbc) {
        try {
            buildDynamicParameter(params, jdbc);
            return buildRequete(params);
        } catch (Exception ex) {
            Logger.getLogger(Paginator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    //rempli les paramètres constrits sous forme de liste d'objets dans les tableaux champs / valeur accepté par notre exécuteur de requête
    public void buildDynamicParameter(List<ParametrePaginator> params, boolean jdbc) {
        try {
            String query = "WHERE ";
            List<ParametrePaginator> lp = new ArrayList<>();
            for (ParametrePaginator p : params) {
                ajouteParam(p, lp);
            }
            int i = 0;
            selectionArgs = new String[lp.size()];
            for (ParametrePaginator p : lp) {
                if (Utils.asDate(p.getValue()) && !jdbc) {
                    selectionArgs[i] = Utils.formatDate.format(new SimpleDateFormat("dd-MM-yyyy").parse(p.getValue()));
                } else {
                    selectionArgs[i] = p.getValue();
                }
                i++;
            }
        } catch (Exception ex) {
            Logger.getLogger(Paginator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Suite de méhode qui permettent de construire la chaine de paramètre sous forme de String*/
    private String buildRequete(List<ParametrePaginator> lp) {
        String re = "";
        try {
            for (ParametrePaginator p : lp) {
                int i = lp.indexOf(p);
                if (i != (lp.size() - 1)) {
                    p.setLastParam(false);
                } else {
                    p.setLastParam(true);
                }
                re += decomposeRequete(p) + "" + ((p.isLastParam()) ? "" : " " + p.getPredicat()) + " ";
            }
        } catch (Exception ex) {
            Logger.getLogger(Paginator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return re;
    }

    public List<ParametrePaginator> ajouteParam(ParametrePaginator p, List<ParametrePaginator> lp) {
        try {
            if (p.getColonne() != null && !(p.getOperateur().trim().equals("IS NOT NULL") || p.getOperateur().trim().equals("IS NULL")) && !p.isCompareAttribut()) {
                if (!p.getOperateur().trim().equals("BETWEEN") && !p.getOperateur().trim().equals("NOT BETWEEN")) {
                    lp.add(p);
                } else {
                    lp.add(p);
                    p = new ParametrePaginator(p.getColonne(), p.getAttribut() + "1", p.getOtherValue());
                    lp.add(p);
                }
            }
            if (!p.getOtherExpression().isEmpty()) {
                for (ParametrePaginator pp : p.getOtherExpression()) {
                    ajouteParam(pp, lp);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Paginator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lp;
    }

    private String decomposeRequete(ParametrePaginator p) {
        String re = "";
        try {
            if (p.getColonne() != null) {
                re += concateneParam(p);
            }
            if (!p.getOtherExpression().isEmpty()) {
                p.getOtherExpression().get(p.getOtherExpression().size() - 1).setLastParam(true);
                if (p.getColonne() != null) {
                    re += " " + p.getPredicat() + " (";
                } else {
                    re += "(";
                }
                for (ParametrePaginator p1 : p.getOtherExpression()) {
                    re += decomposeRequete(p1) + (p1.isLastParam() ? "" : (" " + p1.getPredicat() + " "));
                }
                re += ")";
            }
        } catch (Exception ex) {
            Logger.getLogger(Paginator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return re;
    }

    private String concateneParam(ParametrePaginator p) {
        String re = "";
        try {
            if (p.getOperateur().trim().equals("BETWEEN") || p.getOperateur().trim().equals("NOT BETWEEN")) {
                re += "(" + p.getColonne() + " " + p.getOperateur() + " ? AND ?)";
            } else {
                re += "" + p.getColonne() + " " + p.getOperateur();
                if (!p.isCompareAttribut()) {
                    if (p.getOperateur().trim().equals("IS NOT NULL") || p.getOperateur().trim().equals("IS NULL")) {
                        re += "";
                    } else if (p.getOperateur().trim().equals("IN") || p.getOperateur().trim().equals("NOT IN")) {
                        re += " (?)";
                    } else {
                        re += " ?";
                    }
                } else {
                    re += "" + ((String) p.getValue());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Paginator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return re;
    }
}
