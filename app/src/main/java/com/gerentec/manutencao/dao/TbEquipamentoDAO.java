package com.gerentec.manutencao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gerentec.manutencao.modelo.TbEquipamento;

import java.util.ArrayList;
import java.util.List;

public class TbEquipamentoDAO {

    private static SQLiteDatabase db;

    public TbEquipamentoDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public void createTable() {
        try {
            db.beginTransaction();
            String sql ="CREATE TABLE IF NOT EXISTS tbEquipamento (" +
                    "		idEquipamento INTEGER PRIMARY KEY, " +
                    "		descricao TEXT, " +
                    "		tipo TEXT, " +
                    "		situacao TEXT " +
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

    public void insert(TbEquipamento modelo) {
        try {
            db.beginTransaction();
            String insert =
                    "INSERT INTO tbEquipamento ( " +
                            "       idEquipamento, " +
                            "		descricao, " +
                            "		tipo, " +
                            "		situacao " +
                            ") VALUES (?, ?, ?, ?) ";
            db.execSQL(insert, new Object[] {
                    modelo.getIdEquipamento(), modelo.getDescricao(), modelo.getTipo(), modelo.getSituacao()
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


    public TbEquipamento model(Long id) {
        Cursor cursor = null;
        TbEquipamento model = new TbEquipamento();
        try {
            cursor = db.rawQuery(
                    "SELECT * FROM tbEquipamento WHERE idEquipamento = " + id, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                model.setIdEquipamento(cursor.getLong(cursor.getColumnIndex("idEquipamento")));
                model.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
                model.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
                model.setSituacao(cursor.getString(cursor.getColumnIndex("situacao")));

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

    public List<TbEquipamento> list() {
        Cursor cursor = null;
        List<TbEquipamento> list = new ArrayList<>();

        try {
            cursor = db.rawQuery(
                    "SELECT * FROM tbEquipamento ORDER BY descricao", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    TbEquipamento model = new TbEquipamento();
                    model.setIdEquipamento(cursor.getLong(cursor.getColumnIndex("idEquipamento")));
                    model.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
                    model.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
                    model.setSituacao(cursor.getString(cursor.getColumnIndex("situacao")));
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

    public List<TbEquipamento> listFiltro(String where) {
        Cursor cursor = null;
        List<TbEquipamento> list = new ArrayList<>();

        try {
            cursor = db.rawQuery(
                    "SELECT * FROM tbEquipamento " + where, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    TbEquipamento model = new TbEquipamento();
                    model.setIdEquipamento(cursor.getLong(cursor.getColumnIndex("idEquipamento")));
                    model.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
                    model.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
                    model.setSituacao(cursor.getString(cursor.getColumnIndex("situacao")));
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
                    "DELETE FROM tbEquipamento WHERE idEquipamento NOT IN (?)";
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

    public void apagarByIdEquip(Long idEquip) {
        try {
            db.beginTransaction();
            String insert =
                    "DELETE FROM tbEquipamento WHERE idEquipamento = ?";
            db.execSQL(insert, new Object[] {
                    idEquip
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

    public ArrayList<String> listAdapterDesc(){
        ArrayList<String> array = new ArrayList<>();
        List<TbEquipamento> list = list();
        array.add("Selecione");
        for (TbEquipamento tbEquipamento : list){
            array.add(tbEquipamento.getDescricao());
        }

        return array;
    }

    public List<Long> listAdapterId(){
        List<Long> array = new ArrayList<>();
        List<TbEquipamento> list = list();
        array.add(0l);

        for (TbEquipamento tbEquipamento : list){
            array.add(tbEquipamento.getIdEquipamento());
        }
        return array;
    }
}
