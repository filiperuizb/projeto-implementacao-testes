package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceTest {
    @Test
    void deveFecharPedidoDeClienteComumComFreteDoParanaEPagamentoAprovado() {
        // 1. Preparar: cliente comum, uma compra anterior e item disponível de R$ 100,00.
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("LIVRO-JAVA", 10_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        // Simula o pagamento e registra as cobranças, sem banco ou serviço externo.
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        // 2. Executar: percorrer um caminho completo do fechamento.
        ResultadoPedido resultado = service.fechar(pedido, cliente);

        // 3. Verificar: sem desconto; frete de R$ 12,00; total de R$ 112,00.
        assertAll(
            () -> assertEquals("PAGO", resultado.status()),
            () -> assertEquals(10_000L, resultado.subtotalCentavos()),
            () -> assertEquals(0L, resultado.descontoCentavos()),
            () -> assertEquals(1_200L, resultado.freteCentavos()),
            () -> assertEquals(11_200L, resultado.totalCentavos()),
            // A lista comprova uma única cobrança, com o valor correto.
            () -> assertEquals(List.of(11_200L), cobrancas)
        );
    }

    @Test
    void testClienteBloqueado() {
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });
        Cliente cliente = new Cliente(false, true, 1);
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 10_000, 1, 5, 1_000, false)), "PR", false, "FOO");
        ResultadoPedido resultado = service.fechar(pedido, cliente);
        assertEquals("BLOQUEADO", resultado.status());
        assertEquals(0L, resultado.subtotalCentavos());
        assertEquals(0L, resultado.descontoCentavos());
        assertEquals(0L, resultado.freteCentavos());
        assertEquals(0L, resultado.totalCentavos());
        assertEquals(0, cobrancas.size());
    }

    @Test
    void testSemEstoque() {
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });
        Cliente cliente = new Cliente(false, false, 1);
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 10_000, 2, 1, 1_000, false)), "PR", false, "FOO");
        ResultadoPedido resultado = service.fechar(pedido, cliente);
        assertEquals("SEM_ESTOQUE", resultado.status());
        assertEquals(0L, resultado.subtotalCentavos());
        assertEquals(0, cobrancas.size());
    }

    @Test
    void testPedidoSemItensAtivos() {
        PedidoService service = new PedidoService(total -> true);
        Cliente cliente = new Cliente(false, false, 1);
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 10_000, 0, 5, 1_000, false)), "PR", false, null);
        try {
            service.fechar(pedido, cliente);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Pedido sem itens ativos", e.getMessage());
        }
    }

    @Test
    void testRevisaoNaoCobra() {
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });
        Cliente cliente = new Cliente(false, false, 0);
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 10_000, 1, 5, 1_000, false)), "PR", true, null);
        ResultadoPedido resultado = service.fechar(pedido, cliente);
        assertEquals("REVISAO", resultado.status());
        assertEquals(10_000L, resultado.subtotalCentavos());
        assertEquals(0L, resultado.descontoCentavos());
        assertEquals(2_700L, resultado.freteCentavos());
        assertEquals(12_700L, resultado.totalCentavos());
        assertEquals(0, cobrancas.size());
    }

    @Test
    void testPagamentoRecusado() {
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return false;
        });
        Cliente cliente = new Cliente(false, false, 1);
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 10_000, 1, 5, 1_000, false)), "PR", false, null);
        ResultadoPedido resultado = service.fechar(pedido, cliente);
        assertEquals("PAGAMENTO_RECUSADO", resultado.status());
        assertEquals(10_000L, resultado.subtotalCentavos());
        assertEquals(0L, resultado.descontoCentavos());
        assertEquals(1_200L, resultado.freteCentavos());
        assertEquals(11_200L, resultado.totalCentavos());
        assertEquals(List.of(11_200L), cobrancas);
    }

    @Test
    void testVipComCupom() {
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });
        Cliente cliente = new Cliente(true, false, 1);
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 20_000, 1, 5, 1_000, false)), "PR", false, "EXTRA10");
        ResultadoPedido resultado = service.fechar(pedido, cliente);
        assertEquals("PAGO", resultado.status());
        assertEquals(20_000L, resultado.subtotalCentavos());
        assertEquals(4_000L, resultado.descontoCentavos());
        assertEquals(600L, resultado.freteCentavos());
        assertEquals(16_600L, resultado.totalCentavos());
        assertEquals(List.of(16_600L), cobrancas);
    }

    @Test
    void testReferenciasNulas() {
        PedidoService service = new PedidoService(total -> true);
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 10_000, 1, 5, 1_000, false)), "PR", false, null);
        Cliente cliente = new Cliente(false, false, 1);
        try {
            service.fechar(null, cliente);
            assertEquals("excecao", "nao lancou");
        } catch (NullPointerException e) {
            assertEquals(true, e != null);
        }
        try {
            service.fechar(pedido, null);
            assertEquals("excecao", "nao lancou");
        } catch (NullPointerException e) {
            assertEquals(true, e != null);
        }
    }
}
