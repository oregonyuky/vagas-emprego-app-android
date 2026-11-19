package unoeste.fipp.vagasonlinebackend.services;

import org.springframework.stereotype.Service;
import unoeste.fipp.vagasonlinebackend.dto.InteresseRequest;
import unoeste.fipp.vagasonlinebackend.dto.VagaRequest;
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
        return vagaRepository.findAll();
    }

    public Vaga buscar(String id) {
        return vagaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaga não encontrada"));
    }

    public Vaga criar(VagaRequest request) {
        return salvar(new Vaga(), request);
    }

    public Vaga atualizar(VagaRequest request) {
        return salvar(buscar(request.id()), request);
    }

    private Vaga salvar(Vaga vaga, VagaRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada"));
        Cargo cargo = cargoRepository.findById(request.cargoId())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));

        vaga.setEmpresa(empresa);
        vaga.setCargo(cargo);
        vaga.setCidade(request.cidade().trim());
        vaga.setEstado(request.estado().trim().toUpperCase(Locale.ROOT));
        vaga.setRequisitos(request.requisitos().trim());
        vaga.setFormacao(request.formacao().trim());
        vaga.setConhecimentos(request.conhecimentos().trim());
        vaga.setRegime(request.regime().trim().toUpperCase(Locale.ROOT));
        vaga.setJornada(request.jornada().trim());
        vaga.setRemuneracao(request.remuneracao());
        return vagaRepository.save(vaga);
    }

    public void excluir(String id) {
        buscar(id);
        interesseRepository.deleteByVagaId(id);
        vagaRepository.deleteById(id);
    }

    public Interesse registrarInteresse(InteresseRequest request) {
        Vaga vaga = buscar(request.vagaId());
        Interesse interesse = new Interesse();
        interesse.setVagaId(request.vagaId());
        interesse.setVagaDescricao(vaga.getCargo().getNome() + " - " + vaga.getEmpresa().getNomeFantasia());
        interesse.setNome(request.nome().trim());
        interesse.setCpf(request.cpf());
        interesse.setEmail(request.email().trim().toLowerCase(Locale.ROOT));
        interesse.setTelefone(request.telefone().trim());
        return interesseRepository.save(interesse);
    }

}
