package com.gerentec.manutencao.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gerentec.manutencao.R;
import com.gerentec.manutencao.auxiliar.BaseDados;
import com.gerentec.manutencao.dao.TbEquipeDAO;
import com.gerentec.manutencao.http.HttpNormalImplementation;
import com.gerentec.manutencao.modelo.AndAcesso;
import com.gerentec.manutencao.modelo.TbEquipe;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements Runnable {

    TextInputEditText tiLogin, tiSenha;
    TextView tvMensagemErro;
    Button btnEntrar;

    String response = "";
    private Handler handler = new Handler();
    TbEquipe tbEquipe = null;
    AndAcesso andAcesso = null;
    TbEquipeDAO tbEquipeDAO = new TbEquipeDAO(BaseDados.getBD(this));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tiLogin = findViewById(R.id.tiLogin);
        tiSenha = findViewById(R.id.tiSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        tvMensagemErro = findViewById(R.id.tvMensagemErro);

        Intent intent = getIntent();
        andAcesso = (AndAcesso) intent.getSerializableExtra("andAcesso");

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarLogin();
            }
        });
    }
    public void verificarLogin() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                System.out.println("GPS TRUE");
                btnEntrar.setEnabled(false);
                btnEntrar.setClickable(false);

                new Thread(this).start();
            } else {
                System.out.println("GPS FALSE");
                showMessage("Validação", "Habilite o GPS para prosseguir!");
            }
        }
        catch (Exception e){
            showMessage("Erro", "Algo deu errado");
        }


    }

    @Override
    public void run() {
        try {
            String login = tiLogin.getText().toString().trim();
            String password = tiSenha.getText().toString().trim();

            System.out.println(andAcesso.toString());

            Map map = new HashMap();
            map.put("r", "newTbEquipe");
            map.put("login", login);
            map.put("password", password);
            map.put("vCode", String.valueOf(andAcesso.getvCode()));
            map.put("vName", andAcesso.getvName());
            map.put("gps", andAcesso.getGps());
            map.put("app", andAcesso.getApp());
            map.put("bateria", andAcesso.getBateria());

            response = HttpNormalImplementation.request(map);
            System.out.println("response: " + response);
            if (response.equals("incorreto")){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(Login.this, "Usuário/Senha incorreto(s)!", Toast.LENGTH_SHORT);
                        toast.show();
                        atualizaBotao();
                    }
                });
            }
            else if (response.equals("")){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(Login.this, "Verifique sua conexão com a internet!", Toast.LENGTH_SHORT);
                        toast.show();
                        atualizaBotao();
                    }
                });
            }
            else{
                tbEquipe = new Gson().fromJson(response, TbEquipe.class);
                System.out.println(tbEquipe.toString());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (tbEquipe.getIdEquipe() != null) {
                                tbEquipeDAO.insert(tbEquipe);
                                redirectActivity(tbEquipe);
                            }
                            else {
                                //Sla
                            }
                        }
                        catch (Exception e){
                            //Sla
                        }
                    }
                });
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    atualizaBotao();
                    Toast toast = Toast.makeText(Login.this, "Algo deu errado", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }

    private void atualizaBotao(){
        btnEntrar.setEnabled(true);
        btnEntrar.setClickable(true);
    }

    private void redirectActivity(TbEquipe tbEquipe) {
        BaseDados.fazerBackUp(this);
        Intent intent1 = new Intent(Login.this, LoadingScreen.class);
        intent1.putExtra("tbEquipe", tbEquipe);
        intent1.putExtra("andAcesso", andAcesso);
        startActivity(intent1);
        Login.this.finish();
    }

    public void showMessage(String titulo, String texto){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(Login.this);
        dialogo.setTitle(titulo);
        dialogo.setMessage(texto);
        dialogo.setNeutralButton("OK", null);
        AlertDialog alert = dialogo.create();
        alert.show();
    }
}