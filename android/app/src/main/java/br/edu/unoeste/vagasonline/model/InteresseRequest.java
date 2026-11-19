package br.edu.unoeste.vagasonline.model;

public class InteresseRequest {
    private final String nome;
    private final String cpf;
    private final String email;
    private final String telefone;

    public InteresseRequest(String nome, String cpf, String email, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
    }
}
