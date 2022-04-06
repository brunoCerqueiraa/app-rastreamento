package com.gerentec.manutencao.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gerentec.manutencao.R;
import com.gerentec.manutencao.auxiliar.BaseDados;
import com.gerentec.manutencao.auxiliar.GPSNew;
import com.gerentec.manutencao.auxiliar.ProgressBarAnimation;
import com.gerentec.manutencao.bo.BaseDadosBO;
import com.gerentec.manutencao.http.HttpNormalImplementation;
import com.gerentec.manutencao.modelo.AndAcesso;
import com.gerentec.manutencao.modelo.TbEquipe;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LoadingScreen extends AppCompatActivity {

    ProgressBar progressBar;
    TextView tvPorcentagem;
    TbEquipe tbEquipe = null;
    AndAcesso andAcesso = null;
    TextView textView3;
    String tela = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        tvPorcentagem = findViewById(R.id.tvPorcentagem);
        textView3 = findViewById(R.id.textView3);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        tbEquipe = (TbEquipe) intent.getSerializableExtra("tbEquipe");
        andAcesso = (AndAcesso) intent.getSerializableExtra("andAcesso");
        tela = intent.getStringExtra("tela");

        BaseDados.fazerBackUp(this);
        File dir = new File(BaseDados.getPathFotos());
        dir.mkdirs();
        dir.mkdirs();

        progressBar = findViewById(R.id.progress_bar);

        Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setProgressDrawable(progressDrawable);

        progressBar.setMax(100);
        progressBar.setScaleY(3f);
        progressAnimation();
    }

    public void progressAnimation(){
        ProgressBarAnimation progressBarAnimation = new ProgressBarAnimation(this, progressBar, tvPorcentagem, 0f, 100f, tbEquipe, tela);
        if (tela != null && tela.equals("dashboard")) {
            textView3.setText("Carregando Equipes. Aguarde...");
            progressBarAnimation.setDuration(5000);
        }

        else {
            textView3.setText("Sincronizando dados com o servidor. Aguarde...");
            BaseDadosBO baseDadosBO = new BaseDadosBO(this, tbEquipe);
            BaseDadosBO.gps = true;
            GPSNew.pausarAsync = false;
            baseDadosBO.ativaGps();
            baseDadosBO.requisicaoCoordenada();
            baseDadosBO.realizarRequisicoes();

            progressBarAnimation.setDuration(5000);
            new asyncTask().execute();

        }
        progressBar.setAnimation(progressBarAnimation);
    }

    //Classe para tratar a consulta à servlet sem usar a thread principal
    private class asyncTask extends android.os.AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            String response = "";

            try {
                try {
                    andAcesso.setIdEquipe(tbEquipe.getIdEquipe());
                    andAcesso.setImei(tbEquipe.getImei());

                    Map map2 = new HashMap();
                    map2.put("r", "andAcessoTbEquipe");
                    map2.put("andAcesso", new Gson().toJson(andAcesso));
                    response = HttpNormalImplementation.request(map2);
                    System.out.println(response);
                }
                catch (Exception e) {
                    System.out.println("[SplashScreenActivity] Error in listAndLocalizacaoParam: " + e.toString());
                }
            }
            catch (Exception e) {
                System.out.println("[SplashScreenActivity] Error in doInBackground: " + e.toString());
            }
            return null;
        }
    }
}

