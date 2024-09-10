package com.centrocomunitario.api.service;

import com.centrocomunitario.api.dto.NegociacaoDTO;
import com.centrocomunitario.api.dto.RecursoDTO;
import com.centrocomunitario.api.model.CentroComunitario;
import com.centrocomunitario.api.model.Negociacao;
import com.centrocomunitario.api.model.Recurso;
import com.centrocomunitario.api.repository.CentroComunitarioRepository;
import com.centrocomunitario.api.repository.NegociacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NegociacaoService {

    private static final Logger logger = LoggerFactory.getLogger(NegociacaoService.class);

    @Autowired
    private CentroComunitarioRepository centroComunitarioRepository;

    @Autowired
    private NegociacaoRepository negociacaoRepository;

    public Negociacao realizarNegociacao(NegociacaoDTO dto) {
        logger.info("Iniciando negociação entre o centro {} e o centro {}", dto.getCentroOrigemId(), dto.getCentroDestinoId());

        Optional<CentroComunitario> centroOrigemOpt = centroComunitarioRepository.findById(dto.getCentroOrigemId());
        Optional<CentroComunitario> centroDestinoOpt = centroComunitarioRepository.findById(dto.getCentroDestinoId());

        if (!centroOrigemOpt.isPresent() || !centroDestinoOpt.isPresent()) {
            logger.error("Um dos centros comunitários não foi encontrado.");
            throw new IllegalArgumentException("Um dos centros comunitários não foi encontrado.");
        }

        CentroComunitario centroOrigem = centroOrigemOpt.get();
        CentroComunitario centroDestino = centroDestinoOpt.get();

        int pontosOrigem = calcularPontos(dto.getRecursosTrocaOrigem());
        int pontosDestino = calcularPontos(dto.getRecursosTrocaDestino());

        boolean ocupacaoAltaOrigem = centroOrigem.getOcupacaoAtual() >= (centroOrigem.getCapacidadeMaxima() * 0.9);
        boolean ocupacaoAltaDestino = centroDestino.getOcupacaoAtual() >= (centroDestino.getCapacidadeMaxima() * 0.9);

        if (!ocupacaoAltaOrigem && !ocupacaoAltaDestino && pontosOrigem != pontosDestino) {
            logger.error("A troca de recursos deve ter pontos equivalentes.");
            throw new IllegalArgumentException("A troca de recursos deve ter pontos equivalentes.");
        }

        Negociacao negociacao = new Negociacao();
        negociacao.setCentroOrigemId(dto.getCentroOrigemId());
        negociacao.setCentroDestinoId(dto.getCentroDestinoId());
        negociacao.setRecursosTrocaOrigem(convertDtoToModel(dto.getRecursosTrocaOrigem()));
        negociacao.setRecursosTrocaDestino(convertDtoToModel(dto.getRecursosTrocaDestino()));
        negociacao.setDataNegociacao(LocalDateTime.now());

        logger.info("Negociação realizada com sucesso entre os centros {} e {}", dto.getCentroOrigemId(), dto.getCentroDestinoId());

        return negociacaoRepository.save(negociacao);
    }

    public List<Negociacao> buscarHistoricoNegociacoes(String centroId, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Buscando histórico de negociações para o centro {} entre as datas {} e {}", centroId, startDate, endDate);

        if (startDate == null) {
            startDate = LocalDateTime.of(1970, 1, 1, 0, 0);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }

        List<Negociacao> negociacoes = negociacaoRepository.findByCentroOrigemIdOrCentroDestinoIdAndDataNegociacaoBetween(
                centroId, centroId, startDate, endDate);

        logger.info("Histórico de negociações encontrado: {} negociações para o centro {}", negociacoes.size(), centroId);
        return negociacoes;
    }

    private int calcularPontos(List<RecursoDTO> recursos) {
        return recursos.stream().mapToInt(recurso -> recurso.getPontos() * recurso.getQuantidade()).sum();
    }

    private List<Recurso> convertDtoToModel(List<RecursoDTO> recursosDTO) {
        return recursosDTO.stream().map(recursoDTO -> {
            Recurso recurso = new Recurso();
            recurso.setTipo(recursoDTO.getTipo());
            recurso.setQuantidade(recursoDTO.getQuantidade());
            recurso.setPontos(recursoDTO.getPontos());
            return recurso;
        }).collect(Collectors.toList());
    }
}
