package unoeste.fipp.vagasonlinebackend.entities;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "interesses")
public class Interesse {
    @Id
    private String id;
    private String vagaId;
    private String vagaDescricao;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    @CreatedDate
    private Instant registradoEm;

    public Interesse() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getVagaId() { return vagaId; }
    public void setVagaId(String vagaId) { this.vagaId = vagaId; }
    public String getVagaDescricao() { return vagaDescricao; }
    public void setVagaDescricao(String vagaDescricao) { this.vagaDescricao = vagaDescricao; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public Instant getRegistradoEm() { return registradoEm; }
    public void setRegistradoEm(Instant registradoEm) { this.registradoEm = registradoEm; }
}
