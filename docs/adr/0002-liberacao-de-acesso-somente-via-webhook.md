# ADR 0002: Liberação de acesso somente via webhook

- **Status:** Aceito

## Contexto

Após pagar no Stripe Checkout, o usuário pode fechar o navegador antes de voltar ao nosso
site. Se o acesso dependesse do redirecionamento (`success_url`), haveria usuários que
pagaram e ficaram sem acesso, ou o contrário, com acesso liberado por uma URL forjada.

## Decisão

- O acesso ao conteúdo é liberado **exclusivamente** quando o webhook do Stripe é recebido,
  **verificado** (assinatura `Stripe-Signature` sobre o payload bruto) e processado.
- A página de sucesso é apenas informativa e **não altera nenhum estado**.

## Consequências

- O sistema fica correto mesmo se o usuário nunca voltar ao site.
- Precisamos tratar entrega duplicada (idempotência) e eventos fora de ordem.
  Essas decisões serão registradas em ADRs próprios nas Fases 4 e 5.