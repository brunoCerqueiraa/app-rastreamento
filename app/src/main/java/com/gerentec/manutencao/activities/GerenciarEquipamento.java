package com.gerentec.manutencao.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gerentec.manutencao.R;
import com.gerentec.manutencao.auxiliar.BaseDados;
import com.gerentec.manutencao.dao.TbCompEquipDAO;
import com.gerentec.manutencao.http.HttpNormalImplementation;
import com.gerentec.manutencao.modelo.TbComp;
import com.gerentec.manutencao.modelo.TbCompEquip;
import com.gerentec.manutencao.modelo.TbEquipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GerenciarEquipamento extends AppCompatActivity implements Runnable {

    Intent intent;
    Button btnAddEquipamento;
    ListView lvEquipamento;

    TbEquipe tbEquipe;
    TbCompEquip tbCompEquip;
    String response = "";
    private Handler handler = new Handler();
    TbCompEquipDAO tbCompEquipDAO = new TbCompEquipDAO(BaseDados.getBD(this));

    public void getFormElements(){
        btnAddEquipamento = findViewById(R.id.btnAddEquipamento);
        lvEquipamento = findViewById(R.id.lvEquipamento);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_equipamento);

        getPreviousIntent();
        getFormElements();

        List<TbCompEquip> listCompEquip = tbCompEquipDAO.list();
        MyAdapter adapterList = new MyAdapter(this, listCompEquip);
        lvEquipamento.setAdapter(adapterList);

        btnAddEquipamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listCompEquip.size() > 0 ){
                    showMessage("Validação", "Você não pode adicionar mais equipamentos");
                }
                else{
                    redirectActivity(new Intent(GerenciarEquipamento.this, AdicionarEquipamento.class), tbEquipe);
                }
            }
        });

        lvEquipamento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tbCompEquip = listCompEquip.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(GerenciarEquipamento.this);
                builder.setTitle("Retirar equipamento");
                builder.setMessage("Você tem certeza que retirar " + tbCompEquip.getDescricao() + " de sua equipe?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(GerenciarEquipamento.this).start();
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

    @Override
    public void run() {
        try {
            Map map = new HashMap();
            map.put("r", "retirarEquipamento");
            map.put("idEquipe", tbEquipe.getIdEquipe());
            map.put("idEquipamento", tbCompEquip.getIdEquipamento());

            response = HttpNormalImplementation.request(map);
            System.out.println("response: " + response);

            if (response.equals("")){
                message("Verifique sua conexão com a internet!");
            }
            else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (response.equals("OK")){
                                tbCompEquipDAO.apagarEquipamento(tbCompEquip.getIdEquipamento());
                                redirectActivity(new Intent(GerenciarEquipamento.this, DashboardMain.class), tbEquipe);
                            }
                            else{
                                message("Não foi possível realizar a exclusão!");
                            }
                        }
                        catch (Exception e){
                            message("Verifique sua conexão com a internet!");
                        }
                    }
                });
            }
        }
        catch (Exception e) {
            showMessage("Conexão", "Verifique sua conexão com a internet!");
        }
    }

    public void getPreviousIntent(){
        intent = getIntent();
        tbEquipe = (TbEquipe) intent.getSerializableExtra("tbEquipe");
    }

    public void message(String texto){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(GerenciarEquipamento.this, texto, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void redirectActivity(Intent intent, TbEquipe tbEquipe) {
        intent.putExtra("tbEquipe", tbEquipe);
        startActivity(intent);
        GerenciarEquipamento.this.finish();
    }

    public void redirectActivity(Intent intent, TbEquipe tbEquipe, String dashboard) {
        intent.putExtra("tbEquipe", tbEquipe);
        intent.putExtra("tela", dashboard);
        startActivity(intent);
        GerenciarEquipamento.this.finish();
    }


    public void showMessage(String titulo, String texto) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(GerenciarEquipamento.this);
        dialogo.setTitle(titulo);
        dialogo.setMessage(texto);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }

    class MyAdapter extends ArrayAdapter<TbCompEquip> {
        Context context;
        List<TbCompEquip> vecTbCompEquip;

        MyAdapter(Context context, List<TbCompEquip> vecTbCompEquip){
            super(context, R.layout.list_equipe_adapter, vecTbCompEquip);

            this.context = context;
            this.vecTbCompEquip = vecTbCompEquip;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View styleListView = layoutInflater.inflate(R.layout.list_equipe_adapter, parent, false);

            TbCompEquip tbCompEquip = tbCompEquipDAO.model(vecTbCompEquip.get(position).getIdEquipamento());

            TextView tvNome = styleListView.findViewById(R.id.tvNome);
            TextView tvTipo = styleListView.findViewById(R.id.tvTipo);

            tvNome.setText(tbCompEquip.getDescricao());
            tvTipo.setText(tbCompEquip.getTipo());

            return styleListView;
        }
    }

    @Override
    public void onBackPressed() {
        redirectActivity(new Intent(GerenciarEquipamento.this, DashboardMain.class), tbEquipe);
    }
}