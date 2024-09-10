package com.centrocomunitario.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class NegociacaoDTO {
    private String centroOrigemId;
    private String centroDestinoId;
    private List<RecursoDTO> recursosTrocaOrigem;
    private List<RecursoDTO> recursosTrocaDestino;
}
