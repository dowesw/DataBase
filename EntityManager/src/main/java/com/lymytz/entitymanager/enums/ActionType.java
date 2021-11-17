package com.lymytz.entitymanager.enums;

import com.lymytz.entitymanager.tools.Constantes;

public enum ActionType {
    NO_ACTION, CASCADE, SET_NULL;

    public String value() {
        switch (this) {
            case NO_ACTION:
                return Constantes.ACTION_NO_ACTION;
            case CASCADE:
                return Constantes.ACTION_CASCADE;
            case SET_NULL:
                return Constantes.ACTION_SET_NULL;
        }
        return Constantes.ACTION_NO_ACTION;
    }
}
