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
import com.gerentec.manutencao.dao.TbCompDAO;
import com.gerentec.manutencao.http.HttpNormalImplementation;
import com.gerentec.manutencao.modelo.TbComp;
import com.gerentec.manutencao.modelo.TbEquipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GerenciarEquipe extends AppCompatActivity implements Runnable {
    Intent intent;
    Button btnAddMembro;
    ListView lvEquipe;

    TbEquipe tbEquipe;
    TbComp tbComp;
    String response = "";
    private Handler handler = new Handler();
    TbCompDAO tbCompDAO = new TbCompDAO(BaseDados.getBD(this));


    public void getFormElements(){
        btnAddMembro = findViewById(R.id.btnAddMembro);
        lvEquipe = findViewById(R.id.lvEquipe);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_equipe);

        getFormElements();
        getPreviousIntent();

        List<TbComp> listComp = tbCompDAO.list();
        MyAdapter adapterList = new MyAdapter(this, listComp);
        lvEquipe.setAdapter(adapterList);

        btnAddMembro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(new Intent(GerenciarEquipe.this, AdicionarMembros.class), tbEquipe);
            }
        });

        lvEquipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tbComp = listComp.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(GerenciarEquipe.this);
                builder.setTitle("Retirar membro");
                builder.setMessage("Você tem certeza que retirar " + tbComp.getNome() + " de sua equipe?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(GerenciarEquipe.this).start();
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
            map.put("r", "retirarEquipe");
            map.put("idEquipe", tbEquipe.getIdEquipe());
            map.put("idFuncionario", tbComp.getIdFuncionario());

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
                                tbCompDAO.apagarFuncionario(tbComp.getIdFuncionario());
                                redirectActivity(new Intent(GerenciarEquipe.this, DashboardMain.class), tbEquipe);
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
                Toast toast = Toast.makeText(GerenciarEquipe.this, texto, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void redirectActivity(Intent intent, TbEquipe tbEquipe) {
        intent.putExtra("tbEquipe", tbEquipe);
        startActivity(intent);
        GerenciarEquipe.this.finish();
    }

    public void redirectActivity(Intent intent, TbEquipe tbEquipe, String dashboard) {
        intent.putExtra("tbEquipe", tbEquipe);
        intent.putExtra("tela", dashboard);
        startActivity(intent);
        GerenciarEquipe.this.finish();
    }


    public void showMessage(String titulo, String texto) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(GerenciarEquipe.this);
        dialogo.setTitle(titulo);
        dialogo.setMessage(texto);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }

    class MyAdapter extends ArrayAdapter<TbComp> {
        Context context;
        List<TbComp> vecTbComp;

        MyAdapter(Context context, List<TbComp> vecTbComp){
            super(context, R.layout.list_equipe_adapter, vecTbComp);

            this.context = context;
            this.vecTbComp = vecTbComp;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View styleListView = layoutInflater.inflate(R.layout.list_equipe_adapter, parent, false);

            TbComp tbComp = tbCompDAO.model(vecTbComp.get(position).getIdFuncionario());

            TextView tvNome = styleListView.findViewById(R.id.tvNome);
            TextView tvTipo = styleListView.findViewById(R.id.tvTipo);

            tvNome.setText(tbComp.getNome());
            tvTipo.setText(tbComp.getFuncionarioTp());

            return styleListView;
        }
    }

    @Override
    public void onBackPressed() {
        redirectActivity(new Intent(GerenciarEquipe.this, DashboardMain.class), tbEquipe);
    }
}