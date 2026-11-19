package unoeste.fipp.vagasonlinebackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "vagas")
public class Vaga {
    @Id
    @JsonIgnore
    private String id;
    private String registro;
    private Empresa empresa;
    private String cargo;
    private String cidade;
    private String estado;
    @Field("pre_requisitos")
    @JsonProperty("pre_requisitos")
    private String preRequisitos;
    private String formacao;
    @Field("conhecimentos_requeridos")
    @JsonProperty("conhecimentos_requeridos")
    private String conhecimentosRequeridos;
    private String regime;
    @Field("jornada_trabalho")
    @JsonProperty("jornada_trabalho")
    private String jornadaTrabalho;
    private String remuneracao;

    public Vaga() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRegistro() { return registro; }
    public void setRegistro(String registro) { this.registro = registro; }
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getPreRequisitos() { return preRequisitos; }
    public void setPreRequisitos(String preRequisitos) { this.preRequisitos = preRequisitos; }
    public String getFormacao() { return formacao; }
    public void setFormacao(String formacao) { this.formacao = formacao; }
    public String getConhecimentosRequeridos() { return conhecimentosRequeridos; }
    public void setConhecimentosRequeridos(String conhecimentosRequeridos) { this.conhecimentosRequeridos = conhecimentosRequeridos; }
    public String getRegime() { return regime; }
    public void setRegime(String regime) { this.regime = regime; }
    public String getJornadaTrabalho() { return jornadaTrabalho; }
    public void setJornadaTrabalho(String jornadaTrabalho) { this.jornadaTrabalho = jornadaTrabalho; }
    public String getRemuneracao() { return remuneracao; }
    public void setRemuneracao(String remuneracao) { this.remuneracao = remuneracao; }
}
