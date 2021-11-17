package com.lymytz.entitymanager.db;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParametrePaginator implements Serializable {

    private String colonne;
    private String attribut;
    private String value;
    private String otherValue;
    private String predicat;
    private String operateur;
    private boolean lastParam;
    private boolean compareAttribut;
    private List<ParametrePaginator> otherExpression;

    public ParametrePaginator(String colonne) {
        this.colonne = colonne;
        otherExpression = new ArrayList<>();
    }

    public ParametrePaginator(String colonne, String attribut) {
        this(colonne);
        this.attribut = attribut;
    }

    public ParametrePaginator(String colonne, String attribut, Object value) {
        this(colonne, attribut);
        this.value = value != null ? value.toString() : null;
    }

    public ParametrePaginator(String colonne, String attribut, Object value, String operateur, String predicat) {
        this(colonne, attribut, value);
        this.predicat = predicat;
        this.operateur = operateur;
    }

    public ParametrePaginator(String colonne, String attribut, Object value, Object otherValue, String operateur, String predicat) {
        this(colonne, attribut, value, operateur, predicat);
        this.otherValue = otherValue.toString();
    }

    public String getAttribut() {
        return attribut;
    }

    public void setAttribut(String attribut) {
        this.attribut = attribut;
    }

    public String getColonne() {
        return colonne;
    }

    public void setColonne(String colonne) {
        this.colonne = colonne;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOtherValue() {
        return otherValue;
    }

    public void setOtherValue(String otherValue) {
        this.otherValue = otherValue;
    }

    public String getPredicat() {
        return predicat;
    }

    public void setPredicat(String predicat) {
        this.predicat = predicat;
    }

    public String getOperateur() {
        return operateur;
    }

    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }

    public List<ParametrePaginator> getOtherExpression() {
        return otherExpression;
    }

    public void setOtherExpression(List<ParametrePaginator> otherExpression) {
        this.otherExpression = otherExpression;
    }

    public boolean isLastParam() {
        return lastParam;
    }

    public void setLastParam(boolean lastParam) {
        this.lastParam = lastParam;
    }

    public boolean isCompareAttribut() {
        return compareAttribut;
    }

    public void setCompareAttribut(boolean compareAttribut) {
        this.compareAttribut = compareAttribut;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParametrePaginator that = (ParametrePaginator) o;
        return Objects.equals(attribut, that.attribut);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(attribut);
    }

    @Override
    public String toString() {
        return "ParametrePaginator{" +
                "attribut='" + attribut + '\'' +
                '}';
    }
}
