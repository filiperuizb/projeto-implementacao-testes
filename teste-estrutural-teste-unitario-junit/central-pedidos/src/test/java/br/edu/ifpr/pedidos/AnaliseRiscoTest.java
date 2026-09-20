package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnaliseRiscoTest {

    @Test
    void testAvaliar() {
        AnaliseRisco analiseRisco = new AnaliseRisco();

        Cliente cliente1 = new Cliente(false, false, 0);
        assertEquals("REVISAO", analiseRisco.avaliar(cliente1, 150_000, false));

        Cliente cliente2 = new Cliente(false, false, 0);
        assertEquals("REVISAO", analiseRisco.avaliar(cliente2, 50_000, true));

        Cliente cliente3 = new Cliente(false, false, 5);
        assertEquals("REVISAO", analiseRisco.avaliar(cliente3, 600_000, false));

        Cliente cliente4 = new Cliente(false, true, 5);
        assertEquals("RECUSADO", analiseRisco.avaliar(cliente4, 100_000, false));

        Cliente cliente5 = new Cliente(true, false, 5);
        assertEquals("APROVADO", analiseRisco.avaliar(cliente5, 600_000, false));

        Cliente cliente6 = new Cliente(false, false, 0);
        assertEquals("APROVADO", analiseRisco.avaliar(cliente6, 100_000, false));

        Cliente cliente7 = new Cliente(false, false, 1);
        assertEquals("APROVADO", analiseRisco.avaliar(cliente7, 500_000, false));

        Cliente cliente8 = new Cliente(false, false, 0);
        assertEquals("REVISAO", analiseRisco.avaliar(cliente8, 150_000, true));
    }

    @Test
    void testTotalNegativo() {
        AnaliseRisco analiseRisco = new AnaliseRisco();
        Cliente cliente = new Cliente(false, false, 0);
        try {
            analiseRisco.avaliar(cliente, -1, false);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Total negativo", e.getMessage());
        }
    }
}
