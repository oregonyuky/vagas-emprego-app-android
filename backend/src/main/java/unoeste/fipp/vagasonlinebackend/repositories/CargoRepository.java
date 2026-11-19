package unoeste.fipp.vagasonlinebackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import unoeste.fipp.vagasonlinebackend.entities.Cargo;

import java.util.Optional;

public interface CargoRepository extends MongoRepository<Cargo, String> {
    Optional<Cargo> findByNomeIgnoreCase(String nome);
}
