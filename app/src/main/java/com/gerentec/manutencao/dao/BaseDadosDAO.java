package com.gerentec.manutencao.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.gerentec.manutencao.auxiliar.BaseDados;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class BaseDadosDAO {

    private static SQLiteDatabase db;
    private static Context context;

    public BaseDadosDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public void backUpDB(){
        try {
            final String inFileName = BaseDados.pathDoBanco + "/" + BaseDados.nameDB;
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            String outFileName = Environment.getExternalStorageDirectory() + "/" + BaseDados.nameDB;
            OutputStream output = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            fis.close();
        }
        catch (Exception e){
            System.out.println("[BaseDadosDAOException] Error in BaseDadosDAO: " + e.toString());
        }
    }
}
