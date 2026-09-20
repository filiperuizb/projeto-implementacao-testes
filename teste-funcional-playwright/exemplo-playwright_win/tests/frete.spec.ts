import { test, expect, type Page } from '@playwright/test';

async function calcularFrete(page: Page, cep: string, valor: string) {
  await page.goto('/frete');
  await page.getByLabel('CEP').fill(cep);
  await page.getByLabel('Valor do pedido').fill(valor);
  await page.getByRole('button', { name: 'Calcular frete' }).click();
}

const casosValidos = [
  { cep: '81000000', valor: '100', mensagem: 'Frete: R$ 15,00', classe: 'CEP iniciado por 8' },
  { cep: '01310100', valor: '100', mensagem: 'Frete: R$ 25,00', classe: 'CEP de outro prefixo' },
  { cep: '80000000', valor: '100', mensagem: 'Frete: R$ 15,00', classe: 'limite inferior do prefixo 8' },
  { cep: '89999999', valor: '100', mensagem: 'Frete: R$ 15,00', classe: 'limite superior do prefixo 8' },
  { cep: '79999999', valor: '100', mensagem: 'Frete: R$ 25,00', classe: 'imediatamente abaixo do prefixo 8' },
  { cep: '90000000', valor: '100', mensagem: 'Frete: R$ 25,00', classe: 'imediatamente acima do prefixo 8' },
  { cep: '81000000', valor: '199,99', mensagem: 'Frete: R$ 15,00', classe: 'abaixo do limite de frete grátis, CEP 8' },
  { cep: '01310100', valor: '199,99', mensagem: 'Frete: R$ 25,00', classe: 'abaixo do limite de frete grátis, outro CEP' },
  { cep: '81000000', valor: '200', mensagem: 'Frete grátis', classe: 'limite de frete grátis inteiro, CEP 8' },
  { cep: '01310100', valor: '200', mensagem: 'Frete grátis', classe: 'limite de frete grátis inteiro, outro CEP' },
  { cep: '81000000', valor: '200,00', mensagem: 'Frete grátis', classe: 'limite de frete grátis com centavos, CEP 8' },
  { cep: '01310100', valor: '200,00', mensagem: 'Frete grátis', classe: 'limite de frete grátis com centavos, outro CEP' },
  { cep: '81000000', valor: '200,01', mensagem: 'Frete grátis', classe: 'acima do limite de frete grátis, CEP 8' },
  { cep: '01310100', valor: '200,01', mensagem: 'Frete grátis', classe: 'acima do limite de frete grátis, outro CEP' },
  { cep: '80000000', valor: '0,01', mensagem: 'Frete: R$ 15,00', classe: 'menor valor positivo' },
  { cep: '80000000', valor: '150.50', mensagem: 'Frete: R$ 15,00', classe: 'separador decimal ponto' },
  { cep: '80000000', valor: '150,50', mensagem: 'Frete: R$ 15,00', classe: 'separador decimal vírgula' },
  { cep: '01310100', valor: '50', mensagem: 'Frete: R$ 25,00', classe: 'valor inteiro' },
];

const casosInvalidos = [
  { cep: '1234567', valor: '100', classe: 'CEP com 7 dígitos' },
  { cep: '123456789', valor: '100', classe: 'CEP com 9 dígitos' },
  { cep: '8000000a', valor: '100', classe: 'CEP com letra' },
  { cep: '80000-000', valor: '100', classe: 'CEP com hífen' },
  { cep: '', valor: '100', classe: 'CEP vazio' },
  { cep: '80000000', valor: '0', classe: 'valor zero inteiro' },
  { cep: '80000000', valor: '0,00', classe: 'valor zero com centavos' },
  { cep: '80000000', valor: '-10', classe: 'valor negativo' },
  { cep: '80000000', valor: 'abc', classe: 'valor com tipo inválido' },
  { cep: '80000000', valor: '', classe: 'valor vazio' },
  { cep: '', valor: '', classe: 'CEP e valor vazios' },
];

for (const caso of casosValidos) {
  test(`CEP ${caso.cep} valor ${caso.valor} — ${caso.classe}`, async ({ page }) => {
    await calcularFrete(page, caso.cep, caso.valor);

    const resultado = page.locator('#resultado');
    await expect(resultado).toBeVisible();
    await expect(resultado).toHaveText(caso.mensagem);
    await expect(resultado).toHaveAttribute('role', 'status');
  });
}

for (const caso of casosInvalidos) {
  test(`CEP ${caso.cep || '(vazio)'} valor ${caso.valor || '(vazio)'} — ${caso.classe}`, async ({ page }) => {
    await calcularFrete(page, caso.cep, caso.valor);

    const resultado = page.locator('#resultado');
    await expect(resultado).toBeVisible();
    await expect(resultado).toHaveText('Dados inválidos');
    await expect(resultado).toHaveAttribute('role', 'alert');
  });
}

test.describe('comportamento do formulário de frete', () => {
  test('resultado fica oculto antes do envio', async ({ page }) => {
    await page.goto('/frete');

    await expect(page.locator('#resultado')).toBeHidden();
  });

  test('frete grátis não mascara CEP inválido', async ({ page }) => {
    await calcularFrete(page, '1234567', '500');

    const resultado = page.locator('#resultado');
    await expect(resultado).toHaveText('Dados inválidos');
    await expect(resultado).toHaveAttribute('role', 'alert');
  });

  test('corrigir os dados após um erro substitui a mensagem e o papel', async ({ page }) => {
    await calcularFrete(page, '1234567', '100');
    const resultado = page.locator('#resultado');
    await expect(resultado).toHaveText('Dados inválidos');

    await page.getByLabel('CEP').fill('80000000');
    await page.getByRole('button', { name: 'Calcular frete' }).click();

    await expect(resultado).toHaveText('Frete: R$ 15,00');
    await expect(resultado).toHaveAttribute('role', 'status');
  });
});
