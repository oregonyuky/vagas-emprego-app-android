package br.edu.unoeste.vagasonline.model;

import com.google.gson.annotations.SerializedName;

public class Empresa {
    @SerializedName("nome_fantasia")
    private String nomeFantasia;
    @SerializedName("razao_social")
    private String razaoSocial;
    private String tipo;

    public String getNomeFantasia() { return nomeFantasia; }
    public String getRazaoSocial() { return razaoSocial; }
    public String getTipo() { return tipo; }
}
