package com.centrocomunitario.api.service;

import com.centrocomunitario.api.dto.AtualizarOcupacaoDTO;
import com.centrocomunitario.api.dto.CentroComunitarioDTO;
import com.centrocomunitario.api.model.CentroComunitario;
import com.centrocomunitario.api.repository.CentroComunitarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CentroComunitarioServiceTest {

    @Mock
    private CentroComunitarioRepository centroComunitarioRepository;

    @InjectMocks
    private CentroComunitarioService centroComunitarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAdicionarCentro() {
        when(centroComunitarioRepository.save(any(CentroComunitario.class))).thenReturn(new CentroComunitario());

        CentroComunitarioDTO dto = new CentroComunitarioDTO();
        dto.setNome("Centro Comunitário 1");
        dto.setCapacidadeMaxima(100);
        dto.setOcupacaoAtual(50);
        dto.setRecursos(Collections.emptyList());

        CentroComunitario centroComunitario = centroComunitarioService.adicionarCentro(dto);

        assertNotNull(centroComunitario);
        verify(centroComunitarioRepository, times(1)).save(any(CentroComunitario.class));
    }

    @Test
    void testBuscarCentrosComOcupacaoAlta() {
        CentroComunitario centro1 = new CentroComunitario();
        centro1.setOcupacaoAtual(95);
        centro1.setCapacidadeMaxima(100);

        when(centroComunitarioRepository.findByOcupacaoAtualGreaterThanEqual(90))
                .thenReturn(Arrays.asList(centro1));

        List<CentroComunitario> centros = centroComunitarioService.buscarCentrosComOcupacaoAlta();

        assertEquals(1, centros.size());
        verify(centroComunitarioRepository, times(1)).findByOcupacaoAtualGreaterThanEqual(90);
    }

    @Test
    void testAtualizarOcupacao() {
        CentroComunitario centro = new CentroComunitario();
        centro.setId("centro1");
        centro.setNome("Centro Comunitário 1");
        centro.setCapacidadeMaxima(100);
        centro.setOcupacaoAtual(50);

        when(centroComunitarioRepository.findById("centro1")).thenReturn(Optional.of(centro));
        when(centroComunitarioRepository.save(any(CentroComunitario.class))).thenReturn(centro);

        AtualizarOcupacaoDTO dto = new AtualizarOcupacaoDTO();
        dto.setOcupacaoAtual(60);

        CentroComunitario centroAtualizado = centroComunitarioService.atualizarOcupacao("centro1", dto);

        assertNotNull(centroAtualizado);
        assertEquals(60, centroAtualizado.getOcupacaoAtual());
        verify(centroComunitarioRepository, times(1)).save(any(CentroComunitario.class));
    }
}
