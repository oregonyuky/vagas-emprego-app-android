package unoeste.fipp.vagasonlinebackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import unoeste.fipp.vagasonlinebackend.entities.Vaga;

public interface VagaRepository extends MongoRepository<Vaga, String> {
}
