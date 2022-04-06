package com.gerentec.manutencao.modelo;

public class AndLocalizacao {
    private Long idAndLocalizacao;
    private Long idEquipe;
    private String dtCel;
    private String coordX;
    private String coordY;
    private String recebidoServidor;

    public String getRecebidoServidor() {
        return recebidoServidor;
    }

    public void setRecebidoServidor(String recebidoServidor) {
        this.recebidoServidor = recebidoServidor;
    }

    public Long getIdAndLocalizacao() {
        return idAndLocalizacao;
    }

    public void setIdAndLocalizacao(Long idAndLocalizacao) {
        this.idAndLocalizacao = idAndLocalizacao;
    }

    public Long getIdEquipe() {
        return idEquipe;
    }

    public void setIdEquipe(Long idEquipe) {
        this.idEquipe = idEquipe;
    }

    public String getDtCel() {
        return dtCel;
    }

    public void setDtCel(String dtCel) {
        this.dtCel = dtCel;
    }

    public String getCoordX() {
        return coordX;
    }

    public void setCoordX(String coordX) {
        this.coordX = coordX;
    }

    public String getCoordY() {
        return coordY;
    }

    public void setCoordY(String coordY) {
        this.coordY = coordY;
    }
}
