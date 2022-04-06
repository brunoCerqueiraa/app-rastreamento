package com.gerentec.manutencao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gerentec.manutencao.modelo.TbComp;
import com.gerentec.manutencao.modelo.TbSelfie;

import java.util.ArrayList;
import java.util.List;

public class TbSelfieDAO {

    private static SQLiteDatabase db;

    public TbSelfieDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public void createTable() {
        try {
            db.beginTransaction();
            String sql ="CREATE TABLE IF NOT EXISTS tbSelfie (" +
                    "		idSelfie INTEGER PRIMARY KEY, " +
                    "		idEquipe INTEGER, " +
                    "		finalizado TEXT " +
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

    public void insert(TbSelfie modelo) {
        try {
            db.beginTransaction();
            String insert =
                    "INSERT INTO tbSelfie ( " +
                            "       idSelfie, " +
                            "		idEquipe, " +
                            "		finalizado " +
                            ") VALUES ( ?, ?, ?) ";
            db.execSQL(insert, new Object[] {
                    modelo.getIdSelfie(), modelo.getIdEquipe(), modelo.getFinalizado()
            });
            db.setTransactionSuccessful();
        }
        catch(Exception e){
            e.getStackTrace();
        }
        finally {
            db.endTransaction();
        }
    }

    public void atualizar(TbSelfie modelo){
        try {
            db.beginTransaction();
            String insert =
                    "UPDATE tbSelfie SET idEquipe=?, finalizado=? WHERE idSelfie=? ";
            db.execSQL(insert, new Object[] {
                    modelo.getIdEquipe(), modelo.getFinalizado(), modelo.getIdSelfie()
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

    public boolean alreadyExists(TbSelfie tbSelfie){
        Cursor cursor = null;
        try {
            String sql = "SELECT count(*) AS qtde FROM tbSelfie WHERE idSelfie = ?";
            cursor = db.rawQuery(sql, new String[]{
                    tbSelfie.getIdSelfie().toString()
            });
            cursor.moveToFirst();
            int rowCount = cursor.getInt(cursor.getColumnIndex("qtde"));
            if (rowCount > 0) {
                return true;
            }
        }
        catch (Exception e){
            e.getStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    public TbSelfie model(Long id) {
        Cursor cursor = null;
        TbSelfie model = new TbSelfie();
        try {
            cursor = db.rawQuery(
                    "SELECT * FROM tbSelfie WHERE idSelfie = " + id, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                model.setIdSelfie(cursor.getLong(cursor.getColumnIndex("idSelfie")));
                model.setIdEquipe(cursor.getLong(cursor.getColumnIndex("idEquipe")));
                model.setFinalizado(cursor.getString(cursor.getColumnIndex("finalizado")));

            }
        }
        catch (Exception e) {
            e.getStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return model;
    }

    public List<TbSelfie> listPendente() {
        Cursor cursor = null;
        List<TbSelfie> list = new ArrayList<>();

        try {
            cursor = db.rawQuery(
                    "SELECT * FROM tbSelfie WHERE finalizado IS NULL", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    TbSelfie model = new TbSelfie();
                    model.setIdSelfie(cursor.getLong(cursor.getColumnIndex("idSelfie")));
                    model.setIdEquipe(cursor.getLong(cursor.getColumnIndex("idEquipe")));
                    model.setFinalizado(cursor.getString(cursor.getColumnIndex("finalizado")));
                    list.add(model);
                    cursor.moveToNext();
                }
                while (!cursor.isAfterLast());
            }
        }
        catch (Exception e) {
            e.getStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public void apagarBase(Long idGenerico) {
        try {
            db.beginTransaction();
            String insert =
                    "DELETE FROM tbSelfie WHERE idSelfie NOT IN (?)";
            db.execSQL(insert, new Object[] {
                    idGenerico
            });
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            db.endTransaction();
        }
    }
}
