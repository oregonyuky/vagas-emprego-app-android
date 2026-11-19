package unoeste.fipp.vagasonlinebackend.restcontrollers;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import unoeste.fipp.vagasonlinebackend.entities.Cargo;
import unoeste.fipp.vagasonlinebackend.entities.Empresa;
import unoeste.fipp.vagasonlinebackend.repositories.CargoRepository;
import unoeste.fipp.vagasonlinebackend.repositories.EmpresaRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CatalogoRestController {
    private final CargoRepository cargoRepository;
    private final EmpresaRepository empresaRepository;

    public CatalogoRestController(CargoRepository cargoRepository, EmpresaRepository empresaRepository) {
        this.cargoRepository = cargoRepository;
        this.empresaRepository = empresaRepository;
    }

    @GetMapping
    public Map<String, String> health() {
        return Map.of("status", "Servidor em funcionamento");
    }

    @GetMapping("/cargos")
    public ResponseEntity<List<Cargo>> cargos() {
        return ResponseEntity.ok(cargoRepository.findAll(Sort.by("nome").ascending()));
    }

    @GetMapping("/empresas")
    public ResponseEntity<List<Empresa>> empresas(@RequestParam(required = false) String tipo) {
        List<Empresa> empresas = StringUtils.hasText(tipo)
                ? empresaRepository.findByTipoIgnoreCase(tipo.trim())
                : empresaRepository.findAll(Sort.by("nomeFantasia").ascending());
        return ResponseEntity.ok(empresas);
    }
}
