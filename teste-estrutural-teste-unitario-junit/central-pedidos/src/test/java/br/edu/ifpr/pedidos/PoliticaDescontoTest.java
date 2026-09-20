package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PoliticaDescontoTest {

    @Test
    void testCalcular() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente vip = new Cliente(true, false, 1);
        Cliente comum = new Cliente(false, false, 1);
        Cliente novo = new Cliente(false, false, 0);
        Cliente vipNovo = new Cliente(true, false, 0);

        assertEquals(1_000L, politica.calcular(vip, 10_000, null));
        assertEquals(2_500L, politica.calcular(comum, 50_000, null));
        assertEquals(0L, politica.calcular(comum, 49_999, null));
        assertEquals(0L, politica.calcular(comum, 10_000, "   "));
        assertEquals(2_000L, politica.calcular(novo, 10_000, "BEMVINDO"));
        assertEquals(2_000L, politica.calcular(novo, 10_000, " bemvindo "));
        assertEquals(0L, politica.calcular(comum, 10_000, "BEMVINDO"));
        assertEquals(0L, politica.calcular(novo, 9_999, "BEMVINDO"));
        assertEquals(2_000L, politica.calcular(comum, 20_000, "EXTRA10"));
        assertEquals(0L, politica.calcular(comum, 19_999, "EXTRA10"));
        assertEquals(2_000L, politica.calcular(vipNovo, 10_000, "BEMVINDO"));
        assertEquals(4_000L, politica.calcular(vip, 20_000, "EXTRA10"));
    }

    @Test
    void testExcecoes() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente comum = new Cliente(false, false, 1);
        try {
            politica.calcular(comum, -1, null);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Subtotal negativo", e.getMessage());
        }
        try {
            politica.calcular(comum, 10_000, "FOO");
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Cupom desconhecido", e.getMessage());
        }
    }
}
