package com.centrocomunitario.api.service;
import com.centrocomunitario.api.dto.NegociacaoDTO;
import com.centrocomunitario.api.dto.RecursoDTO;
import com.centrocomunitario.api.model.CentroComunitario;
import com.centrocomunitario.api.model.Negociacao;
import com.centrocomunitario.api.repository.CentroComunitarioRepository;
import com.centrocomunitario.api.repository.NegociacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class NegociacaoServiceTest {

    @Mock
    private CentroComunitarioRepository centroComunitarioRepository;

    @Mock
    private NegociacaoRepository negociacaoRepository;

    @InjectMocks
    private NegociacaoService negociacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRealizarNegociacao() {
        CentroComunitario centroOrigem = new CentroComunitario();
        centroOrigem.setId("centro1");
        centroOrigem.setCapacidadeMaxima(100);
        centroOrigem.setOcupacaoAtual(50);

        CentroComunitario centroDestino = new CentroComunitario();
        centroDestino.setId("centro2");
        centroDestino.setCapacidadeMaxima(100);
        centroDestino.setOcupacaoAtual(30);

        when(centroComunitarioRepository.findById("centro1")).thenReturn(Optional.of(centroOrigem));
        when(centroComunitarioRepository.findById("centro2")).thenReturn(Optional.of(centroDestino));

        when(negociacaoRepository.save(any(Negociacao.class))).thenReturn(new Negociacao());

        NegociacaoDTO dto = new NegociacaoDTO();
        dto.setCentroOrigemId("centro1");
        dto.setCentroDestinoId("centro2");

        RecursoDTO recursoOrigem = new RecursoDTO();
        recursoOrigem.setTipo("Voluntário");
        recursoOrigem.setQuantidade(3);
        recursoOrigem.setPontos(3);

        RecursoDTO recursoDestino1 = new RecursoDTO();
        recursoDestino1.setTipo("Médico");
        recursoDestino1.setQuantidade(1);
        recursoDestino1.setPontos(4);

        RecursoDTO recursoDestino2 = new RecursoDTO();
        recursoDestino2.setTipo("Cesta Básica");
        recursoDestino2.setQuantidade(1);
        recursoDestino2.setPontos(5);

        dto.setRecursosTrocaOrigem(Arrays.asList(recursoOrigem));
        dto.setRecursosTrocaDestino(Arrays.asList(recursoDestino1, recursoDestino2));

        Negociacao negociacao = negociacaoService.realizarNegociacao(dto);

        assertNotNull(negociacao);
        verify(negociacaoRepository, times(1)).save(any(Negociacao.class));
    }


    @Test
    void testRealizarNegociacaoCentroNaoEncontrado() {
        when(centroComunitarioRepository.findById("centro1")).thenReturn(Optional.empty());

        NegociacaoDTO dto = new NegociacaoDTO();
        dto.setCentroOrigemId("centro1");
        dto.setCentroDestinoId("centro2");

        RecursoDTO recursoOrigem = new RecursoDTO();
        recursoOrigem.setTipo("Voluntário");
        recursoOrigem.setQuantidade(2);
        recursoOrigem.setPontos(3);

        RecursoDTO recursoDestino = new RecursoDTO();
        recursoDestino.setTipo("Médico");
        recursoDestino.setQuantidade(1);
        recursoDestino.setPontos(4);

        dto.setRecursosTrocaOrigem(Arrays.asList(recursoOrigem));
        dto.setRecursosTrocaDestino(Arrays.asList(recursoDestino));

        try {
            negociacaoService.realizarNegociacao(dto);
        } catch (IllegalArgumentException ex) {
            assertNotNull(ex.getMessage());
            verify(negociacaoRepository, never()).save(any(Negociacao.class));
        }
    }

    @Test
    void testBuscarHistoricoNegociacoes() {
        List<Negociacao> mockNegociacoes = Arrays.asList(new Negociacao());

        when(negociacaoRepository.findByCentroOrigemIdOrCentroDestinoIdAndDataNegociacaoBetween(
                eq("centro1"), eq("centro1"), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(mockNegociacoes);

        List<Negociacao> negociacoes = negociacaoService.buscarHistoricoNegociacoes("centro1", null, null);

        assertEquals(1, negociacoes.size());
    }

    @Test
    void testBuscarHistoricoComDatas() {
        List<Negociacao> mockNegociacoes = Arrays.asList(new Negociacao());

        LocalDateTime startDate = LocalDateTime.of(2024, 9, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 9, 10, 0, 0);

        when(negociacaoRepository.findByCentroOrigemIdOrCentroDestinoIdAndDataNegociacaoBetween(
                "centro1", "centro1", startDate, endDate))
                .thenReturn(mockNegociacoes);

        List<Negociacao> negociacoes = negociacaoService.buscarHistoricoNegociacoes("centro1", startDate, endDate);

        assertEquals(1, negociacoes.size());
    }
}
