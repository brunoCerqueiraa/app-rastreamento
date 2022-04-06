package com.gerentec.manutencao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gerentec.manutencao.modelo.TbFuncionario;

import java.util.ArrayList;
import java.util.List;

public class TbFuncionarioDAO {

    private static SQLiteDatabase db;

    public TbFuncionarioDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public void createTable() {
        try {
            db.beginTransaction();
            String sql ="CREATE TABLE IF NOT EXISTS tbFuncionario (" +
                    "		idFuncionario INTEGER PRIMARY KEY, " +
                    "		idFuncionarioTp INTEGER, " +
                    "		nome TEXT, " +
                    "		funcionarioTp TEXT " +
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

    public void insert(TbFuncionario modelo) {
        try {
            db.beginTransaction();
            String insert =
                    "INSERT INTO tbFuncionario ( " +
                            "       idFuncionario, " +
                            "       idFuncionarioTp, " +
                            "		nome, " +
                            "		funcionarioTp " +
                            ") VALUES (?, ?, ?, ?) ";
            db.execSQL(insert, new Object[] {
                    modelo.getIdFuncionario(), modelo.getIdFuncionarioTp(), modelo.getNome(), modelo.getFuncionarioTp()
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


    public TbFuncionario model(Long id) {
        Cursor cursor = null;
        TbFuncionario model = new TbFuncionario();
        try {
            cursor = db.rawQuery(
                    "SELECT * FROM tbFuncionario WHERE idFuncionario = " + id, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                model.setIdFuncionario(cursor.getLong(cursor.getColumnIndex("idFuncionario")));
                model.setIdFuncionarioTp(cursor.getLong(cursor.getColumnIndex("idFuncionarioTp")));
                model.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                model.setFuncionarioTp(cursor.getString(cursor.getColumnIndex("funcionarioTp")));

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

    public List<TbFuncionario> list() {
        Cursor cursor = null;
        List<TbFuncionario> list = new ArrayList<>();

        try {
            cursor = db.rawQuery(
                    "SELECT * FROM tbFuncionario ORDER BY nome", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    TbFuncionario model = new TbFuncionario();
                    model.setIdFuncionario(cursor.getLong(cursor.getColumnIndex("idFuncionario")));
                    model.setIdFuncionarioTp(cursor.getLong(cursor.getColumnIndex("idFuncionarioTp")));
                    model.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                    model.setFuncionarioTp(cursor.getString(cursor.getColumnIndex("funcionarioTp")));
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

    public List<TbFuncionario> listFiltro(String where) {
        Cursor cursor = null;
        List<TbFuncionario> list = new ArrayList<>();

        try {
            cursor = db.rawQuery(
                    "SELECT * FROM tbFuncionario " + where, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    TbFuncionario model = new TbFuncionario();
                    model.setIdFuncionario(cursor.getLong(cursor.getColumnIndex("idFuncionario")));
                    model.setIdFuncionarioTp(cursor.getLong(cursor.getColumnIndex("idFuncionarioTp")));
                    model.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                    model.setFuncionarioTp(cursor.getString(cursor.getColumnIndex("funcionarioTp")));
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
                    "DELETE FROM tbFuncionario WHERE idFuncionario NOT IN (?)";
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

    public void apagarByIdFunc(Long idFunc) {
        try {
            db.beginTransaction();
            String insert =
                    "DELETE FROM tbFuncionario WHERE idFuncionario = ?";
            db.execSQL(insert, new Object[] {
                    idFunc
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
        List<TbFuncionario> list = list();
        array.add("Selecione");
        for (TbFuncionario tbFuncionario : list){
            array.add(tbFuncionario.getNome());
        }

        return array;
    }

    public List<Long> listAdapterId(){
        List<Long> array = new ArrayList<>();
        List<TbFuncionario> list = list();
        array.add(0l);

        for (TbFuncionario tbFuncionario : list){
            array.add(tbFuncionario.getIdFuncionario());
        }
        return array;
    }
}
