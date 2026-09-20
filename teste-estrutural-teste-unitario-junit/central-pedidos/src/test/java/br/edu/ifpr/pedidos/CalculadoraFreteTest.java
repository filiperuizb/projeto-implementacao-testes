package br.edu.ifpr.pedidos;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraFreteTest {

    @Test
    void testCalcular() {
        CalculadoraFrete calc = new CalculadoraFrete();
        Cliente comum = new Cliente(false, false, 1);
        Cliente vip = new Cliente(true, false, 1);
        ItemPedido leve = new ItemPedido("A", 10_000, 1, 5, 1_000, false);
        ItemPedido peso2000 = new ItemPedido("A", 10_000, 1, 5, 2_000, false);
        ItemPedido peso2001 = new ItemPedido("A", 10_000, 1, 5, 2_001, false);
        ItemPedido peso3001 = new ItemPedido("A", 10_000, 1, 5, 3_001, false);
        ItemPedido fragil = new ItemPedido("A", 10_000, 1, 5, 1_000, true);

        Pedido pr = new Pedido(List.of(leve), "PR", false, null);
        Pedido sp = new Pedido(List.of(leve), "SP", false, null);
        Pedido rj = new Pedido(List.of(leve), "RJ", false, null);
        Pedido mg = new Pedido(List.of(leve), "MG", false, null);
        Pedido prExp = new Pedido(List.of(leve), "PR", true, null);
        Pedido prFrag = new Pedido(List.of(fragil), "PR", false, null);

        assertEquals(1_200L, calc.calcular(pr, comum, 10_000));
        assertEquals(2_000L, calc.calcular(sp, comum, 10_000));
        assertEquals(2_000L, calc.calcular(rj, comum, 10_000));
        assertEquals(3_000L, calc.calcular(mg, comum, 10_000));
        assertEquals(1_200L, calc.calcular(new Pedido(List.of(peso2000), "PR", false, null), comum, 10_000));
        assertEquals(1_500L, calc.calcular(new Pedido(List.of(peso2001), "PR", false, null), comum, 10_000));
        assertEquals(1_800L, calc.calcular(new Pedido(List.of(peso3001), "PR", false, null), comum, 10_000));
        assertEquals(0L, calc.calcular(pr, comum, 30_000));
        assertEquals(1_200L, calc.calcular(pr, comum, 29_999));
        assertEquals(2_700L, calc.calcular(prExp, comum, 30_000));
        assertEquals(600L, calc.calcular(pr, vip, 10_000));
        assertEquals(1_700L, calc.calcular(prFrag, comum, 10_000));
        assertEquals(500L, calc.calcular(prFrag, vip, 30_000));
    }

    @Test
    void testLiquidoNegativo() {
        CalculadoraFrete calc = new CalculadoraFrete();
        Cliente comum = new Cliente(false, false, 1);
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 10_000, 1, 5, 1_000, false)), "PR", false, null);
        try {
            calc.calcular(pedido, comum, -1);
            assertEquals("excecao", "nao lancou");
        } catch (IllegalArgumentException e) {
            assertEquals("Valor líquido negativo", e.getMessage());
        }
    }
}
