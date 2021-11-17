package com.lymytz.entitymanager.tools;

import android.content.Context;

import java.io.File;

public class Chemins {

    public static File Root(Context context) {
        return context.getExternalFilesDir("");
    }

    public static String Data(Context context) {
        File file = Root(context);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String Logs(Context context) {
        File file = new File(Data(context), "logs");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String Backup(Context context) {
        File file = new File(Data(context), "backup");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }
}
