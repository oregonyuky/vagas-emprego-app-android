package br.edu.unoeste.vagasonline.model;

public class InteresseRequest {
    private final Vaga vaga;
    private final CandidatoRequest candidato;

    public InteresseRequest(Vaga vaga, String nome, String cpf, String email, String telefone) {
        this.vaga = vaga;
        this.candidato = new CandidatoRequest(nome, cpf, email, telefone);
    }
}
