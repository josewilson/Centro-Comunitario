package com.centrocomunitario.api.dto;
import lombok.Data;

import java.util.List;

@Data
public class CentroComunitarioDTO {

    private String nome;
    private String endereco;
    private String localizacao;
    private int capacidadeMaxima;
    private int ocupacaoAtual;
    private List<RecursoDTO> recursos;

}
