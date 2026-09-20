package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoTest {

    @Test
    void testItem() {
        ItemPedido item = new ItemPedido("SKU", 1_000, 2, 5, 100, false);
        assertEquals("SKU", item.sku());
        assertEquals(2_000L, item.totalCentavos());
        assertEquals(true, item.disponivel());

        ItemPedido semEstoque = new ItemPedido("SKU", 1_000, 3, 2, 100, false);
        assertEquals(false, semEstoque.disponivel());

        ItemPedido inativo = new ItemPedido("SKU", 1, 0, 0, 1, true);
        assertEquals(0L, inativo.totalCentavos());
        assertEquals(true, inativo.disponivel());
    }

    @Test
    void testExcecoes() {
        try {
            new ItemPedido(null, 1_000, 1, 1, 100, false);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("SKU obrigatório", e.getMessage());
        }
        try {
            new ItemPedido("  ", 1_000, 1, 1, 100, false);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("SKU obrigatório", e.getMessage());
        }
        try {
            new ItemPedido("SKU", 0, 1, 1, 100, false);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Preço inválido", e.getMessage());
        }
        try {
            new ItemPedido("SKU", 1_000_001, 1, 1, 100, false);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Preço inválido", e.getMessage());
        }
        try {
            new ItemPedido("SKU", 1_000, -1, 1, 100, false);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Quantidade inválida", e.getMessage());
        }
        try {
            new ItemPedido("SKU", 1_000, 101, 1, 100, false);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Quantidade inválida", e.getMessage());
        }
        try {
            new ItemPedido("SKU", 1_000, 1, -1, 100, false);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Estoque inválido", e.getMessage());
        }
        try {
            new ItemPedido("SKU", 1_000, 1, 1, 0, false);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Peso inválido", e.getMessage());
        }
        try {
            new ItemPedido("SKU", 1_000, 1, 1, 100_001, false);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Peso inválido", e.getMessage());
        }
    }
}
