package com.centrocomunitario.api.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "negociacoes")
public class Negociacao {

    @Id
    private String id;
    private String centroOrigemId;
    private String centroDestinoId;
    private List<Recurso> recursosTrocaOrigem;
    private List<Recurso> recursosTrocaDestino;
    private LocalDateTime dataNegociacao;
}

