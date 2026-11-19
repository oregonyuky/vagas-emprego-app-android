package unoeste.fipp.vagasonlinebackend.restcontrollers;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unoeste.fipp.vagasonlinebackend.entities.Cargo;
import unoeste.fipp.vagasonlinebackend.entities.Empresa;
import unoeste.fipp.vagasonlinebackend.repositories.CargoRepository;
import unoeste.fipp.vagasonlinebackend.repositories.EmpresaRepository;

import java.util.List;

@RestController
@RequestMapping("/apis")
public class CatalogoRestController {
    private final CargoRepository cargoRepository;
    private final EmpresaRepository empresaRepository;

    public CatalogoRestController(CargoRepository cargoRepository, EmpresaRepository empresaRepository) {
        this.cargoRepository = cargoRepository;
        this.empresaRepository = empresaRepository;
    }

    @GetMapping("/empresa/get-all-empresas")
    public ResponseEntity<List<Empresa>> empresas() {
        return ResponseEntity.ok(empresaRepository.findAll(Sort.by("nomeFantasia").ascending()));
    }

    @GetMapping("/cargo/get-all-cargos")
    public ResponseEntity<List<Cargo>> cargos() {
        return ResponseEntity.ok(cargoRepository.findAll(Sort.by("nome").ascending()));
    }
}
