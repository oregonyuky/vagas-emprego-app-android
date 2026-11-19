package unoeste.fipp.vagasonlinebackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import unoeste.fipp.vagasonlinebackend.entities.Empresa;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepository extends MongoRepository<Empresa, String> {
    List<Empresa> findByTipoIgnoreCase(String tipo);
    Optional<Empresa> findByNomeFantasiaIgnoreCase(String nomeFantasia);
}
