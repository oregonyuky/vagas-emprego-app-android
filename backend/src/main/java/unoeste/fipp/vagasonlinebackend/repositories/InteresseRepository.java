package unoeste.fipp.vagasonlinebackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import unoeste.fipp.vagasonlinebackend.entities.Interesse;

import java.util.List;

public interface InteresseRepository extends MongoRepository<Interesse, String> {
    List<Interesse> findByVagaIdOrderByRegistradoEmDesc(String vagaId);
    void deleteByVagaId(String vagaId);
}
