package com.gerentec.manutencao.auxiliar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.gerentec.manutencao.dao.BaseDadosDAO;

public class BaseDados {
    public static String nameDB = "manutencao.db";
    public static String pathDoBanco = "/data/data/com.gerentec.manutencao/databases/";
    public static SQLiteDatabase bd;
    public static String pathFotos = "/manutencao_fotos/";

    @SuppressLint("WrongConstant")
    public static SQLiteDatabase getBD(Context context) {
        return isOpen(context);
    }

    @SuppressLint("WrongConstant")
    public static SQLiteDatabase isOpen(Context context) {
        if(bd == null){
            bd = context.openOrCreateDatabase(nameDB, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            return bd;
        }
        else if(!bd.isOpen()){
            bd = context.openOrCreateDatabase(nameDB, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            return bd;
        }
        else{
            return bd;
        }

    }

    public static String getPathFotos(){
        return Environment.getExternalStorageDirectory() + pathFotos;
    }

    public static String getPathDb() {
        return BaseDados.pathDoBanco+"/"+BaseDados.nameDB;
    }

    public static void fazerBackUp(Context context){
        BaseDadosDAO baseDadosDAO = new BaseDadosDAO(getBD(context));
        try{
            baseDadosDAO.backUpDB();
        }
        catch (Exception e){
            System.out.println("[BaseDados] Error in fazerBackUp: " + e.toString());
        }
    }
}
