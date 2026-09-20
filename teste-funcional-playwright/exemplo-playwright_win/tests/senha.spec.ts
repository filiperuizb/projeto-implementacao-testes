import { test, expect, type Page } from '@playwright/test';

function criarSenhaComTamanho(tamanho: number) {
  return 'Aa1' + 'x'.repeat(tamanho - 3);
}

async function cadastrarSenha(page: Page, senha: string, confirmacao: string) {
  await page.goto('/senha');
  await page.getByLabel('Nova senha').fill(senha);
  await page.getByLabel('Confirmar senha').fill(confirmacao);
  await page.getByRole('button', { name: 'Cadastrar senha' }).click();
}

const senhaValida = 'Senha1234';

const casos = [
  { senha: criarSenhaComTamanho(8), confirmacao: criarSenhaComTamanho(8), mensagem: 'Senha cadastrada', classe: 'limite mínimo de 8 caracteres' },
  { senha: criarSenhaComTamanho(9), confirmacao: criarSenhaComTamanho(9), mensagem: 'Senha cadastrada', classe: 'acima do mínimo, 9 caracteres' },
  { senha: criarSenhaComTamanho(19), confirmacao: criarSenhaComTamanho(19), mensagem: 'Senha cadastrada', classe: 'abaixo do máximo, 19 caracteres' },
  { senha: criarSenhaComTamanho(20), confirmacao: criarSenhaComTamanho(20), mensagem: 'Senha cadastrada', classe: 'limite máximo de 20 caracteres' },
  { senha: 'Senha@1234', confirmacao: 'Senha@1234', mensagem: 'Senha cadastrada', classe: 'caractere especial permitido' },

  { senha: criarSenhaComTamanho(7), confirmacao: criarSenhaComTamanho(7), mensagem: 'Senha fora do padrão', classe: 'abaixo do mínimo, 7 caracteres' },
  { senha: criarSenhaComTamanho(21), confirmacao: criarSenhaComTamanho(21), mensagem: 'Senha fora do padrão', classe: 'acima do máximo, 21 caracteres' },
  { senha: 'abcdefg1', confirmacao: 'abcdefg1', mensagem: 'Senha fora do padrão', classe: 'sem letra maiúscula' },
  { senha: 'ABCDEFG1', confirmacao: 'ABCDEFG1', mensagem: 'Senha fora do padrão', classe: 'sem letra minúscula' },
  { senha: 'Abcdefgh', confirmacao: 'Abcdefgh', mensagem: 'Senha fora do padrão', classe: 'sem número' },
  { senha: 'Abcd 1234', confirmacao: 'Abcd 1234', mensagem: 'Senha fora do padrão', classe: 'espaço no meio' },
  { senha: 'Abcdefg1 ', confirmacao: 'Abcdefg1 ', mensagem: 'Senha fora do padrão', classe: 'espaço no fim' },
  { senha: ' Abcdefg1', confirmacao: ' Abcdefg1', mensagem: 'Senha fora do padrão', classe: 'espaço no início' },
  { senha: '', confirmacao: '', mensagem: 'Senha fora do padrão', classe: 'senha vazia' },

  { senha: senhaValida, confirmacao: 'Senha4321', mensagem: 'As senhas não coincidem', classe: 'confirmação diferente' },
  { senha: senhaValida, confirmacao: '', mensagem: 'As senhas não coincidem', classe: 'confirmação vazia' },
  { senha: senhaValida, confirmacao: 'senha1234', mensagem: 'As senhas não coincidem', classe: 'confirmação difere só na caixa' },
];

for (const caso of casos) {
  test(`senha ${caso.senha.length} caracteres "${caso.senha || '(vazia)'}" confirmação "${caso.confirmacao || '(vazia)'}" — ${caso.classe}`, async ({ page }) => {
    await cadastrarSenha(page, caso.senha, caso.confirmacao);

    const resultado = page.locator('#resultado');
    await expect(resultado).toBeVisible();
    await expect(resultado).toHaveText(caso.mensagem);
    await expect(resultado).toHaveAttribute('role', caso.mensagem === 'Senha cadastrada' ? 'status' : 'alert');
  });
}

test.describe('comportamento do formulário de senha', () => {
  test('resultado fica oculto antes do envio', async ({ page }) => {
    await page.goto('/senha');

    await expect(page.locator('#resultado')).toBeHidden();
  });

  test('formato inválido tem precedência sobre confirmação divergente', async ({ page }) => {
    await cadastrarSenha(page, 'abcdefg1', senhaValida);

    const resultado = page.locator('#resultado');
    await expect(resultado).toHaveText('Senha fora do padrão');
    await expect(resultado).toHaveAttribute('role', 'alert');
  });

  test('campos são limpos após o cadastro com sucesso', async ({ page }) => {
    await cadastrarSenha(page, senhaValida, senhaValida);
    await expect(page.locator('#resultado')).toHaveText('Senha cadastrada');

    await expect(page.getByLabel('Nova senha')).toHaveValue('');
    await expect(page.getByLabel('Confirmar senha')).toHaveValue('');
  });

  test('corrigir a confirmação após um erro substitui a mensagem e o papel', async ({ page }) => {
    await cadastrarSenha(page, senhaValida, 'Senha4321');
    const resultado = page.locator('#resultado');
    await expect(resultado).toHaveText('As senhas não coincidem');

    await page.getByLabel('Confirmar senha').fill(senhaValida);
    await page.getByRole('button', { name: 'Cadastrar senha' }).click();

    await expect(resultado).toHaveText('Senha cadastrada');
    await expect(resultado).toHaveAttribute('role', 'status');
  });
});
