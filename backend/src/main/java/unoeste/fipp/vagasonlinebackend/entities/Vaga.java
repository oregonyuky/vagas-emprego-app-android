package unoeste.fipp.vagasonlinebackend.entities;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "vagas")
public class Vaga {
    @Id
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
    @CreatedDate
    private Instant criadaEm;
    @LastModifiedDate
    private Instant atualizadaEm;

    public Vaga() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getRequisitos() { return requisitos; }
    public void setRequisitos(String requisitos) { this.requisitos = requisitos; }
    public String getFormacao() { return formacao; }
    public void setFormacao(String formacao) { this.formacao = formacao; }
    public String getConhecimentos() { return conhecimentos; }
    public void setConhecimentos(String conhecimentos) { this.conhecimentos = conhecimentos; }
    public String getRegime() { return regime; }
    public void setRegime(String regime) { this.regime = regime; }
    public String getJornada() { return jornada; }
    public void setJornada(String jornada) { this.jornada = jornada; }
    public BigDecimal getRemuneracao() { return remuneracao; }
    public void setRemuneracao(BigDecimal remuneracao) { this.remuneracao = remuneracao; }
    public Instant getCriadaEm() { return criadaEm; }
    public void setCriadaEm(Instant criadaEm) { this.criadaEm = criadaEm; }
    public Instant getAtualizadaEm() { return atualizadaEm; }
    public void setAtualizadaEm(Instant atualizadaEm) { this.atualizadaEm = atualizadaEm; }
}
