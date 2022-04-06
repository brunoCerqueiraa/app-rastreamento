package com.gerentec.manutencao.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gerentec.manutencao.R;
import com.gerentec.manutencao.auxiliar.BaseDados;
import com.gerentec.manutencao.dao.TbCompDAO;
import com.gerentec.manutencao.dao.TbFuncionarioDAO;
import com.gerentec.manutencao.http.HttpNormalImplementation;
import com.gerentec.manutencao.modelo.TbComp;
import com.gerentec.manutencao.modelo.TbEquipe;
import com.gerentec.manutencao.modelo.TbFuncionario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdicionarMembros extends AppCompatActivity implements Runnable {
    Intent intent;
    ListView lvEquipes;
    EditText etNome;

    TbEquipe tbEquipe;
    String response = "";
    private Handler handler = new Handler();

    List<TbFuncionario> listFuncionario;
    TbFuncionario tbFuncionario;
    TbFuncionarioDAO tbFuncionarioDAO = new TbFuncionarioDAO(BaseDados.getBD(this));
    TbCompDAO tbCompDAO = new TbCompDAO(BaseDados.getBD(this));

    public void getFormElements(){
        etNome = findViewById(R.id.etNome);
        lvEquipes = findViewById(R.id.lvEquipes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_membros);

        getFormElements();
        getPreviousIntent();

        listFuncionario = tbFuncionarioDAO.list();
        MyAdapter adapterList = new MyAdapter(AdicionarMembros.this, listFuncionario);
        lvEquipes.setAdapter(adapterList);

        etNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listFuncionario = tbFuncionarioDAO.listFiltro(" WHERE nome LIKE '%" + etNome.getText().toString() + "%' ORDER BY nome ");
                MyAdapter adapterList = new MyAdapter(AdicionarMembros.this, listFuncionario);
                lvEquipes.setAdapter(adapterList);
                adapterList.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        lvEquipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tbFuncionario = tbFuncionarioDAO.model(listFuncionario.get(position).getIdFuncionario());
                new Thread(AdicionarMembros.this).start();
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
            map.put("r", "adicionarEquipe");
            map.put("idEquipe", tbEquipe.getIdEquipe());
            map.put("idFuncionario", tbFuncionario.getIdFuncionario());

            response = HttpNormalImplementation.request(map);
            System.out.println("response: " + response);

            if (response.equals("indisponivel")){
                message("Este funcionário já faz parte de uma outra equipe!");
            }
            else if (response.equals("")){
                message("Verifique sua conexão com a internet!");
            }
            else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (response.equals("OK")) {
                                TbComp tbComp = new TbComp();
                                tbComp.setIdFuncionario(tbFuncionario.getIdFuncionario());
                                tbComp.setNome(tbFuncionario.getNome());
                                tbComp.setFuncionarioTp(tbFuncionario.getFuncionarioTp());
                                tbCompDAO.insert(tbComp);
                                tbFuncionarioDAO.apagarByIdFunc(tbFuncionario.getIdFuncionario());
                                redirectActivity(new Intent(AdicionarMembros.this, GerenciarEquipe.class), tbEquipe);
                            }
                            else if(response.equals("EXISTE")){
                                message("Funcionário já alocado por outra equipe!");
                            }
                            else {
                                message("Não foi possível realizar a adição!");
                            }
                        } catch (Exception e) {
                            message("Verifique sua conexão com a internet!");
                        }
                    }
                });
            }
        }
        catch (Exception e) {
            message("Verifique sua conexão com a internet!");
        }
    }

    public void redirectActivity(Intent intent, TbEquipe tbEquipe) {
        intent.putExtra("tbEquipe", tbEquipe);
        startActivity(intent);
        AdicionarMembros.this.finish();
    }

    public void redirectActivity(Intent intent, TbEquipe tbEquipe, String dashboard) {
        intent.putExtra("tbEquipe", tbEquipe);
        intent.putExtra("tela", dashboard);
        startActivity(intent);
        AdicionarMembros.this.finish();
    }

    public void message(String texto){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(AdicionarMembros.this, texto, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


    public void showMessage(String titulo, String texto) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(AdicionarMembros.this);
        dialogo.setTitle(titulo);
        dialogo.setMessage(texto);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }

    class MyAdapter extends ArrayAdapter<TbFuncionario> {
        Context context;
        List<TbFuncionario> vecTbFuncionario;

        MyAdapter(Context context, List<TbFuncionario> vecTbFuncionario){
            super(context, R.layout.list_escolher_equipe_adapter, vecTbFuncionario);

            this.context = context;
            this.vecTbFuncionario = vecTbFuncionario;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View styleListView = layoutInflater.inflate(R.layout.list_escolher_equipe_adapter, parent, false);

            TbFuncionario tbFuncionario = tbFuncionarioDAO.model(vecTbFuncionario.get(position).getIdFuncionario());

            TextView tvNome = styleListView.findViewById(R.id.tvNome);
            TextView tvTipo = styleListView.findViewById(R.id.tvTipo);

            tvNome.setText(tbFuncionario.getNome());
            tvTipo.setText(tbFuncionario.getFuncionarioTp());

            return styleListView;
        }
    }

    @Override
    public void onBackPressed() {
        redirectActivity(new Intent(AdicionarMembros.this, DashboardMain.class), tbEquipe);
    }
}