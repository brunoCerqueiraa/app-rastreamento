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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gerentec.manutencao.R;
import com.gerentec.manutencao.auxiliar.BaseDados;
import com.gerentec.manutencao.dao.TbCompDAO;
import com.gerentec.manutencao.dao.TbCompEquipDAO;
import com.gerentec.manutencao.dao.TbEquipamentoDAO;
import com.gerentec.manutencao.dao.TbFuncionarioDAO;
import com.gerentec.manutencao.http.HttpNormalImplementation;
import com.gerentec.manutencao.modelo.TbComp;
import com.gerentec.manutencao.modelo.TbCompEquip;
import com.gerentec.manutencao.modelo.TbEquipamento;
import com.gerentec.manutencao.modelo.TbEquipe;
import com.gerentec.manutencao.modelo.TbFuncionario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdicionarEquipamento extends AppCompatActivity implements Runnable{
    Intent intent;
    ListView lvEquipamentos;
    EditText etNome;

    TbEquipe tbEquipe;
    String response = "";
    Long kmHori;
    String kmHoriFinal;
    private Handler handler = new Handler();

    List<TbEquipamento> listEquipamentos;
    TbEquipamento tbEquipamento;
    TbEquipamentoDAO tbEquipamentoDAO = new TbEquipamentoDAO(BaseDados.getBD(this));
    TbCompEquipDAO tbCompEquipDAO = new TbCompEquipDAO(BaseDados.getBD(this));

    public void getFormElements(){
        etNome = findViewById(R.id.etNome);
        lvEquipamentos = findViewById(R.id.lvEquipamentos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_equipamento);

        getFormElements();
        getPreviousIntent();

        listEquipamentos = tbEquipamentoDAO.list();
        MyAdapter adapterList = new MyAdapter(AdicionarEquipamento.this, listEquipamentos);
        lvEquipamentos.setAdapter(adapterList);

        etNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listEquipamentos = tbEquipamentoDAO.listFiltro(" WHERE descricao LIKE '%" + etNome.getText().toString() + "%' ORDER BY descricao ");
                MyAdapter adapterList = new MyAdapter(AdicionarEquipamento.this, listEquipamentos);
                lvEquipamentos.setAdapter(adapterList);
                adapterList.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        lvEquipamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tbEquipamento = tbEquipamentoDAO.model(listEquipamentos.get(position).getIdEquipamento());

                //EditText
                EditText editText= new EditText(AdicionarEquipamento.this);
                editText.setHint("Km/Horimetro");
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);


                //LinearLayout
                LinearLayout linearLayout = new LinearLayout(AdicionarEquipamento.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,30,10,20);

                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(editText);

                //AlertDialog
                FrameLayout layout = new FrameLayout(AdicionarEquipamento.this);
                layout.setPaddingRelative(45,0,45,0);
                layout.addView(linearLayout);

                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdicionarEquipamento.this);
                    builder.setTitle("");
                    builder.setMessage("Informe a km/horimetro:");
                    builder.setView(layout);
                    builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (editText.getText().toString().length() < 1){
                                showMessage("Validação", "Você precisa informar a km/horimetro!");
                            }
                            else {
                                Double tmp = Double.parseDouble(editText.getText().toString().replaceAll(",", ".")) * 10;
                                kmHori = tmp.longValue();
                                kmHoriFinal = kmHori.toString();
                                new Thread(AdicionarEquipamento.this).start();
                            }
                        }
                    });
                    builder.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                catch (Exception e){
                    showMessage("Validação", "Verifique se o campo foi digitado corretamente!");
                }
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
            map.put("r", "adicionarEquipamento");
            map.put("idEquipe", tbEquipe.getIdEquipe());
            map.put("idEquipamento", tbEquipamento.getIdEquipamento());
            map.put("kmHori", kmHoriFinal);

            response = HttpNormalImplementation.request(map);
            System.out.println("response: " + response);

            if (response.equals("indisponivel")){
                message("Este equipamento já foi reservado por uma outra equipe!");
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
                                TbCompEquip model = new TbCompEquip();
                                model.setIdEquipamento(tbEquipamento.getIdEquipamento());
                                model.setDescricao(tbEquipamento.getDescricao());
                                model.setTipo(tbEquipamento.getTipo());
                                model.setKm(kmHori);
                                model.setSitucao(tbEquipamento.getSituacao());
                                tbCompEquipDAO.insert(model);
                                tbEquipamentoDAO.apagarByIdEquip(tbEquipamento.getIdEquipamento());
                                redirectActivity(new Intent(AdicionarEquipamento.this, GerenciarEquipamento.class), tbEquipe);
                            }
                            else if(response.equals("EXISTE")){
                                message("Equipamento já alocado por outra equipe!");
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
        AdicionarEquipamento.this.finish();
    }

    public void redirectActivity(Intent intent, TbEquipe tbEquipe, String dashboard) {
        intent.putExtra("tbEquipe", tbEquipe);
        intent.putExtra("tela", dashboard);
        startActivity(intent);
        AdicionarEquipamento.this.finish();
    }

    public void message(String texto){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(AdicionarEquipamento.this, texto, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


    public void showMessage(String titulo, String texto) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(AdicionarEquipamento.this);
        dialogo.setTitle(titulo);
        dialogo.setMessage(texto);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }

    class MyAdapter extends ArrayAdapter<TbEquipamento> {
        Context context;
        List<TbEquipamento> vecTbEquipamento;

        MyAdapter(Context context, List<TbEquipamento> vecTbEquipamento){
            super(context, R.layout.list_equipe_adapter, vecTbEquipamento);

            this.context = context;
            this.vecTbEquipamento = vecTbEquipamento;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View styleListView = layoutInflater.inflate(R.layout.list_equipe_adapter, parent, false);

            TbEquipamento tbEquipamento = tbEquipamentoDAO.model(vecTbEquipamento.get(position).getIdEquipamento());

            TextView tvNome = styleListView.findViewById(R.id.tvNome);
            TextView tvTipo = styleListView.findViewById(R.id.tvTipo);

            tvNome.setText(tbEquipamento.getDescricao());
            tvTipo.setText(tbEquipamento.getTipo());

            return styleListView;
        }
    }

    @Override
    public void onBackPressed() {
        redirectActivity(new Intent(AdicionarEquipamento.this, DashboardMain.class), tbEquipe);
    }
}