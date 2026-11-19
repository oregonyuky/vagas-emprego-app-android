package unoeste.fipp.vagasonlinebackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import unoeste.fipp.vagasonlinebackend.entities.Interesse;

public interface InteresseRepository extends MongoRepository<Interesse, String> {
    void deleteByVaga_Registro(String registro);
}
