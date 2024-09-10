package com.centrocomunitario.api.service;

import com.centrocomunitario.api.dto.AtualizarOcupacaoDTO;
import com.centrocomunitario.api.dto.CentroComunitarioDTO;
import com.centrocomunitario.api.model.CentroComunitario;
import com.centrocomunitario.api.model.Recurso;
import com.centrocomunitario.api.repository.CentroComunitarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CentroComunitarioService {

    private static final Logger logger = LoggerFactory.getLogger(CentroComunitarioService.class);

    @Autowired
    private CentroComunitarioRepository centroComunitarioRepository;

    public CentroComunitario adicionarCentro(CentroComunitarioDTO centroDTO) {
        CentroComunitario centroComunitario = new CentroComunitario();
        centroComunitario.setNome(centroDTO.getNome());
        centroComunitario.setEndereco(centroDTO.getEndereco());
        centroComunitario.setLocalizacao(centroDTO.getLocalizacao());
        centroComunitario.setCapacidadeMaxima(centroDTO.getCapacidadeMaxima());
        centroComunitario.setOcupacaoAtual(centroDTO.getOcupacaoAtual());

        List<Recurso> recursos = centroDTO.getRecursos() != null ? centroDTO.getRecursos().stream().map(recursoDTO -> {
            Recurso recurso = new Recurso();
            recurso.setTipo(recursoDTO.getTipo());
            recurso.setQuantidade(recursoDTO.getQuantidade());
            recurso.setPontos(recursoDTO.getPontos());
            return recurso;
        }).collect(Collectors.toList()) : Collections.emptyList();

        centroComunitario.setRecursos(recursos);

        logger.info("Adicionando centro comunitário: {}", centroComunitario.getNome());
        return centroComunitarioRepository.save(centroComunitario);
    }

    public CentroComunitario atualizarOcupacao(String id, AtualizarOcupacaoDTO dto) {
        Optional<CentroComunitario> optionalCentro = centroComunitarioRepository.findById(id);

        if (!optionalCentro.isPresent()) {
            logger.error("Centro Comunitário com ID {} não encontrado", id);
            throw new IllegalArgumentException("Centro Comunitário não encontrado");
        }

        CentroComunitario centroComunitario = optionalCentro.get();

        centroComunitario.setOcupacaoAtual(dto.getOcupacaoAtual());

        if (centroComunitario.getOcupacaoAtual() >= centroComunitario.getCapacidadeMaxima()) {
            logger.warn("Capacidade máxima atingida no centro: {}", centroComunitario.getNome());
            // Envio de notificação para outro serviço
        }

        logger.info("Atualizando ocupação do centro comunitário: {} para {} pessoas", centroComunitario.getNome(), dto.getOcupacaoAtual());
        return centroComunitarioRepository.save(centroComunitario);
    }

    public List<CentroComunitario> buscarCentrosComOcupacaoAlta() {
        int limiteOcupacao = (int) (0.9 * 100);

        return centroComunitarioRepository.findByOcupacaoAtualGreaterThanEqual(limiteOcupacao);
    }

    public Map<String, Double> calcularMediaRecursos() {
        List<CentroComunitario> centros = centroComunitarioRepository.findAll();
        Map<String, Integer> totalRecursos = new HashMap<>();
        Map<String, Integer> totalCentrosComRecurso = new HashMap<>();

        for (CentroComunitario centro : centros) {
            for (Recurso recurso : centro.getRecursos()) {
                totalRecursos.put(recurso.getTipo(), totalRecursos.getOrDefault(recurso.getTipo(), 0) + recurso.getQuantidade());
                totalCentrosComRecurso.put(recurso.getTipo(), totalCentrosComRecurso.getOrDefault(recurso.getTipo(), 0) + 1);
            }
        }

        Map<String, Double> mediaRecursos = new HashMap<>();
        for (String tipo : totalRecursos.keySet()) {
            int totalQuantidade = totalRecursos.get(tipo);
            int totalCentros = totalCentrosComRecurso.get(tipo);
            mediaRecursos.put(tipo, totalCentros == 0 ? 0 : (double) totalQuantidade / totalCentros);
        }

        return mediaRecursos;
    }

}
