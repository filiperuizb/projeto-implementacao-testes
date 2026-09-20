package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void testCliente() {
        Cliente cliente = new Cliente(true, false, 2);
        assertEquals(true, cliente.vip());
        assertEquals(false, cliente.bloqueado());
        assertEquals(2, cliente.comprasAnteriores());
    }

    @Test
    void testHistoricoInvalido() {
        try {
            new Cliente(false, false, -1);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Histórico inválido", e.getMessage());
        }
    }
}
