package com.gerentec.manutencao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gerentec.manutencao.modelo.AndTempoParametro;

public class AndTempoParametroDAO {

    private static SQLiteDatabase db;

    public AndTempoParametroDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public void createTable() {
        try {
            db.beginTransaction();
            String sql ="CREATE TABLE IF NOT EXISTS tbAndTempoParametro (" +
                    "		idAndTempoParametro INTEGER PRIMARY KEY, " +
                    "		tipo TEXT, " +
                    "		tempo INTEGER, " +
                    "       situacao TEXT " +
                    ");";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
    }

    public boolean alreadyExists(AndTempoParametro andTempoParametro){
        Cursor cursor = null;
        try {
            String sql = "SELECT count(*) AS qtde FROM tbAndTempoParametro WHERE idAndTempoParametro = ?";
            cursor = db.rawQuery(sql, new String[]{
                    andTempoParametro.getIdAndTempoParametro().toString()
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

    public void insert(AndTempoParametro andTempoParametro) {
        try {
            db.beginTransaction();
            String insert =
                    "INSERT INTO tbAndTempoParametro ( " +
                            "       idAndTempoParametro, " +
                            "       tipo, " +
                            "		tempo, " +
                            "		situacao " +
                            ") VALUES ( ?, ?, ?, ?) ";
            db.execSQL(insert, new Object[] {
                    andTempoParametro.getIdAndTempoParametro(), andTempoParametro.getTipo(), andTempoParametro.getTempo(), andTempoParametro.getSituacao()
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

    public void atualizar(AndTempoParametro andTempoParametro){
        try {
            db.beginTransaction();
            String insert =
                    "UPDATE tbAndTempoParametro SET tipo=?, tempo=?, situacao=? WHERE idAndTempoParametro=? ";
            db.execSQL(insert, new Object[] {
                    andTempoParametro.getTipo(), andTempoParametro.getTempo(), andTempoParametro.getSituacao(), andTempoParametro.getIdAndTempoParametro()
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

    public Long getTempoByTipo(String tipo) {
        Cursor cursor = null;
        AndTempoParametro andTempoParametro = new AndTempoParametro();
        try {
            cursor = db.rawQuery(
                    "SELECT tipo, tempo, situacao" +
                            "  FROM tbAndTempoParametro" +
                            "  WHERE situacao = 'A' AND tipo = '" + tipo + "'", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                andTempoParametro.setSituacao(cursor.getString(cursor.getColumnIndex("situacao")));
                andTempoParametro.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
                andTempoParametro.setTempo(cursor.getLong(cursor.getColumnIndex("tempo")));
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
        return andTempoParametro.getTempo();
    }
}
