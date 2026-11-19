package unoeste.fipp.vagasonlinebackend.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "empresas")
public class Empresa {
    @Id
    private String id;
    @Field("nome_fantasia")
    private String nomeFantasia;
    @Field("razao_social")
    private String razaoSocial;
    private String tipo;

    public Empresa() {
    }

    public Empresa(String id, String nomeFantasia, String razaoSocial, String tipo) {
        this.id = id;
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
