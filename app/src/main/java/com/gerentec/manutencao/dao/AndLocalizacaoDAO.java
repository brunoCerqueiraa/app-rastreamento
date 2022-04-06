package com.gerentec.manutencao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.gerentec.manutencao.modelo.AndLocalizacao;

import java.util.ArrayList;
import java.util.List;

public class AndLocalizacaoDAO {

    private static SQLiteDatabase db;

    public AndLocalizacaoDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public void createTable() {
        try {
            db.beginTransaction();
            String sql ="CREATE TABLE IF NOT EXISTS tbAndLocalizacao (" +
                    "		idAndLocalizacao INTEGER PRIMARY KEY, " +
                    "		idEquipe INTEGER, " +
                    "       coordX TEXT, " +
                    "       coordY TEXT, " +
                    "       dtCel DATE, " +
                    "       recebidoServidor TEXT " +
                    ");";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
    }


    public void insert(AndLocalizacao andLocalizacao) {
        try {
            if (andLocalizacao.getCoordX().equals("0") || andLocalizacao.getCoordX().equals("0.0")){
                //Coord zerada
            }
            else{
                db.beginTransaction();
                String insert =
                        "INSERT INTO tbAndLocalizacao ( " +
                                "       idAndLocalizacao, " +
                                "		idEquipe, " +
                                "		coordX, " +
                                "		coordY, " +
                                "		dtCel, " +
                                "		recebidoServidor " +
                                ") VALUES ( ?, ?, ?, ?, ?, ?) ";
                db.execSQL(insert, new Object[] {
                        andLocalizacao.getIdAndLocalizacao(), andLocalizacao.getIdEquipe(), andLocalizacao.getCoordX(), andLocalizacao.getCoordY(), andLocalizacao.getDtCel(), andLocalizacao.getRecebidoServidor()
                });
                db.setTransactionSuccessful();
            }
        }
        catch(Exception e){
            e.getStackTrace();
        }
        finally {
            if (andLocalizacao.getCoordX().equals("0") || andLocalizacao.getCoordX().equals("0.0")){
                //Coord zerada
            }
            else {
                db.endTransaction();
            }
        }
    }

    public long generateIdAndLocalizacao() {
        int response = 0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT MAX(idAndLocalizacao) AS id FROM tbAndLocalizacao ", null);
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

    public List<AndLocalizacao> listAndLocalizacao(Long idEquipe) {
        Cursor cursor = null;
        List<AndLocalizacao> listAndLocalizacao = new ArrayList<>();
        try {
            cursor = db.rawQuery(
                    "SELECT idAndLocalizacao, idEquipe, coordX, coordY, dtCel" +
                            "  FROM tbAndLocalizacao" +
                            " WHERE idEquipe = " + idEquipe + " AND recebidoServidor = 'N'", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    AndLocalizacao andLocalizacao = new AndLocalizacao();
                    andLocalizacao.setIdAndLocalizacao(cursor.getLong(cursor.getColumnIndex("idAndLocalizacao")));
                    andLocalizacao.setIdEquipe(cursor.getLong(cursor.getColumnIndex("idEquipe")));
                    andLocalizacao.setCoordX(cursor.getString(cursor.getColumnIndex("coordX")));
                    andLocalizacao.setCoordY(cursor.getString(cursor.getColumnIndex("coordY")));
                    andLocalizacao.setDtCel(cursor.getString(cursor.getColumnIndex("dtCel")));

                    listAndLocalizacao.add(andLocalizacao);
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
        return listAndLocalizacao;
    }

    public void atualizarAndLocalizacaoRecebidoServidor(List<AndLocalizacao> listAndLocalizacao) {
        for (AndLocalizacao andLocalizacao : listAndLocalizacao) {
            try {
                db.beginTransaction();
                String insert =
                        "UPDATE tbAndLocalizacao SET " +
                                "		recebidoServidor = 'S' " +
                                "WHERE idAndLocalizacao=? ";
                db.execSQL(insert, new Object[]{
                        andLocalizacao.getIdAndLocalizacao()
                });
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }
    }
}
