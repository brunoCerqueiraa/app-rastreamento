package com.gerentec.manutencao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gerentec.manutencao.modelo.TbCompEquip;

import java.util.ArrayList;
import java.util.List;

public class TbCompEquipDAO {

    private static SQLiteDatabase db;

    public TbCompEquipDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public void createTable() {
        try {
            db.beginTransaction();
            String sql ="CREATE TABLE IF NOT EXISTS tbCompEquip (" +
                    "		idEquipamento INTEGER PRIMARY KEY, " +
                    "		descricao TEXT, " +
                    "		tipo TEXT, " +
                    "		km INTEGER, " +
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

    public void insert(TbCompEquip modelo) {
        try {
            db.beginTransaction();
            String insert =
                    "INSERT INTO tbCompEquip ( " +
                            "       idEquipamento, " +
                            "		descricao, " +
                            "		tipo, " +
                            "		km, " +
                            "		situacao " +
                            ") VALUES ( ?, ?, ?, ?, ?) ";
            db.execSQL(insert, new Object[] {
                    modelo.getIdEquipamento(), modelo.getDescricao(), modelo.getTipo(), modelo.getKm() ,modelo.getSitucao()
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

    public void atualizar(TbCompEquip tbCompEquip){
        try {
            db.beginTransaction();
            String insert =
                    "UPDATE tbCompEquip SET descricao=?, tipo=?, km=?, situacao=? WHERE idEquipamento=? ";
            db.execSQL(insert, new Object[] {
                    tbCompEquip.getDescricao(), tbCompEquip.getTipo(), tbCompEquip.getKm(), tbCompEquip.getSitucao(), tbCompEquip.getIdEquipamento()
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

    public boolean alreadyExists(TbCompEquip tbCompEquip){
        Cursor cursor = null;
        try {
            String sql = "SELECT count(*) AS qtde FROM tbCompEquip WHERE idEquipamento = ?";
            cursor = db.rawQuery(sql, new String[]{
                    tbCompEquip.getIdEquipamento().toString()
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

    public TbCompEquip model(Long id) {
        Cursor cursor = null;
        TbCompEquip model = new TbCompEquip();
        try {
            cursor = db.rawQuery(
                    "SELECT * FROM tbCompEquip WHERE idEquipamento = " + id, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                model.setIdEquipamento(cursor.getLong(cursor.getColumnIndex("idEquipamento")));
                model.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
                model.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
                model.setKm(cursor.getLong(cursor.getColumnIndex("km")));
                model.setSitucao(cursor.getString(cursor.getColumnIndex("situacao")));

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

    public List<TbCompEquip> list() {
        Cursor cursor = null;
        List<TbCompEquip> list = new ArrayList<>();

        try {
            cursor = db.rawQuery(
                    "SELECT * FROM tbCompEquip ORDER BY descricao", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    TbCompEquip model = new TbCompEquip();
                    model.setIdEquipamento(cursor.getLong(cursor.getColumnIndex("idEquipamento")));
                    model.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
                    model.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
                    model.setKm(cursor.getLong(cursor.getColumnIndex("km")));
                    model.setSitucao(cursor.getString(cursor.getColumnIndex("situacao")));
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
                    "DELETE FROM tbCompEquip WHERE idEquipamento NOT IN (?)";
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

    public void apagarEquipamento(Long idEquipamento) {
        try {
            db.beginTransaction();
            String insert =
                    "DELETE FROM tbCompEquip WHERE idEquipamento = ?";
            db.execSQL(insert, new Object[] {
                    idEquipamento
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
        List<TbCompEquip> list = list();
        array.add("Selecione");
        for (TbCompEquip tbCompEquip : list){
            array.add(tbCompEquip.getDescricao());
        }

        return array;
    }

    public List<Long> listAdapterId(){
        List<Long> array = new ArrayList<>();
        List<TbCompEquip> list = list();
        array.add(0l);

        for (TbCompEquip tbCompEquip : list){
            array.add(tbCompEquip.getIdEquipamento());
        }
        return array;
    }
}
