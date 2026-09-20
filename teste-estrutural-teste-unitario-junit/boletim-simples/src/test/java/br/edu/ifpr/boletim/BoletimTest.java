package br.edu.ifpr.boletim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoletimTest {

    @Test
    void deveAprovarAlunoComMediaOito() {
        Boletim boletim = new Boletim();

        String resultado = boletim.verificarSituacao(8);

        assertEquals("APROVADO", resultado);
    }

    @Test
    void deveRecuperarNotaAlunoComMediaQuatro() {
        Boletim boletim = new Boletim();

        String resultado = boletim.verificarSituacao(4);

        assertEquals("RECUPERACAO", resultado);
    }

    @Test
    void deveReprovarAlunoComMediaDois() {
        Boletim boletim = new Boletim();

        String resultado = boletim.verificarSituacao(2);

        assertEquals("REPROVADO", resultado);
    }

    @Test
    void deveCalcularMediaIgualCinco() {
        Boletim boletim = new Boletim();

        double resultado = boletim.calcularMedia(5,5);

        assertEquals(5,resultado);

    }

    @Test
    void deveContarAprovados() {
        Boletim boletim = new Boletim();
        double[] medias = {8, 6, 7, 5, 9};

        int resultado = boletim.contarAprovados(medias);

        assertEquals(3, resultado);
    }
}
