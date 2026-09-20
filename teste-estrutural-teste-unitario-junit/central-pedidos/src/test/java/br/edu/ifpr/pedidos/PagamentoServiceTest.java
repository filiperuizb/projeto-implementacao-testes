package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PagamentoServiceTest {

    @Test
    void testPagar() {
        int[] chamadas = {0};

        PagamentoService ok = new PagamentoService(total -> {
            chamadas[0]++;
            assertEquals(100L, total);
            return true;
        });
        assertEquals(true, ok.pagar(100, 3));
        assertEquals(1, chamadas[0]);

        PagamentoService recusa = new PagamentoService(total -> false);
        assertEquals(false, recusa.pagar(50, 2));

        int[] tentativas = {0};
        PagamentoService retry = new PagamentoService(total -> {
            tentativas[0]++;
            if (tentativas[0] < 3) throw new IllegalStateException();
            return true;
        });
        assertEquals(true, retry.pagar(80, 3));
        assertEquals(3, tentativas[0]);

        int[] esgotadas = {0};
        PagamentoService esgota = new PagamentoService(total -> {
            esgotadas[0]++;
            throw new IllegalStateException();
        });
        assertEquals(false, esgota.pagar(80, 3));
        assertEquals(3, esgotadas[0]);
    }

    @Test
    void testExcecoes() {
        PagamentoService service = new PagamentoService(total -> true);
        try {
            service.pagar(0, 1);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Total deve ser positivo", e.getMessage());
        }
        try {
            service.pagar(10, 0);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Use 1 a 3 tentativas", e.getMessage());
        }
        try {
            service.pagar(10, 4);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Use 1 a 3 tentativas", e.getMessage());
        }
        PagamentoService outroErro = new PagamentoService(total -> {
            throw new RuntimeException("falha");
        });
        try {
            outroErro.pagar(10, 3);
            assertEquals("excecao", "nao lancou");
        } catch (RuntimeException e) {
            assertEquals("falha", e.getMessage());
        }
        try {
            new PagamentoService(null);
            assertEquals("excecao", "nao lancou");
        } catch (NullPointerException e) {
            assertEquals(true, e != null);
        }
    }
}
