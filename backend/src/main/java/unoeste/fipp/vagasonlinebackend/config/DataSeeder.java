package unoeste.fipp.vagasonlinebackend.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import unoeste.fipp.vagasonlinebackend.entities.Cargo;
import unoeste.fipp.vagasonlinebackend.entities.Empresa;
import unoeste.fipp.vagasonlinebackend.entities.Vaga;
import unoeste.fipp.vagasonlinebackend.repositories.CargoRepository;
import unoeste.fipp.vagasonlinebackend.repositories.EmpresaRepository;
import unoeste.fipp.vagasonlinebackend.repositories.VagaRepository;

import java.math.BigDecimal;

@Component
public class DataSeeder implements ApplicationRunner {
    private final CargoRepository cargoRepository;
    private final EmpresaRepository empresaRepository;
    private final VagaRepository vagaRepository;

    public DataSeeder(CargoRepository cargoRepository, EmpresaRepository empresaRepository,
                      VagaRepository vagaRepository) {
        this.cargoRepository = cargoRepository;
        this.empresaRepository = empresaRepository;
        this.vagaRepository = vagaRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        Cargo desenvolvedor = cargoRepository.findByNomeIgnoreCase("Desenvolvedor Java")
                .orElseGet(() -> cargoRepository.save(new Cargo(null, "Desenvolvedor Java")));
        cargoRepository.findByNomeIgnoreCase("Analista de Dados")
                .orElseGet(() -> cargoRepository.save(new Cargo(null, "Analista de Dados")));
        cargoRepository.findByNomeIgnoreCase("Assistente Administrativo")
                .orElseGet(() -> cargoRepository.save(new Cargo(null, "Assistente Administrativo")));

        Empresa empresa = empresaRepository.findByNomeFantasiaIgnoreCase("Tech Oeste")
                .orElseGet(() -> empresaRepository.save(
                        new Empresa(null, "Tech Oeste", "Tech Oeste Soluções Ltda.", "Tecnologia")));
        empresaRepository.findByNomeFantasiaIgnoreCase("Comercial Paulista")
                .orElseGet(() -> empresaRepository.save(
                        new Empresa(null, "Comercial Paulista", "Comercial Paulista S.A.", "Comércio")));

        if (vagaRepository.count() == 0) {
            Vaga vaga = new Vaga();
            vaga.setEmpresa(empresa);
            vaga.setCargo(desenvolvedor);
            vaga.setCidade("Presidente Prudente");
            vaga.setEstado("SP");
            vaga.setRequisitos("Experiência com desenvolvimento de APIs REST");
            vaga.setFormacao("Ensino superior em Computação ou áreas relacionadas");
            vaga.setConhecimentos("Java, Spring Boot, Git e bancos de dados");
            vaga.setRegime("CLT");
            vaga.setJornada("40 horas semanais");
            vaga.setRemuneracao(new BigDecimal("5500.00"));
            vagaRepository.save(vaga);
        }
    }
}
