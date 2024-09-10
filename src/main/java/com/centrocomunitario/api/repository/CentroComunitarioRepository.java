package com.centrocomunitario.api.repository;

import com.centrocomunitario.api.model.CentroComunitario;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CentroComunitarioRepository extends MongoRepository<CentroComunitario, String> {
    List<CentroComunitario> findByOcupacaoAtualGreaterThanEqual(int ocupacao);
}
