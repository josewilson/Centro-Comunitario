package com.centrocomunitario.api.controller;

import com.centrocomunitario.api.dto.NegociacaoDTO;
import com.centrocomunitario.api.model.Negociacao;
import com.centrocomunitario.api.service.NegociacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/negociacoes")
public class NegociacaoController {

    private static final Logger logger = LoggerFactory.getLogger(NegociacaoController.class);

    @Autowired
    private NegociacaoService negociacaoService;

    @PostMapping
    public ResponseEntity<Negociacao> realizarNegociacao(@RequestBody NegociacaoDTO dto) {
        logger.info("Recebendo solicitação para realizar uma negociação entre os centros de origem {} e destino {}",
                dto.getCentroOrigemId(), dto.getCentroDestinoId());

        Negociacao negociacao = negociacaoService.realizarNegociacao(dto);

        logger.info("Negociação realizada com sucesso entre os centros de origem {} e destino {}",
                dto.getCentroOrigemId(), dto.getCentroDestinoId());

        return ResponseEntity.ok(negociacao);
    }
    @GetMapping("/historico")
    public ResponseEntity<List<Negociacao>> buscarHistoricoNegociacoes(
            @RequestParam String centroId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        logger.info("Recebendo solicitação para buscar histórico de negociações para o centro {}.", centroId);

        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : null;

        List<Negociacao> negociacoes = negociacaoService.buscarHistoricoNegociacoes(centroId, start, end);

        logger.info("Encontradas {} negociações para o centro {}.", negociacoes.size(), centroId);

        return ResponseEntity.ok(negociacoes);
    }
}
