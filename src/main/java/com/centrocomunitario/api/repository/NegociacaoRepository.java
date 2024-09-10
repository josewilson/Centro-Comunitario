package com.centrocomunitario.api.repository;

import com.centrocomunitario.api.model.Negociacao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NegociacaoRepository extends MongoRepository<Negociacao, String> {
    List<Negociacao> findByCentroOrigemIdOrCentroDestinoIdAndDataNegociacaoBetween(
            String centroOrigemId, String centroDestinoId, LocalDateTime startDate, LocalDateTime endDate);
}
