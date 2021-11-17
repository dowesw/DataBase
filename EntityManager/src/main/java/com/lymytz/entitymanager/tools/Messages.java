package com.lymytz.entitymanager.tools;

import android.annotation.SuppressLint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Messages {

    public static void Exception(Throwable ex) {
        try {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, ex);
            String file = Chemins.Logs(Constantes.CONTEXT) + Constantes.FILE_SEPARATOR + "Log.txt";
            WriteTxt(file, ex.getMessage());
        } catch (Exception e) {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void Exception(String title, Throwable ex) {
        try {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, title, ex);
            String file = Chemins.Logs(Constantes.CONTEXT) + Constantes.FILE_SEPARATOR + "Log.txt";
            WriteTxt(file, "[" + title + "] " + ex.getMessage());
        } catch (Exception e) {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, title, e);
        }
    }

    @SuppressLint("NewApi")
    public static void WriteTxt(String fileDestination, String message) {
        try {
            boolean append = true;
            File file = new File(fileDestination);
            if (!file.exists()) {
                file.createNewFile();
                append = false;
            }
            try (FileWriter fw = new FileWriter(file, append)) {
                try (BufferedWriter out = new BufferedWriter(fw)) {
                    out.write(Utils.dateTimeStr(new Date()) + " : " + message);
                    out.newLine();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
