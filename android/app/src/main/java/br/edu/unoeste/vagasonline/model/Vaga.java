package br.edu.unoeste.vagasonline.model;

import java.math.BigDecimal;

public class Vaga {
    private String id;
    private Empresa empresa;
    private Cargo cargo;
    private String cidade;
    private String estado;
    private String requisitos;
    private String formacao;
    private String conhecimentos;
    private String regime;
    private String jornada;
    private BigDecimal remuneracao;

    public String getId() { return id; }
    public Empresa getEmpresa() { return empresa; }
    public Cargo getCargo() { return cargo; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }
    public String getRequisitos() { return requisitos; }
    public String getFormacao() { return formacao; }
    public String getConhecimentos() { return conhecimentos; }
    public String getRegime() { return regime; }
    public String getJornada() { return jornada; }
    public BigDecimal getRemuneracao() { return remuneracao; }
}
