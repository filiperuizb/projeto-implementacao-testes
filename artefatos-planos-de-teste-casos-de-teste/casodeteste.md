**INTEGRANTES**

Nome: Filipe Ruiz Boligon - RA:24042069-2 

Nome: Filipe Senra Gonçalves - RA: 24170444-2

Nome: Gabriel Arantes - RA: 24169751-2

Nome: Thiago Fernando Zulli - RA:24000965-2

**PLANO DE TESTE E CASOS DE TESTE**

**1 PLANO DE TESTE**

**1.1 ESCOPO**
O escopo contempla a validação das regras de negócio do módulo de reservas, o que inclui a compatibilidade de capacidades e recursos das salas, gestão de datas, horários, turmas e responsáveis. Abrange a verificação de restrições de conflitos, manutenção, limites de horários, permissões de perfis de usuário, notificações, histórico e trilha de auditoria.

**1.2 RISCOS MITIGADOS**
A execução visa garantir a segurança da operação contra a dupla ocupação, alocação com capacidade insegura, alteração de dados sem autorização e falha no disparo de notificações.

**2 CASOS DE TESTE**

**2.1 CT-01: Reserva de sala disponível**
**Objetivo:** Validar a reserva de sala para turma compatível (RF-01).
**Pré-condições:** Sala com capacidade e recursos adequados encontra-se disponível no horário desejado.
**Passos:**

1. Selecionar a sala desejada.


2. Informar data, horário, turma e responsável.


3. Confirmar a reserva.
**Resultado Esperado:** A reserva é efetivada e a sala consta como ocupada no período.



**2.2 CT-02: Impedimento de sobreposição**
**Objetivo:** Evitar a dupla ocupação na mesma sala (RF-02, Risco Crítico).
**Pré-condições:** A sala possui uma reserva confirmada para o horário.
**Passos:**

1. Iniciar nova reserva para a mesma sala.


2. Inserir o mesmo horário da reserva existente.


3. Confirmar a operação.
**Resultado Esperado:** O sistema bloqueia a ação e exibe alerta de conflito de horário.



**2.3 CT-03: Validação de capacidade**
**Objetivo:** Bloquear turmas maiores que a capacidade da sala (RF-03, Risco Crítico).
**Pré-condições:** A sala selecionada possui limite de lotação inferior ao tamanho da turma.
**Passos:**

1. Selecionar a sala.


2. Informar uma turma com número de alunos superior à capacidade do local.


3. Confirmar a reserva.
**Resultado Esperado:** O sistema recusa o agendamento por violação de capacidade máxima.



**2.4 CT-04: Restrição por manutenção**
**Objetivo:** Impedir o uso de sala em manutenção (RF-04).
**Pré-condições:** O status da sala está definido como em manutenção.
**Passos:**

1. Tentar selecionar a sala para um novo agendamento.
**Resultado Esperado:** A sala não permite seleção ou o sistema exibe alerta de indisponibilidade.



**2.5 CT-05: Janela de horário permitida**
**Objetivo:** Garantir que as reservas ocorram apenas entre 07h30 e 22h30 (RF-05).
**Pré-condições:** Sistema acessível para nova reserva.
**Passos:**

1. Inserir horário de início anterior às 07h30 ou término posterior às 22h30.


2. Confirmar a reserva.
**Resultado Esperado:** O sistema bloqueia a reserva por estar fora do horário operacional.



**2.6 CT-06: Alteração por coordenação**
**Objetivo:** Validar que apenas a coordenação altera reservas de outros professores (RF-06, Risco Crítico).
**Pré-condições:** Reserva ativa registrada em nome do Professor A. Usuário logado com perfil de Professor B.
**Passos:**

1. Acessar a reserva do Professor A.


2. Tentar alterar os dados da reserva.
**Resultado Esperado:** O sistema bloqueia a edição por falta de permissão.



**2.7 CT-07: Cancelamento e liberação de horário**
**Objetivo:** Verificar a liberação da sala e o registro no histórico após o cancelamento (RF-07).
**Pré-condições:** Reserva ativa selecionada.
**Passos:**

1. Acionar o cancelamento da reserva.


2. Consultar a disponibilidade da sala no mesmo horário.


3. Consultar o histórico da sala.
**Resultado Esperado:** O horário fica disponível para novos agendamentos e o histórico registra o evento de cancelamento.



**2.8 CT-08: Geração de notificações**
**Objetivo:** Validar o disparo de notificações em alterações ou cancelamentos (RF-08, Risco Crítico).
**Pré-condições:** Reserva ativa no sistema.
**Passos:**

1. Efetuar a alteração ou o cancelamento da reserva.
**Resultado Esperado:** O sistema gera e envia a notificação correspondente ao responsável.



**2.9 CT-09: Desempenho da busca**
**Objetivo:** Verificar o tempo de resposta da busca (RNF-01).
**Pré-condições:** Base de dados acessível e em funcionamento normal.
**Passos:**

1. Executar uma busca por salas disponíveis no sistema.
**Resultado Esperado:** Os resultados são exibidos em até 2 segundos.



**2.10 CT-10: Trilha de auditoria**
**Objetivo:** Confirmar o registro das operações na trilha de auditoria (RNF-02).
**Pré-condições:** Operação de reserva, alteração ou cancelamento recém-executada.
**Passos:**

1. Acessar os logs de auditoria do sistema.
**Resultado Esperado:** A operação executada consta na trilha com registro adequado.



**2.11 CT-11: Acesso por unidade**
**Objetivo:** Validar o acesso limitado às unidades autorizadas (RNF-03).
**Pré-condições:** Usuário logado com permissão restrita à Unidade A.
**Passos:**

1. Tentar visualizar ou reservar salas vinculadas à Unidade B.
**Resultado Esperado:** O sistema não exibe as salas da Unidade B ou bloqueia o acesso informando falta de autorização.
