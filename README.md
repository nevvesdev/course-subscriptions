# course-subscriptions

[![CI](https://github.com/nevvesdev/course-subscriptions/actions/workflows/ci.yml/badge.svg)](https://github.com/nevvesdev/course-subscriptions/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.2.0-6db33f)

API REST de uma plataforma de cursos por assinatura, com pagamentos via **Stripe (modo teste)**.

> **Status:** em desenvolvimento (Fase 0 concluída: scaffold, decisões e CI).

## O problema que este projeto resolve

Integrar um sistema de pagamento é fácil quando tudo funciona. O difícil é quando algo falha:

- O usuário paga e **fecha o navegador** antes de voltar ao site. Como o sistema descobre o pagamento?
- O Stripe reenvia a mesma notificação. Como **não estender a assinatura duas vezes**?
- Os eventos chegam **fora de ordem**. Como não aplicar um estado antigo por cima de um novo?

O domínio (cursos, planos, conteúdo protegido) é propositalmente simples. O foco é a camada
de cobrança: **webhooks verificados, idempotência, tratamento de falhas e testes automatizados**.

## Fluxo central

1. O usuário se registra e escolhe um plano (mensal ou anual).
2. É redirecionado ao Stripe Checkout (modo teste) e paga.
3. O Stripe notifica o backend via **webhook**.
4. O backend verifica a assinatura, processa o evento **uma única vez** e libera o acesso.

O redirecionamento do navegador **nunca** libera acesso. Veja o
[ADR 0002](docs/adr/0002-liberacao-de-acesso-somente-via-webhook.md).

## Stack

- Java 21 e Spring Boot 4.2.0 (Maven)
- PostgreSQL e Flyway (Fase 1)
- Spring Security com JWT (Fase 2)
- Stripe Java SDK e Stripe CLI (Fases 3 e 4)
- JUnit 5, Testcontainers, WireMock e MockMvc
- Docker Compose e GitHub Actions
- springdoc-openapi (Swagger UI)

## Arquitetura

Monólito em **camadas simples**: `config`, `controller`, `service`, `domain`, `infrastructure`
e `exception`, com módulos `identity`, `catalog`, `billing` e `content`.
Detalhes e justificativas em [`docs/adr`](docs/adr).

## Como executar

Pré-requisitos: JDK 21 e Docker com `docker-compose`.

```bash
# 1. sobe o PostgreSQL (valores padrão de desenvolvimento, sem precisar de .env)
docker-compose up -d

# 2. sobe a aplicação
./mvnw spring-boot:run
```

Verificação de saúde:

```bash
curl http://localhost:8080/actuator/health
```

Variáveis de ambiente: veja `.env.example`. Copie para `.env` apenas se quiser sobrescrever
os padrões (nunca versione o `.env`).

Para derrubar o banco: `docker-compose down` (acrescente `-v` para apagar os dados).

## Como executar os testes

Requer Docker em execução: os testes de integração sobem um PostgreSQL descartável via
Testcontainers e aplicam as migrations reais.

```bash
./mvnw verify
```

## Roadmap

- [x] **Fase 0:** scaffold, ADRs iniciais, estrutura em camadas e CI
- [x] **Fase 1:** Docker Compose, PostgreSQL, Flyway e primeiro teste de integração
- [ ] **Fase 2:** usuários, autenticação e catálogo de planos
- [ ] **Fase 3:** Checkout Session e assinatura pendente
- [ ] **Fase 4:** webhook com verificação de assinatura, idempotência e liberação de acesso
- [ ] **Fase 5:** falhas (pagamento recusado, cancelamento, eventos fora de ordem, duplicata concorrente)
- [ ] **Fase 6:** acabamento (demo, cobertura e documentação final)

## Limitações conhecidas

Esta seção é mantida desde o início, de propósito.

- Não haverá frontend; a demonstração será via Swagger UI e Stripe CLI.
- Autenticação será JWT simples, sem OAuth e sem refresh token.
- O processamento do webhook será síncrono; processamento assíncrono com fila fica como evolução.
- Apenas Stripe em modo teste; nenhum dinheiro real é movimentado.
- Os testes de integração exigem Docker em execução.
- Ainda não há funcionalidades de negócio, apenas a infraestrutura de persistência.

## Desenvolvido por

João Victor · [GitHub](https://github.com/nevvesdev) · [LinkedIn](https://www.linkedin.com/in/nevvesdev/)

## Licença

Distribuído sob a licença MIT. Veja o arquivo [LICENSE](LICENSE).