package com.gerentec.manutencao.modelo;

public class TbCompEquip {
    private Long idEquipamento;
    private String descricao;
    private String tipo;
    private Long km;
    private String situcao;

    public Long getKm() {
        return km;
    }

    public void setKm(Long km) {
        this.km = km;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getIdEquipamento() {
        return idEquipamento;
    }

    public void setIdEquipamento(Long idEquipamento) {
        this.idEquipamento = idEquipamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSitucao() {
        return situcao;
    }

    public void setSitucao(String situcao) {
        this.situcao = situcao;
    }
}
