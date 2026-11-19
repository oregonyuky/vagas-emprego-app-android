package unoeste.fipp.vagasonlinebackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import unoeste.fipp.vagasonlinebackend.entities.Vaga;

import java.util.List;
import java.util.Optional;

public interface VagaRepository extends MongoRepository<Vaga, String> {
    List<Vaga> findByRegistroNotNull();
    Optional<Vaga> findByRegistro(String registro);
    boolean existsByRegistro(String registro);
    void deleteByRegistro(String registro);
}
