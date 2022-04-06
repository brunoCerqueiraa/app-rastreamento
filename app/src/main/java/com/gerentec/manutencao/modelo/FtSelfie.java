package com.gerentec.manutencao.modelo;

import java.io.Serializable;

public class FtSelfie implements Serializable {
    private Long idFtSelfie;
    private Long idSelfie;
    private String dtInsert;
    private String coordX;
    private String coordY;
    private String arquivo;
    private String recebidoServidor;

    public Long getIdFtSelfie() {
        return idFtSelfie;
    }

    public void setIdFtSelfie(Long idFtSelfie) {
        this.idFtSelfie = idFtSelfie;
    }

    public Long getIdSelfie() {
        return idSelfie;
    }

    public void setIdSelfie(Long idSelfie) {
        this.idSelfie = idSelfie;
    }

    public String getDtInsert() {
        return dtInsert;
    }

    public void setDtInsert(String dtInsert) {
        this.dtInsert = dtInsert;
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

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getRecebidoServidor() {
        return recebidoServidor;
    }

    public void setRecebidoServidor(String recebidoServidor) {
        this.recebidoServidor = recebidoServidor;
    }
}
