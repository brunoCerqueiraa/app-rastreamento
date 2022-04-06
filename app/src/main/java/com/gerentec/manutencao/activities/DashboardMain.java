package com.gerentec.manutencao.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gerentec.manutencao.R;
import com.gerentec.manutencao.auxiliar.Auxiliar;
import com.gerentec.manutencao.auxiliar.BaseDados;
import com.gerentec.manutencao.auxiliar.GPSNew;
import com.gerentec.manutencao.dao.AndLocalizacaoDAO;
import com.gerentec.manutencao.dao.FtSelfieDAO;
import com.gerentec.manutencao.dao.TbCompDAO;
import com.gerentec.manutencao.dao.TbCompEquipDAO;
import com.gerentec.manutencao.dao.TbEquipamentoDAO;
import com.gerentec.manutencao.dao.TbEquipeDAO;
import com.gerentec.manutencao.dao.TbFuncionarioDAO;
import com.gerentec.manutencao.dao.TbSelfieDAO;
import com.gerentec.manutencao.http.HttpFileUploader;
import com.gerentec.manutencao.http.HttpNormalImplementation;
import com.gerentec.manutencao.modelo.AndLocalizacao;
import com.gerentec.manutencao.modelo.FtSelfie;
import com.gerentec.manutencao.modelo.TbComp;
import com.gerentec.manutencao.modelo.TbCompEquip;
import com.gerentec.manutencao.modelo.TbEquipamento;
import com.gerentec.manutencao.modelo.TbEquipe;
import com.gerentec.manutencao.modelo.TbFuncionario;
import com.gerentec.manutencao.modelo.TbSelfie;
import com.gerentec.manutencao.modelo.VwCompEquipamento;
import com.gerentec.manutencao.modelo.VwCompFuncionario;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardMain extends AppCompatActivity implements Runnable{

    Intent intent;
    LinearLayout llGerenciarEquipe, llGerenciarEquipamento, llSelfie;
    TextView tvTitle, tvVersao, tvComposicao, tvComposicaoEquip;
    Button btnEncerrarTurno;

    String encerramentotp = "";
    TbEquipe tbEquipe;
    private Handler handler = new Handler();
    String response = "", requisicao = "";
    TbSelfie tbSelfie;
    public static String latitude, longitude;
    int locationRequestCode = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    public static boolean Gpsnew = false;

    TbCompEquipDAO tbCompEquipDAO = new TbCompEquipDAO(BaseDados.getBD(this));
    TbCompDAO tbCompDAO = new TbCompDAO(BaseDados.getBD(this));

    public void getFormElements(){
        llGerenciarEquipe = findViewById(R.id.llGerenciarEquipe);
        llGerenciarEquipamento = findViewById(R.id.llGerenciarEquipamento);
        tvTitle = findViewById(R.id.tvTitle);
        llSelfie = findViewById(R.id.llSelfie);
        btnEncerrarTurno = findViewById(R.id.btnEncerrarTurno);
        tvVersao = findViewById(R.id.tvVersao);
        tvComposicao = findViewById(R.id.tvComposicao);
        tvComposicaoEquip = findViewById(R.id.tvComposicaoEquip);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_main);

        getFormElements();
        getPreviousIntent();
        initializeLocation();
        preencherForm();

        System.out.println("DATA LOGIN: " + tbEquipe.getDataLogin());

        //requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE);


        llGerenciarEquipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desabilitaForm();
                requisicao = "equipe";
                new Thread(DashboardMain.this).start();
            }
        });

        llGerenciarEquipamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desabilitaForm();
                requisicao = "equipamento";
                new Thread(DashboardMain.this).start();
            }
        });

        llSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TbSelfieDAO tbSelfieDAO = new TbSelfieDAO(BaseDados.getBD(DashboardMain.this));
                    List<TbSelfie> list = tbSelfieDAO.listPendente();
                    if (list.size() > 0) {
                        tbSelfie = list.get(0);
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        File file = new File(BaseDados.getPathFotos(), "temp.jpg");
                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, 1);
                    } else {
                        showMessage("Validação", "Você não possui nenhuma selfie pendente");
                    }
                }
                catch (Exception e){
                    showMessage("Erro", e.toString());
                }
            }
        });

        btnEncerrarTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardMain.this);
                builder.setTitle("Encerrar turno");
                builder.setMessage("Você tem certeza que deseja encerrar o turno?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        desabilitaForm();
                        AndLocalizacaoDAO andLocalizacaoDAO = new AndLocalizacaoDAO(BaseDados.getBD(DashboardMain.this));
                        TbSelfieDAO tbSelfieDAO = new TbSelfieDAO(BaseDados.getBD(DashboardMain.this));
                        requisicao = "encerrarTurno";
                        List<AndLocalizacao> listLocalizacao = andLocalizacaoDAO.listAndLocalizacao(tbEquipe.getIdEquipe());
                        List<TbSelfie> listSelfie = tbSelfieDAO.listPendente();
                        System.out.println("listLocalizacao: " + listLocalizacao.size());
                        System.out.println("listSelfie: " + listSelfie.size());

                        if (listLocalizacao.size() > 20 || listSelfie.size() > 0){
                            //PROCEDIMENTO COM BACKUP
                            encerramentotp = "2";
                            new EnvioBackup().execute();
                        }
                        else{
                            //PROCEDIMENTO NORMAL
                            encerramentotp = "1";
                        }
                        new Thread(DashboardMain.this).start();
                    }
                });
                builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    public void getPreviousIntent(){
        intent = getIntent();
        tbEquipe = (TbEquipe) intent.getSerializableExtra("tbEquipe");
    }

    @Override
    public void run() {
        try {
            Map map = new HashMap();
            if (requisicao.equals("equipe")){
                map.put("r", "getEquipeByIdOficial");
                map.put("idContrato", tbEquipe.getIdContrato());
                map.put("idEquipe", tbEquipe.getIdEquipe());
            }
            else if (requisicao.equals("equipamento")){
                map.put("r", "getEquipamentoByIdOficial");
                map.put("idContrato", tbEquipe.getIdContrato());
                map.put("idEquipe", tbEquipe.getIdEquipe());
            }
            else if (requisicao.equals("encerrarTurno")){
                map.put("r", "encerrarTurno");
                map.put("idEquipe", tbEquipe.getIdEquipe());
                map.put("idTipo", encerramentotp);
            }

            response = HttpNormalImplementation.request(map);
            System.out.println("response: " + response);

            if (response.equals("")){
                message("Verifique sua conexão com a internet!");
                habilitaForm();
            }
            else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TbEquipeDAO tbEquipeDAO = new TbEquipeDAO(BaseDados.getBD(DashboardMain.this));
                            TbFuncionarioDAO tbFuncionarioDAO = new TbFuncionarioDAO(BaseDados.getBD(DashboardMain.this));
                            TbEquipamentoDAO tbEquipamentoDAO = new TbEquipamentoDAO(BaseDados.getBD(DashboardMain.this));
                            if (requisicao.equals("equipe")){
                                tbFuncionarioDAO.apagarBase(0l);
                                VwCompFuncionario[] vetor = new Gson().fromJson(response, VwCompFuncionario[].class);
                                List<VwCompFuncionario> list = Arrays.asList(vetor);
                                processarLista(list);
                                redirectActivity(new Intent(DashboardMain.this, GerenciarEquipe.class), tbEquipe);
                            }
                            else if(requisicao.equals("equipamento")){
                                tbEquipamentoDAO.apagarBase(0l);
                                VwCompEquipamento[] vetor = new Gson().fromJson(response, VwCompEquipamento[].class);
                                List<VwCompEquipamento> list = Arrays.asList(vetor);
                                processarListaEquip(list);
                                redirectActivity(new Intent(DashboardMain.this, GerenciarEquipamento.class), tbEquipe);
                            }
                            else if(requisicao.equals("encerrarTurno")){
                                tbEquipeDAO.limparBase();
                                GPSNew.pausarAsync = true;
                                redirectActivity(new Intent(DashboardMain.this, SplashScreen.class));
                            }
                        }
                        catch (Exception e) {
                            message("Algo deu errado!");
                            habilitaForm();
                        }
                    }
                });
            }
        }
        catch (Exception e) {
            message("Algo deu errado!");
            habilitaForm();
        }
    }

    public void preencherForm(){
        String composicao = "Membros da equipe: \n";
        String composicaoEquip = "Equipamento: \n";

        try {
            List<TbComp> listComp = tbCompDAO.list();
            List<TbCompEquip> listCompEquip = tbCompEquipDAO.list();

            if (listComp.size() > 0) {
                for (TbComp tbComp : listComp) {
                    composicao += tbComp.getNome() + "\n";
                }
            } else {
                composicao += "Nenhum membro adicionado";
            }

            if (listCompEquip.size() > 0) {
                for (TbCompEquip tbCompEquip : listCompEquip) {
                    Double kmDB = Double.parseDouble(tbCompEquip.getKm().toString()) / 10;
                    String km = kmDB.toString().replace(".",",");
                    composicaoEquip += tbCompEquip.getDescricao() + " - KM/HM: " + km + "\n";
                }
            } else {
                composicaoEquip += "Nenhum equipamento adicionado";
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
        }


        try {
            tvTitle.setText("Bem-vindo " + tbEquipe.getNome() + "!");
            tvVersao.setText("Versão: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);

            tvComposicao.setText(composicao);
            tvComposicaoEquip.setText(composicaoEquip);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void message(String texto){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(DashboardMain.this, texto, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void initializeLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(4000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null){
                    return;
                }
                for (Location location : locationResult.getLocations()){
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                    System.out.println("Lat: " + latitude + ", Long:" + longitude);
                }
            }
        };

    }

    public void showMessage(String titulo, String texto) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(DashboardMain.this);
        dialogo.setTitle(titulo);
        dialogo.setMessage(texto);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }

    public void redirectActivity(Intent intent, TbEquipe tbEquipe) {
        intent.putExtra("tbEquipe", tbEquipe);
        startActivity(intent);
        DashboardMain.this.finish();
    }

    public void redirectActivity(Intent intent) {
        startActivity(intent);
        DashboardMain.this.finish();
    }

    private void habilitaForm(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                llGerenciarEquipe.setEnabled(true);
                llGerenciarEquipe.setClickable(true);
            }
        });
    }

    private void desabilitaForm(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                llGerenciarEquipe.setEnabled(false);
                llGerenciarEquipe.setClickable(false);
            }
        });
    }

    public void salvarImagem(Context context, Long idSelfie){
        FtSelfieDAO ftSelfieDAO = new FtSelfieDAO(BaseDados.getBD(context));
        TbSelfieDAO tbSelfieDAO = new TbSelfieDAO(BaseDados.getBD(context));
        try {
            String nomeFoto = idSelfie + "_" + Auxiliar.dataAtualCompleta() + ".jpg";
            File temp = new File(BaseDados.getPathFotos() + "temp.jpg");
            if (temp.exists() && temp.length() > 0) {
                Auxiliar.copiarArquivo(BaseDados.getPathFotos() + "temp.jpg", BaseDados.getPathFotos() + "/" + nomeFoto);
                Auxiliar.ajustarRotacaoImagem(BaseDados.getPathFotos() + "/" + nomeFoto);
                Auxiliar.ajustaFoto(BaseDados.getPathFotos() + "/" + nomeFoto);

                FtSelfie modelo = new FtSelfie();
                modelo.setIdFtSelfie(ftSelfieDAO.generateId());
                modelo.setIdSelfie(idSelfie);
                modelo.setArquivo(nomeFoto);
                modelo.setCoordX(GPSNew.latitude);
                modelo.setCoordY(GPSNew.longitude);
                modelo.setDtInsert(Auxiliar.dataAtualCompleta());
                modelo.setRecebidoServidor("N");

                //tbSelfieDAO.atualizarFinalizado(idSelfie);
                ftSelfieDAO.inserir(modelo);
            }
            else {
                showMessage("Aviso", "Erro");
                System.out.println("Nenhum arquivo encontrado!!");
            }
        }
        catch (Exception e){
            showMessage("Aviso", e.toString());
        }
    }

    public void checkSettingsAndStartLocationUpdates(){
        LocationSettingsRequest request = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);

        locationSettingsResponseTask.addOnSuccessListener(locationSettingsResponse -> {
            startLocationUpdates();
        });

        locationSettingsResponseTask.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException){
                ResolvableApiException api = (ResolvableApiException) e;
                try {
                    api.startResolutionForResult(DashboardMain.this, 1001);
                }
                catch (IntentSender.SendIntentException sendIntentException) {
                    sendIntentException.printStackTrace();
                }
            }
        });
    }

    public void startLocationUpdates(){
        if (checkPermission()) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    public void stopLocationUpdates(){
        if (checkPermission()) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    public void askLocationPermission(){
        if (!(checkPermission())){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                //Mensagem esclarecendo o motivo da permissão de localização
            }
            requestLocationPermission();
        }
    }

    public void requestLocationPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION ,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, locationRequestCode);
    }

    public boolean checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            return false;
        }
    }

    public void processarLista(List<VwCompFuncionario> list){
        TbCompDAO tbCompDAO = new TbCompDAO(BaseDados.getBD(DashboardMain.this));
        TbFuncionarioDAO tbFuncionarioDAO = new TbFuncionarioDAO(BaseDados.getBD(DashboardMain.this));

        for (VwCompFuncionario vwCompFuncionario : list){
            if (vwCompFuncionario.getComp_idFuncionario() == null || vwCompFuncionario.getComp_idFuncionario() == 0){
                //Funcionario
                TbFuncionario tbFuncionario = new TbFuncionario();
                tbFuncionario.setIdFuncionario(vwCompFuncionario.getFunc_idFuncionario());
                tbFuncionario.setIdFuncionarioTp(vwCompFuncionario.getFunc_idFuncionarioTp());
                tbFuncionario.setNome(vwCompFuncionario.getFunc_nome());
                tbFuncionario.setFuncionarioTp(vwCompFuncionario.getFunc_funcionarioTp());
                tbFuncionarioDAO.insert(tbFuncionario);
            }
            else{
                //Comp
                TbComp tbComp = new TbComp();
                tbComp.setIdFuncionario(vwCompFuncionario.getComp_idFuncionario());
                tbComp.setNome(vwCompFuncionario.getComp_nome());
                tbComp.setFuncionarioTp(vwCompFuncionario.getComp_funcionarioTp());
                if (!tbCompDAO.alreadyExists(tbComp)) {
                    tbCompDAO.insert(tbComp);
                } else {
                    tbCompDAO.atualizar(tbComp);
                }

            }
        }
    }

    public void processarListaEquip(List<VwCompEquipamento> list){
        TbCompEquipDAO tbCompEquipDAO = new TbCompEquipDAO(BaseDados.getBD(DashboardMain.this));
        TbEquipamentoDAO tbEquipamentoDAO = new TbEquipamentoDAO(BaseDados.getBD(DashboardMain.this));
        for (VwCompEquipamento vwCompEquipamento : list){
            if (vwCompEquipamento.getComp_idEquipamento() == null || vwCompEquipamento.getComp_idEquipamento() == 0){
                //Equipamento
                TbEquipamento tbEquipamento = new TbEquipamento();
                tbEquipamento.setIdEquipamento(vwCompEquipamento.getEquip_idEquipamento());
                tbEquipamento.setDescricao(vwCompEquipamento.getEquip_descricao());
                tbEquipamento.setTipo(vwCompEquipamento.getEquip_tipo());
                tbEquipamento.setSituacao(vwCompEquipamento.getEquip_situacao());
                tbEquipamentoDAO.insert(tbEquipamento);
            }
            else{
                //CompEquip
                TbCompEquip tbCompEquip = new TbCompEquip();
                tbCompEquip.setIdEquipamento(vwCompEquipamento.getComp_idEquipamento());
                tbCompEquip.setDescricao(vwCompEquipamento.getComp_descricao());
                tbCompEquip.setTipo(vwCompEquipamento.getComp_tipo());
                tbCompEquip.setKm(vwCompEquipamento.getComp_km());
                tbCompEquip.setSitucao(vwCompEquipamento.getComp_situacao());
                if (!tbCompEquipDAO.alreadyExists(tbCompEquip)) {
                    tbCompEquipDAO.insert(tbCompEquip);
                } else {
                    tbCompEquipDAO.atualizar(tbCompEquip);
                }

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkPermission()){
            checkSettingsAndStartLocationUpdates();
        }
        else{
            askLocationPermission();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    @Override
    public void onBackPressed() {
        //Nada acontece
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED){
            //Operação cancelada
        }
        else if (requestCode == 1){
            try {
                salvarImagem(this, tbSelfie.getIdSelfie());
            }
            catch (Exception e){
                e.getStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == locationRequestCode){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Permissão garantida
                checkSettingsAndStartLocationUpdates();
            }
            else{
                //Permissão não garantida
                showMessage("Permissão", "Permissão de localização bloqueada. Sua experiência será reduzida.");
            }
        }
    }

    private class EnvioBackup extends AsyncTask<String, String, String> {

        @SuppressLint("WrongThread")
        protected String doInBackground(String... urls) {

            //envio db para servidor
            String respExport = "";
            try {
                BaseDados.fazerBackUp(getApplicationContext());
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