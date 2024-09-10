package com.centrocomunitario.api.controller;

import com.centrocomunitario.api.dto.AtualizarOcupacaoDTO;
import com.centrocomunitario.api.dto.CentroComunitarioDTO;
import com.centrocomunitario.api.model.CentroComunitario;
import com.centrocomunitario.api.service.CentroComunitarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/centros-comunitarios")
public class CentroComunitarioController {

    private static final Logger logger = LoggerFactory.getLogger(CentroComunitarioController.class);

    @Autowired
    private CentroComunitarioService centroComunitarioService;

    @PostMapping
    public ResponseEntity<CentroComunitario> adicionarCentro(@RequestBody CentroComunitarioDTO centroDTO) {
        logger.info("Recebendo solicitação para adicionar um novo centro comunitário: {}", centroDTO.getNome());
        CentroComunitario centroComunitario = centroComunitarioService.adicionarCentro(centroDTO);
        logger.info("Centro comunitário adicionado com sucesso: {}", centroComunitario.getNome());
        return ResponseEntity.ok(centroComunitario);
    }

    @PutMapping("/{id}/ocupacao")
    public ResponseEntity<Void> atualizarOcupacao(@PathVariable String id, @RequestBody AtualizarOcupacaoDTO atualizarOcupacaoDTO) {
        logger.info("Recebendo solicitação para atualizar ocupação do centro comunitário com ID: {}", id);
        centroComunitarioService.atualizarOcupacao(id, atualizarOcupacaoDTO);
        logger.info("Ocupação do centro comunitário com ID {} atualizada para {} pessoas", id, atualizarOcupacaoDTO.getOcupacaoAtual());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ocupacao-alta")
    public ResponseEntity<List<CentroComunitario>> buscarCentrosComOcupacaoAlta() {
        logger.info("Recebendo solicitação para buscar centros comunitários com ocupação superior a 90%");

        List<CentroComunitario> centros = centroComunitarioService.buscarCentrosComOcupacaoAlta();

        logger.info("Encontrados {} centros comunitários com ocupação acima de 90%", centros.size());

        return ResponseEntity.ok(centros);
    }

    @GetMapping("/recursos/quantidade-media")
    public ResponseEntity<Map<String, Double>> calcularMediaRecursos() {
        logger.info("Recebendo solicitação para calcular a média de recursos");
        Map<String, Double> mediaRecursos = centroComunitarioService.calcularMediaRecursos();
        logger.info("Média de recursos calculada com sucesso");
        return ResponseEntity.ok(mediaRecursos);
    }
}
