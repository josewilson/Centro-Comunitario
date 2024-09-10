package com.centrocomunitario.api.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "centros_comunitarios")
public class CentroComunitario {

    @Id
    private String id;
    private String nome;
    private String endereco;
    private String localizacao;
    private int capacidadeMaxima;
    private int ocupacaoAtual;
    private List<Recurso> recursos;

}

