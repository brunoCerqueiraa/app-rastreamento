package com.gerentec.manutencao.bo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.AutoCompleteTextView;

import com.gerentec.manutencao.activities.DashboardMain;
import com.gerentec.manutencao.activities.GerenciarEquipamento;
import com.gerentec.manutencao.activities.GerenciarEquipe;
import com.gerentec.manutencao.activities.Login;
import com.gerentec.manutencao.activities.SplashScreen;
import com.gerentec.manutencao.auxiliar.Auxiliar;
import com.gerentec.manutencao.auxiliar.BaseDados;
import com.gerentec.manutencao.auxiliar.GPSNew;
import com.gerentec.manutencao.dao.AndLocalizacaoDAO;
import com.gerentec.manutencao.dao.AndTempoParametroDAO;
import com.gerentec.manutencao.dao.FtSelfieDAO;
import com.gerentec.manutencao.dao.TbEquipeDAO;
import com.gerentec.manutencao.dao.TbSelfieDAO;
import com.gerentec.manutencao.http.HttpFileUploader;
import com.gerentec.manutencao.http.HttpNormalImplementation;
import com.gerentec.manutencao.modelo.AndLocalizacao;
import com.gerentec.manutencao.modelo.AndTempoParametro;
import com.gerentec.manutencao.modelo.FtSelfie;
import com.gerentec.manutencao.modelo.TbEquipe;
import com.gerentec.manutencao.modelo.TbSelfie;
import com.gerentec.manutencao.modelo.VwCompEquipamento;
import com.gerentec.manutencao.modelo.VwCompFuncionario;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BaseDadosBO {
    private static final Handler handler = new Handler();
    public static Context context = null;
    private TbEquipe tbEquipe;
    String pathFoto = BaseDados.getPathFotos();
    public static boolean gps = false;
    public static boolean deslogar = false;

    public BaseDadosBO(Context context, TbEquipe tbEquipe) {
        this.context = context;
        this.tbEquipe = tbEquipe;
    }

    public void realizarRequisicoes() {
        Timer timer = new Timer();
        TimerTask taskEnvio = new TimerTask(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!GPSNew.pausarAsync){
                            new requisicoes().execute();
                        }
                        else{
                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }
        };
        timer.schedule(taskEnvio, 600, 60000);
        if (GPSNew.pausarAsync){
            timer.cancel();
            timer.purge();
        }
    }


    public void requisicaoCoordenada() {
        Timer timer = new Timer();
        AndTempoParametroDAO andTempoParametroDAO = new AndTempoParametroDAO(BaseDados.getBD(context));
        Long tempo = andTempoParametroDAO.getTempoByTipo("coordenada");
        if (tempo == null || tempo == 0){
            tempo = 120l;
        }

        TimerTask taskAndLocalizacaoParam = new TimerTask(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!GPSNew.pausarAsync){
                            new andLocalizacaoStart().execute();
                        }
                        else{
                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }
        };
        timer.schedule(taskAndLocalizacaoParam, 600, tempo * 1000);
    }

    public void ativaGps() {
        Timer timer = new Timer();
        TimerTask taskGps = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Pausar: " + GPSNew.pausarAsync);
                        if (!GPSNew.pausarAsync) {
                            new gpsManager().execute();
                        }
                        else{
                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }
        };
        timer.schedule(taskGps, 150, 15000);

    }

    private class gpsManager extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (gps) {
                    iniciarGps();
                    gps = false;
                } else {
                    //Gps não iniciado
                }
            } catch (Exception e) {
                System.out.println("[gpsManager] Error in doInBackground: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (GPSNew.contador > 15) {
                //GPS desligado
                //System.exit(0);
            } else {
                //GPS ligado
            }
        }

        public void iniciarGps() {
            try {
                context.startService((new Intent(context, GPSNew.class)));
            } catch (Exception e) {
                System.out.println("[gpsManager] Error in iniciarGps: " + e.toString());
            }
        }
    }

    private class requisicoes extends AsyncTask<Void, Void, Void> {
        TbEquipe tbEquipeAtt;
        TbEquipeDAO tbEquipeDAO = new TbEquipeDAO(BaseDados.getBD(context));

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("Requisições");
            String response = "";
            TbSelfieDAO tbSelfieDAO = new TbSelfieDAO(BaseDados.getBD(context));
            FtSelfieDAO ftSelfieDAO = new FtSelfieDAO(BaseDados.getBD(context));

            tbEquipe = tbEquipeDAO.getModelo();

            //VERIFICAR SE O USUARIO ESTÁ LOGADO

            //Traz informações da TB_SELFIE
            try {
                Map map = new HashMap();
                map.put("r", "listTbSelfie");
                map.put("idEquipe", tbEquipe.getIdEquipe());

                response = HttpNormalImplementation.request(map);
                TbSelfie[] vetor = new Gson().fromJson(response, TbSelfie[].class);
                List<TbSelfie> list = Arrays.asList(vetor);
                for (TbSelfie model : list) {
                    if (!tbSelfieDAO.alreadyExists(model)) {
                        tbSelfieDAO.insert(model);
                    } else {
                        tbSelfieDAO.atualizar(model);
                    }
                }
            }
            catch (Exception e){
                System.out.println(e.toString());
            }

            //Envio fotos
            try {
                List<FtSelfie> list = ftSelfieDAO.listParaEnviar();
                if (list.size() > 0) {
                    for (FtSelfie ftSelfie : list) {
                        response = new HttpFileUploader().enviarFt(pathFoto + ftSelfie.getArquivo(), ftSelfie);
                        System.out.println("Foto: " + response);
                        FtSelfie retorno = new Gson().fromJson(response, FtSelfie.class);
                        ftSelfieDAO.atualizarRecebidoServidor(retorno);
                    }
                }
            }
            catch (Exception e) {
                System.out.println(e.toString());
            }

            System.out.println("Fim Requisições");
            return null;
        }
    }

    private class andLocalizacaoStart extends AsyncTask<Void, Void, Void>{
        private boolean trocaAct = false;
        TbEquipeDAO tbEquipeDAO = new TbEquipeDAO(BaseDados.getBD(context));

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            tbEquipe = tbEquipeDAO.getModelo();
            try {
                String response;
                AndLocalizacaoDAO andLocalizacaoDAO = new AndLocalizacaoDAO(BaseDados.getBD(context));
                TbEquipeDAO tbEquipeDAO = new TbEquipeDAO(BaseDados.getBD(context));
                AndLocalizacao model = new AndLocalizacao();
                model.setIdAndLocalizacao(andLocalizacaoDAO.generateIdAndLocalizacao());
                model.setIdEquipe(tbEquipe.getIdEquipe());
                model.setCoordX(GPSNew.latitude);
                model.setCoordY(GPSNew.longitude);
                model.setDtCel(Auxiliar.dataAtualCompleta());
                model.setRecebidoServidor("N");
                andLocalizacaoDAO.insert(model);

                List<AndLocalizacao> listAndLocalizacao = andLocalizacaoDAO.listAndLocalizacao(tbEquipe.getIdEquipe());
                if (listAndLocalizacao.size() > 0) {
                    Map mapAndLocalizacao = new HashMap();
                    mapAndLocalizacao.put("r", "listAndLocalizacao");
                    mapAndLocalizacao.put("idFuncionario", tbEquipe.getIdFuncionario());
                    mapAndLocalizacao.put("idEquipe", tbEquipe.getIdEquipe());
                    mapAndLocalizacao.put("data", tbEquipe.getDataLogin());
                    mapAndLocalizacao.put("listAndLocalizacao", new Gson().toJson(listAndLocalizacao));
                    try {
                        response = HttpNormalImplementation.request(mapAndLocalizacao);
                        System.out.println("response: " + response);

                        if (response.equals("OK")) {
                            andLocalizacaoDAO.atualizarAndLocalizacaoRecebidoServidor(listAndLocalizacao);
                        }
                        else if (response.equals("DESLOGAR") || response.equals("DESLOGARDATA") || deslogar ){
                            try {
                                Map map = new HashMap();
                                map.put("r", "encerrarTurno");
                                map.put("idEquipe", tbEquipe.getIdEquipe());
                                if (response.equals("DESLOGAR")){
                                    map.put("idTipo", "3");
                                }
                                else if(response.equals("DESLOGARDATA")){
                                    map.put("idTipo", "4");
                                }
                                response = HttpNormalImplementation.request(map);
                                System.out.println("response: " + response);

                                if (response.equals("")){
                                    deslogar = true;
                                }
                                else {
                                    System.out.println("SYSTEM EXIT");
                                    new EnvioBackup().execute();
                                    tbEquipeDAO.limparBase();
                                    trocaAct = true;
                                }
                            }
                            catch (Exception e) {
                                System.out.println("Something was gone wrong");
                            }
                        }
                        else{
                            //A servlet retornou algum erro
                        }
                    }
                    catch (JsonParseException | IOException e) {
                        System.out.println("[andLocalizacaoParamStart] Error in listAndLocalizacao: " + e.toString());
                    }
                }
                else{
                    //Não retornou nada
                }
            }
            catch (Exception e){
                System.out.println("[andLocalizacaoParamStart] Error in doInBackground: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (trocaAct){
                GPSNew.pausarAsync = true;
                Intent intent = new Intent(context, SplashScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        }
    }

    private class EnvioBackup extends AsyncTask<String, String, String> {
        TbEquipeDAO tbEquipeDAO = new TbEquipeDAO(BaseDados.getBD(context));

        @SuppressLint("WrongThread")
        protected String doInBackground(String... urls) {
            tbEquipe = tbEquipeDAO.getModelo();
            //envio db para servidor
            String respExport = "";
            try {
                BaseDados.fazerBackUp(context);
                respExport = new HttpFileUploader().enviarBD(BaseDados.getPathDb(), tbEquipe);
                System.out.println("envioBDServidor=" + respExport);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.toString());
            }

            return respExport;
        }
    }
}
