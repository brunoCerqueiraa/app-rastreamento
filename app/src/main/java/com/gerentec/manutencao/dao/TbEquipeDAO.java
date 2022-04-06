package com.gerentec.manutencao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gerentec.manutencao.modelo.TbEquipe;

public class TbEquipeDAO {

    private static SQLiteDatabase db;

    public TbEquipeDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public void createTable() {
        try {
            db.beginTransaction();

            String sql ="CREATE TABLE IF NOT EXISTS tbEquipe (" +
                    "		idEquipe INTEGER PRIMARY KEY, " +
                    "		nome TEXT, " +
                    "		situacao TEXT, " +
                    "		login TEXT, " +
                    "		senha TEXT, " +
                    "		idContrato INTEGER, " +
                    "		idFuncionario INTEGER, " +
                    "		imei TEXT, " +
                    "		dataLogin TEXT " +
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

    public void insert(TbEquipe tbEquipe) {
        try {
            db.beginTransaction();
            String insert =
                    "INSERT INTO tbEquipe ( " +
                            "		idEquipe, " +
                            "		nome, " +
                            "		situacao, " +
                            "		login, " +
                            "		senha, " +

                            "		idContrato, " +
                            "		idFuncionario, " +
                            "		imei, " +
                            "		dataLogin " +
                            ") VALUES ( " +
                            "?,?,?,?,?," +
                            "?,?,?,?" +
                            ") ";
            db.execSQL(insert, new Object[] {
                    tbEquipe.getIdEquipe(),
                    tbEquipe.getNome(),
                    tbEquipe.getSituacao(),
                    tbEquipe.getLogin(),
                    tbEquipe.getSenha(),

                    tbEquipe.getIdContrato(),
                    tbEquipe.getIdFuncionario(),
                    tbEquipe.getImei(),
                    tbEquipe.getDataLogin()
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

    public boolean alreadyExists(TbEquipe tbEquipe){
        Cursor cursor = null;
        try {
            String sql = "SELECT count(*) AS qtde FROM tbEquipe WHERE idEquipe = ?";
            cursor = db.rawQuery(sql, new String[]{tbEquipe.getIdEquipe().toString()});
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

    public TbEquipe getModelo() {
        Cursor cursor = null;
        TbEquipe tbEquipe = null;

        try {
            cursor = db.rawQuery(
                    "SELECT * FROM tbEquipe WHERE situacao = 'A'", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                tbEquipe = new TbEquipe();
                tbEquipe.setIdEquipe(cursor.getLong(cursor.getColumnIndex("idEquipe")));
                tbEquipe.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                tbEquipe.setSituacao(cursor.getString(cursor.getColumnIndex("situacao")));
                tbEquipe.setLogin(cursor.getString(cursor.getColumnIndex("login")));
                tbEquipe.setSenha(cursor.getString(cursor.getColumnIndex("senha")));
                tbEquipe.setIdContrato(cursor.getLong(cursor.getColumnIndex("idContrato")));
                tbEquipe.setIdFuncionario(cursor.getLong(cursor.getColumnIndex("idFuncionario")));
                tbEquipe.setImei(cursor.getString(cursor.getColumnIndex("imei")));
                tbEquipe.setDataLogin(cursor.getString(cursor.getColumnIndex("dataLogin")));

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
        return tbEquipe;
    }


    public boolean existeEquipeBySituacao(String situacao) {
        boolean resposta;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT count(*) AS qtde FROM tbEquipe WHERE situacao = '" + situacao + "' ", null);
            int countIndex = cursor.getColumnIndex("qtde");
            cursor.moveToFirst();
            int rowCount = cursor.getInt(countIndex);

            if (rowCount > 0) {
                resposta = true;
            }
            else{
                resposta = false;
            }
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return resposta;
    }

    public void excluirTbEquipe(Long idEquipe) {
        try {
            db.beginTransaction();
            String sql = "DELETE FROM tbEquipe WHERE idEquipe = "  + idEquipe;
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void limparBase() {
        limparAndLocalizacao();
        limparFtSelfie();
        limparTbComp();
        limparTbCompEquip();
        limparTbEquipamento();
        limparTbEquipe();
        limparTbFuncionario();
        limparTbSelfie();
    }

    public void limparAndLocalizacao() {
        try {
            db.beginTransaction();
            String sql = "DELETE FROM tbAndLocalizacao";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void limparFtSelfie() {
        try {
            db.beginTransaction();
            String sql = "DELETE FROM ftSelfie";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void limparTbComp() {
        try {
            db.beginTransaction();
            String sql = "DELETE FROM tbComp";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void limparTbCompEquip() {
        try {
            db.beginTransaction();
            String sql = "DELETE FROM tbCompEquip";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void limparTbEquipamento() {
        try {
            db.beginTransaction();
            String sql = "DELETE FROM tbEquipamento";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void limparTbEquipe() {
        try {
            db.beginTransaction();
            String sql = "DELETE FROM tbEquipe";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void limparTbFuncionario() {
        try {
            db.beginTransaction();
            String sql = "DELETE FROM tbFuncionario";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void limparTbSelfie() {
        try {
            db.beginTransaction();
            String sql = "DELETE FROM tbSelfie";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
