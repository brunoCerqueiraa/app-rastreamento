package com.gerentec.manutencao.modelo;

public class VwCompEquipamento {
    //TbCompEquip
    private Long comp_idCompEquip;
    private Long comp_idEquipamento;
    private String comp_descricao;
    private String comp_tipo;
    private Long comp_km;
    private String comp_situacao;


    //TbEquipamento
    private Long equip_idEquipamento;
    private String equip_descricao;
    private String equip_tipo;
    private String equip_situacao;

    public Long getComp_km() {
        return comp_km;
    }

    public void setComp_km(Long comp_km) {
        this.comp_km = comp_km;
    }

    public String getComp_tipo() {
        return comp_tipo;
    }

    public void setComp_tipo(String comp_tipo) {
        this.comp_tipo = comp_tipo;
    }

    public String getEquip_tipo() {
        return equip_tipo;
    }

    public void setEquip_tipo(String equip_tipo) {
        this.equip_tipo = equip_tipo;
    }

    public Long getComp_idCompEquip() {
        return comp_idCompEquip;
    }

    public void setComp_idCompEquip(Long comp_idCompEquip) {
        this.comp_idCompEquip = comp_idCompEquip;
    }

    public Long getComp_idEquipamento() {
        return comp_idEquipamento;
    }

    public void setComp_idEquipamento(Long comp_idEquipamento) {
        this.comp_idEquipamento = comp_idEquipamento;
    }

    public String getComp_descricao() {
        return comp_descricao;
    }

    public void setComp_descricao(String comp_descricao) {
        this.comp_descricao = comp_descricao;
    }

    public String getComp_situacao() {
        return comp_situacao;
    }

    public void setComp_situacao(String comp_situacao) {
        this.comp_situacao = comp_situacao;
    }

    public Long getEquip_idEquipamento() {
        return equip_idEquipamento;
    }

    public void setEquip_idEquipamento(Long equip_idEquipamento) {
        this.equip_idEquipamento = equip_idEquipamento;
    }

    public String getEquip_descricao() {
        return equip_descricao;
    }

    public void setEquip_descricao(String equip_descricao) {
        this.equip_descricao = equip_descricao;
    }

    public String getEquip_situacao() {
        return equip_situacao;
    }

    public void setEquip_situacao(String equip_situacao) {
        this.equip_situacao = equip_situacao;
    }
}
