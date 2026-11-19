package unoeste.fipp.vagasonlinebackend.config;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class DataSeeder implements ApplicationRunner {
    private final MongoTemplate mongoTemplate;

    public DataSeeder(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        importar("data/cargos.json", "cargos");
        importar("data/empresas.json", "empresas");
        importar("data/vagas.json", "vagas");
        importar("data/interesses.json", "interesses");
    }

    private void importar(String recurso, String colecao) throws IOException {
        ClassPathResource resource = new ClassPathResource(recurso);
        String json;
        try (var input = resource.getInputStream()) {
            json = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }

        MongoCollection<Document> collection = mongoTemplate.getCollection(colecao);
        for (BsonValue value : BsonArray.parse(json)) {
            BsonDocument bsonDocument = value.asDocument();
            Document document = Document.parse(bsonDocument.toJson());
            Bson identidade = identidade(colecao, document);
            if (collection.countDocuments(identidade) == 0) {
                collection.insertOne(document);
            }
        }
    }

    private Bson identidade(String colecao, Document document) {
        return switch (colecao) {
            case "cargos" -> Filters.eq("nome", document.getString("nome"));
            case "empresas" -> Filters.eq("nome_fantasia", document.getString("nome_fantasia"));
            case "vagas" -> Filters.eq("registro", document.getString("registro"));
            case "interesses" -> Filters.and(
                    Filters.eq("vaga.registro", document.get("vaga", Document.class).getString("registro")),
                    Filters.eq("candidato.cpf", document.get("candidato", Document.class).getString("cpf"))
            );
            default -> throw new IllegalArgumentException("Coleção de carga desconhecida: " + colecao);
        };
    }
}
