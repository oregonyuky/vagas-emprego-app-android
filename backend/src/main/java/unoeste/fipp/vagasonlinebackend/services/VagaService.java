package unoeste.fipp.vagasonlinebackend.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import unoeste.fipp.vagasonlinebackend.dto.CandidatoRequest;
import unoeste.fipp.vagasonlinebackend.dto.InteresseRequest;
import unoeste.fipp.vagasonlinebackend.dto.VagaRequest;
import unoeste.fipp.vagasonlinebackend.entities.Candidato;
import unoeste.fipp.vagasonlinebackend.entities.Cargo;
import unoeste.fipp.vagasonlinebackend.entities.Empresa;
import unoeste.fipp.vagasonlinebackend.entities.Interesse;
import unoeste.fipp.vagasonlinebackend.entities.Vaga;
import unoeste.fipp.vagasonlinebackend.exceptions.ResourceNotFoundException;
import unoeste.fipp.vagasonlinebackend.repositories.CargoRepository;
import unoeste.fipp.vagasonlinebackend.repositories.EmpresaRepository;
import unoeste.fipp.vagasonlinebackend.repositories.InteresseRepository;
import unoeste.fipp.vagasonlinebackend.repositories.VagaRepository;

import java.util.List;
import java.util.Locale;

@Service
public class VagaService {
    private final VagaRepository vagaRepository;
    private final EmpresaRepository empresaRepository;
    private final CargoRepository cargoRepository;
    private final InteresseRepository interesseRepository;

    public VagaService(VagaRepository vagaRepository, EmpresaRepository empresaRepository,
                       CargoRepository cargoRepository, InteresseRepository interesseRepository) {
        this.vagaRepository = vagaRepository;
        this.empresaRepository = empresaRepository;
        this.cargoRepository = cargoRepository;
        this.interesseRepository = interesseRepository;
    }

    public List<Vaga> listar() {
        return vagaRepository.findByRegistroNotNull();
    }

    public Vaga buscar(String registro) {
        return vagaRepository.findByRegistro(registro)
                .orElseThrow(() -> new ResourceNotFoundException("Vaga não encontrada"));
    }

    public Vaga criar(VagaRequest request) {
        String registro = request.registro().trim();
        if (vagaRepository.existsByRegistro(registro)) {
            throw new IllegalArgumentException("Já existe uma vaga com este registro");
        }
        return salvar(new Vaga(), request);
    }

    public Vaga atualizar(VagaRequest request) {
        return salvar(buscar(request.registro().trim()), request);
    }

    private Vaga salvar(Vaga vaga, VagaRequest request) {
        if (!StringUtils.hasText(request.empresa().getNomeFantasia())) {
            throw new IllegalArgumentException("O nome fantasia da empresa é obrigatório");
        }

        Empresa empresa = empresaRepository.findByNomeFantasiaIgnoreCase(request.empresa().getNomeFantasia())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada"));
        Cargo cargo = cargoRepository.findByNomeIgnoreCase(request.cargo())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));

        vaga.setRegistro(request.registro().trim());
        vaga.setEmpresa(empresa);
        vaga.setCargo(cargo.getNome());
        vaga.setCidade(request.cidade().trim());
        vaga.setEstado(request.estado().trim().toUpperCase(Locale.ROOT));
        vaga.setPreRequisitos(request.preRequisitos().trim());
        vaga.setFormacao(request.formacao().trim());
        vaga.setConhecimentosRequeridos(request.conhecimentosRequeridos().trim());
        vaga.setRegime(request.regime().trim());
        vaga.setJornadaTrabalho(request.jornadaTrabalho().trim());
        vaga.setRemuneracao(request.remuneracao().trim());
        return vagaRepository.save(vaga);
    }

    public void excluir(String registro) {
        buscar(registro);
        interesseRepository.deleteByVaga_Registro(registro);
        vagaRepository.deleteByRegistro(registro);
    }

    public Interesse registrarInteresse(InteresseRequest request) {
        if (!StringUtils.hasText(request.vaga().getRegistro())) {
            throw new IllegalArgumentException("O registro da vaga é obrigatório");
        }

        Vaga vaga = buscar(request.vaga().getRegistro());
        CandidatoRequest dados = request.candidato();
        Candidato candidato = new Candidato(
                dados.nome().trim(),
                dados.cpf(),
                dados.email().trim().toLowerCase(Locale.ROOT),
                dados.telefone().trim(),
                StringUtils.hasText(dados.formacao()) ? dados.formacao().trim() : null
        );

        Interesse interesse = new Interesse();
        interesse.setVaga(vaga);
        interesse.setCandidato(candidato);
        return interesseRepository.save(interesse);
    }
}
