package com.gerentec.manutencao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.gerentec.manutencao.modelo.FtSelfie;

import java.util.ArrayList;
import java.util.List;

public class FtSelfieDAO {

    private static SQLiteDatabase db;

    public FtSelfieDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public void createTable(){
        try {
            db.beginTransaction();
            String sql =
                    "CREATE TABLE IF NOT EXISTS ftSelfie (" +
                            "		idFtSelfie INTEGER PRIMARY KEY, " +
                            "		idSelfie INTEGER, " +
                            "		dtInsert TEXT, " +
                            "		coordX TEXT, " +
                            "       coordY TEXT," +
                            "       arquivo TEXT, " +
                            "       recebidoServidor TEXT " +
                            ");";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        }
        catch (Exception e){
            e.getStackTrace();
        }
        finally {
            db.endTransaction();
        }
    }

    public void inserir(FtSelfie ftSelfie) {
        try {
            db.beginTransaction();
            String insert =
                    "INSERT INTO ftSelfie ( " +
                            "		idFtSelfie, idSelfie, dtInsert, coordX, " +
                            "		coordY, arquivo, recebidoServidor " +
                            ") VALUES ( " +
                            "       ?,?,?,?,?,?,? " +
                            ") ";
            db.execSQL(insert, new Object[] {
                    ftSelfie.getIdFtSelfie(), ftSelfie.getIdSelfie(), ftSelfie.getDtInsert(), ftSelfie.getCoordX(), ftSelfie.getCoordY(), ftSelfie.getArquivo(), ftSelfie.getRecebidoServidor()

            });
            db.setTransactionSuccessful();
        }
        catch (Exception e){
            e.getStackTrace();
        }
        finally {
            db.endTransaction();
        }
    }

    public long generateId() {
        int response = 0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT MAX(idFtSelfie) AS id FROM ftSelfie ", null);
            int id = cursor.getColumnIndex("id");
            cursor.moveToFirst();
            response = cursor.getInt(id) + 1;
        }
        catch (Exception e){
            e.getStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return response;
    }

    public void atualizarRecebidoServidor(FtSelfie ftSelfie) {
        try {
            db.beginTransaction();
            String insert =
                    "UPDATE ftSelfie SET " +
                            "		recebidoServidor = 'S' " +
                            "WHERE idFtSelfie=? ";
            db.execSQL(insert, new Object[]{
                    ftSelfie.getIdFtSelfie()
            });
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
    }

    public List<FtSelfie> listParaEnviar() {
        Cursor cursor = null;
        List<FtSelfie> list = new ArrayList<>();

        try {
            cursor = db.rawQuery(
                    "SELECT * FROM ftSelfie" +
                            "  WHERE recebidoServidor = 'N'", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    FtSelfie model = new FtSelfie();
                    model.setIdFtSelfie(cursor.getLong(cursor.getColumnIndex("idFtSelfie")));
                    model.setIdSelfie(cursor.getLong(cursor.getColumnIndex("idSelfie")));
                    model.setDtInsert(cursor.getString(cursor.getColumnIndex("dtInsert")));
                    model.setCoordX(cursor.getString(cursor.getColumnIndex("coordX")));
                    model.setCoordY(cursor.getString(cursor.getColumnIndex("coordY")));
                    model.setArquivo(cursor.getString(cursor.getColumnIndex("arquivo")));
                    model.setRecebidoServidor(cursor.getString(cursor.getColumnIndex("recebidoServidor")));
                    list.add(model);
                    cursor.moveToNext();
                }
                while (!cursor.isAfterLast());
            }
        }
        catch (Exception e) {
            System.out.println("Sim, o erro está aqui. Como advinhou?");
            e.getStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }
}
