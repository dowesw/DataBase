package com.lymytz.entitymanager.tools;

import java.io.Serializable;

public interface IClasse extends Serializable {

    Classe Open();

    boolean save();

    boolean update();

    boolean delete();

    Classe one(Object id);

}
