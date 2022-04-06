package com.gerentec.manutencao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gerentec.manutencao.modelo.TbComp;
import com.gerentec.manutencao.modelo.TbFuncionario;

import java.util.ArrayList;
import java.util.List;

public class TbCompDAO {

    private static SQLiteDatabase db;

    public TbCompDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public void createTable() {
        try {
            db.beginTransaction();
            String sql ="CREATE TABLE IF NOT EXISTS tbComp (" +
                    "		idFuncionario INTEGER PRIMARY KEY, " +
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

    public void insert(TbComp modelo) {
        try {
            db.beginTransaction();
            String insert =
                    "INSERT INTO tbComp ( " +
                            "       idFuncionario, " +
                            "		nome, " +
                            "		funcionarioTp " +
                            ") VALUES ( ?, ?, ?) ";
            db.execSQL(insert, new Object[] {
                    modelo.getIdFuncionario(), modelo.getNome(), modelo.getFuncionarioTp()
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

    public void atualizar(TbComp tbComp){
        try {
            db.beginTransaction();
            String insert =
                    "UPDATE tbComp SET nome=?, funcionarioTp=? WHERE idFuncionario=? ";
            db.execSQL(insert, new Object[] {
                    tbComp.getNome(), tbComp.getFuncionarioTp(), tbComp.getIdFuncionario()
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

    public boolean alreadyExists(TbComp tbComp){
        Cursor cursor = null;
        try {
            String sql = "SELECT count(*) AS qtde FROM tbComp WHERE idFuncionario = ?";
            cursor = db.rawQuery(sql, new String[]{
                    tbComp.getIdFuncionario().toString()
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

    public TbComp model(Long id) {
        Cursor cursor = null;
        TbComp model = new TbComp();
        try {
            cursor = db.rawQuery(
                    "SELECT * FROM tbComp WHERE idFuncionario = " + id, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                model.setIdFuncionario(cursor.getLong(cursor.getColumnIndex("idFuncionario")));
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

    public List<TbComp> list() {
        Cursor cursor = null;
        List<TbComp> list = new ArrayList<>();

        try {
            cursor = db.rawQuery(
                    "SELECT * FROM tbComp ORDER BY nome", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    TbComp model = new TbComp();
                    model.setIdFuncionario(cursor.getLong(cursor.getColumnIndex("idFuncionario")));
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
                    "DELETE FROM tbComp WHERE idFuncionario NOT IN (?)";
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

    public void apagarFuncionario(Long idFuncionario) {
        try {
            db.beginTransaction();
            String insert =
                    "DELETE FROM tbComp WHERE idFuncionario = ?";
            db.execSQL(insert, new Object[] {
                    idFuncionario
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
        List<TbComp> list = list();
        array.add("Selecione");
        for (TbComp tbComp : list){
            array.add(tbComp.getNome());
        }

        return array;
    }

    public List<Long> listAdapterId(){
        List<Long> array = new ArrayList<>();
        List<TbComp> list = list();
        array.add(0l);

        for (TbComp tbComp : list){
            array.add(tbComp.getIdFuncionario());
        }
        return array;
    }
}
