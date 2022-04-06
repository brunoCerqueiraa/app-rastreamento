package com.gerentec.manutencao.modelo;

public class AndTempoParametro {
    private Long idAndTempoParametro;
    private String tipo;
    private Long tempo;
    private String situacao;


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getIdAndTempoParametro() {
        return idAndTempoParametro;
    }

    public void setIdAndTempoParametro(Long idAndTempoParametro) {
        this.idAndTempoParametro = idAndTempoParametro;
    }

    public Long getTempo() {
        return tempo;
    }

    public void setTempo(Long tempo) {
        this.tempo = tempo;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
