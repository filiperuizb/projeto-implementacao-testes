package br.edu.ifpr.boletim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParticipacaoTest {

    @Test
    void testCalcularPontos() {
        Participacao participacao = new Participacao();
        assertEquals(0, participacao.calcularPontos(false, false));
        assertEquals(2, participacao.calcularPontos(true, false));
        assertEquals(1, participacao.calcularPontos(false, true));
        assertEquals(3, participacao.calcularPontos(true, true));
    }
}
