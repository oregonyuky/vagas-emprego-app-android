package unoeste.fipp.vagasonlinebackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "interesses")
public class Interesse {
    @Id
    @JsonIgnore
    private String id;
    private Vaga vaga;
    private Candidato candidato;

    public Interesse() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Vaga getVaga() { return vaga; }
    public void setVaga(Vaga vaga) { this.vaga = vaga; }
    public Candidato getCandidato() { return candidato; }
    public void setCandidato(Candidato candidato) { this.candidato = candidato; }
}
