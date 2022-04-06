package com.gerentec.manutencao.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.gerentec.manutencao.R;
import com.gerentec.manutencao.auxiliar.Auxiliar;
import com.gerentec.manutencao.auxiliar.BaseDados;
import com.gerentec.manutencao.dao.AndLocalizacaoDAO;
import com.gerentec.manutencao.dao.AndTempoParametroDAO;
import com.gerentec.manutencao.dao.FtSelfieDAO;
import com.gerentec.manutencao.dao.TbCompDAO;
import com.gerentec.manutencao.dao.TbCompEquipDAO;
import com.gerentec.manutencao.dao.TbEquipamentoDAO;
import com.gerentec.manutencao.dao.TbEquipeDAO;
import com.gerentec.manutencao.dao.TbFuncionarioDAO;
import com.gerentec.manutencao.dao.TbSelfieDAO;
import com.gerentec.manutencao.http.HttpNormalImplementation;
import com.gerentec.manutencao.modelo.AndAcesso;
import com.gerentec.manutencao.modelo.AndTempoParametro;
import com.gerentec.manutencao.modelo.TbEquipe;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    private int splashScreenDuration = 5000;
    Animation topAnim;
    ImageView ivLogo;

    AndAcesso andAcesso = new AndAcesso();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        criacaoDatabase();
        final TbEquipeDAO tbEquipeDAO = new TbEquipeDAO(BaseDados.getBD(this));

        new SplashScreen.AsyncTask().execute();

        ivLogo = findViewById(R.id.ivLogo);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        ivLogo.setAnimation(topAnim);

        try {
            //Informações para AndAcesso
            String vName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            int vCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            String bateria = String.valueOf(Auxiliar.nivelBateria(this));

            //Gps
            String gps;
            try {
                if (Auxiliar.gpsHabilitado(this)) {
                    gps = "S";
                }
                else {
                    gps = "N";
                }
            }
            catch (Exception e) {
                gps = "E";
            }

            andAcesso.setvCode(Long.parseLong(String.valueOf(vCode)));
            andAcesso.setvName(vName);
            andAcesso.setGps(gps);
            andAcesso.setBateria(Long.parseLong(bateria));
            andAcesso.setApp("Manutencao");
        }
        catch (Exception e){
            e.toString();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tbEquipeDAO.existeEquipeBySituacao("A")){
                    TbEquipe tbEquipe = tbEquipeDAO.getModelo();
                    Intent intent = new Intent(SplashScreen.this, LoadingScreen.class);
                    intent.putExtra("tbEquipe", tbEquipe);

                    andAcesso.setIdEquipe(tbEquipe.getIdEquipe());
                    andAcesso.setImei(tbEquipe.getImei());
                    intent.putExtra("andAcesso", andAcesso);
                    startActivity(intent);

                }
                else{
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    intent.putExtra("andAcesso", andAcesso);
                    startActivity(intent);
                }
                SplashScreen.this.finish();
            }
        }, splashScreenDuration);
    }

    public void criacaoDatabase() {
        AndLocalizacaoDAO andLocalizacaoDAO = new AndLocalizacaoDAO(BaseDados.getBD(this));
        AndTempoParametroDAO andTempoParametroDAO = new AndTempoParametroDAO(BaseDados.getBD(this));
        TbCompDAO tbCompDAO = new TbCompDAO(BaseDados.getBD(this));
        TbCompEquipDAO tbCompEquipDAO = new TbCompEquipDAO(BaseDados.getBD(this));
        TbEquipeDAO tbEquipeDAO = new TbEquipeDAO(BaseDados.getBD(this));
        TbFuncionarioDAO tbRetornoTpDAO = new TbFuncionarioDAO(BaseDados.getBD(this));
        TbSelfieDAO tbSelfieDAO = new TbSelfieDAO(BaseDados.getBD(this));
        FtSelfieDAO ftSelfieDAO = new FtSelfieDAO(BaseDados.getBD(this));
        TbEquipamentoDAO tbEquipamentoDAO = new TbEquipamentoDAO(BaseDados.getBD(this));

        andLocalizacaoDAO.createTable();
        andTempoParametroDAO.createTable();
        tbCompDAO.createTable();
        tbEquipeDAO.createTable();
        tbRetornoTpDAO.createTable();
        tbSelfieDAO.createTable();
        ftSelfieDAO.createTable();
        tbEquipamentoDAO.createTable();
        tbCompEquipDAO.createTable();
    }

    //Classe para tratar a consulta à servlet sem usar a thread principal
    private class AsyncTask extends android.os.AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            String response = "";

            final TbEquipeDAO tbEquipeDAO = new TbEquipeDAO(BaseDados.getBD(getApplicationContext()));
            AndTempoParametroDAO andTempoParametroDAO = new AndTempoParametroDAO(BaseDados.getBD(getApplicationContext()));

            try {
                try {
                    Map map2 = new HashMap();
                    map2.put("r", "listAndTempoParametro");

                    response = HttpNormalImplementation.request(map2);
                    AndTempoParametro[] vetor = new Gson().fromJson(response, AndTempoParametro[].class);
                    List<AndTempoParametro> list = Arrays.asList(vetor);
                    for (AndTempoParametro andTempoParametro : list) {
                        if (!andTempoParametroDAO.alreadyExists(andTempoParametro)) {
                            andTempoParametroDAO.insert(andTempoParametro);
                        }
                        else {
                            andTempoParametroDAO.atualizar(andTempoParametro);
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println("[SplashScreenActivity] Error in listAndTempoParametro: " + e.toString());
                }

                //Backup BD example
//                if (tbEquipeDAO.existeEquipeBySituacao("A")){
//                    TbEquipe tbEquipe = tbEquipeDAO.getModelo();
//                    String respExport = "";
//                    try {
//                        if (android.os.Build.VERSION.SDK_INT > 9) {
//                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//                            StrictMode.setThreadPolicy(policy);
//                        }
//                        respExport = new HttpFileUploader().enviarBD(BaseDados.getPathDb(), tbEquipe);
//                        System.out.println("Response BD backup: " + respExport);
//                    }
//                    catch (Exception e) {
//                        System.out.println(e.toString());
//                        e.printStackTrace();
//                    }
//                }
//                else{
//                    //Não gere o backup
//                }
            }
            catch (Exception e) {
                System.out.println("[SplashScreenActivity] Error in doInBackground: " + e.toString());
            }
            return null;
        }
    }
}
