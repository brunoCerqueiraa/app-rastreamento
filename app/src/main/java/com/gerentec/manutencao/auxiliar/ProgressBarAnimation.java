package com.gerentec.manutencao.auxiliar;

import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gerentec.manutencao.activities.AdicionarMembros;
import com.gerentec.manutencao.activities.DashboardMain;
import com.gerentec.manutencao.modelo.TbEquipe;

public class ProgressBarAnimation extends Animation {

    private Context context;
    private ProgressBar progressBar;
    private TextView textView;
    private float from;
    private float to;
    TbEquipe tbEquipe;
    String tela;

    public ProgressBarAnimation(Context context, ProgressBar progressBar, TextView textView, float from, float to, TbEquipe tbEquipe, String tela) {
        this.context = context;
        this.progressBar = progressBar;
        this.textView = textView;
        this.from = from;
        this.to = to;
        this.tbEquipe = tbEquipe;
        this.tela = tela;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int)value);
        textView.setText((int) value + " %");

        if(value == to){
            Intent intent;
            if (tela != null && tela.equals("gerenciarEquipe")){
                intent = new Intent(context, AdicionarMembros.class);
            }
            else{
                intent = new Intent(context, DashboardMain.class);
            }
            intent.putExtra("tbEquipe", tbEquipe);
            context.startActivity(intent);
        }
    }
}
