package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    @Test
    void testPedido() {
        ItemPedido ativo = new ItemPedido("A", 10_000, 2, 5, 100, false);
        ItemPedido inativo = new ItemPedido("B", 5_000, 0, 5, 50, true);
        ItemPedido fragil = new ItemPedido("C", 1_000, 1, 1, 200, true);
        Pedido pedido = new Pedido(List.of(ativo, inativo, fragil), "PR", false, "BEMVINDO");

        assertEquals(21_000L, pedido.subtotalCentavos());
        assertEquals(400, pedido.pesoGramas());
        assertEquals(true, pedido.temFragil());
        assertEquals(true, pedido.estoqueSuficiente());
        assertEquals("PR", pedido.uf());
        assertEquals(false, pedido.expresso());
        assertEquals("BEMVINDO", pedido.cupom());
    }

    @Test
    void testCopiaDaListaEInativos() {
        ItemPedido item = new ItemPedido("A", 10_000, 1, 5, 100, false);
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(item);
        Pedido pedido = new Pedido(itens, "SP", true, null);
        itens.add(new ItemPedido("B", 10_000, 1, 5, 100, false));
        assertEquals(1, pedido.itens().size());
        assertEquals(10_000L, pedido.subtotalCentavos());

        Pedido soInativo = new Pedido(List.of(new ItemPedido("A", 10_000, 0, 5, 100, true)), "PR", false, null);
        assertEquals(0L, soInativo.subtotalCentavos());
        assertEquals(false, soInativo.temFragil());
        assertEquals(true, soInativo.estoqueSuficiente());
    }

    @Test
    void testEstoqueInsuficiente() {
        ItemPedido ok = new ItemPedido("A", 10_000, 1, 5, 100, false);
        ItemPedido falta = new ItemPedido("B", 10_000, 3, 2, 100, false);
        Pedido noFim = new Pedido(List.of(ok, falta), "PR", false, null);
        assertEquals(false, noFim.estoqueSuficiente());

        Pedido noInicio = new Pedido(List.of(falta, ok), "PR", false, null);
        assertEquals(false, noInicio.estoqueSuficiente());

        Pedido vazio = new Pedido(List.of(), "PR", false, null);
        assertEquals(0L, vazio.subtotalCentavos());
        assertEquals(0, vazio.pesoGramas());
        assertEquals(false, vazio.temFragil());
        assertEquals(true, vazio.estoqueSuficiente());
    }

    @Test
    void testExcecoes() {
        ItemPedido item = new ItemPedido("A", 10_000, 1, 5, 100, false);
        try {
            new Pedido(null, "PR", false, null);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Lista inválida", e.getMessage());
        }
        List<ItemPedido> muitos = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            muitos.add(item);
        }
        try {
            new Pedido(muitos, "PR", false, null);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Lista inválida", e.getMessage());
        }
        try {
            new Pedido(List.of(item), null, false, null);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("UF inválida", e.getMessage());
        }
        try {
            new Pedido(List.of(item), "pr", false, null);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("UF inválida", e.getMessage());
        }
        try {
            new Pedido(java.util.Arrays.asList((ItemPedido) null), "PR", false, null);
            assertEquals("excecao", "nao lancou");
        } catch (NullPointerException e) {
            assertEquals(true, e != null);
        }
    }
}
