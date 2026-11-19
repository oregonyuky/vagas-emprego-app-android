package br.edu.unoeste.vagasonline.model;

import com.google.gson.annotations.SerializedName;

public class Vaga {
    private String registro;
    private Empresa empresa;
    private String cargo;
    private String cidade;
    private String estado;
    @SerializedName("pre_requisitos")
    private String preRequisitos;
    private String formacao;
    @SerializedName("conhecimentos_requeridos")
    private String conhecimentosRequeridos;
    private String regime;
    @SerializedName("jornada_trabalho")
    private String jornadaTrabalho;
    private String remuneracao;

    public String getRegistro() { return registro; }
    public Empresa getEmpresa() { return empresa; }
    public String getCargo() { return cargo; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }
    public String getPreRequisitos() { return preRequisitos; }
    public String getFormacao() { return formacao; }
    public String getConhecimentosRequeridos() { return conhecimentosRequeridos; }
    public String getRegime() { return regime; }
    public String getJornadaTrabalho() { return jornadaTrabalho; }
    public String getRemuneracao() { return remuneracao; }
}
