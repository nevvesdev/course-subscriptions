# ADR 0001: Arquitetura em camadas simples

- **Status:** Aceito

## Contexto

O foco do projeto é a camada de cobrança (webhooks, idempotência, falhas), e não a
sofisticação arquitetural. Preciso de uma estrutura legível, fácil de manter e que
permita evoluir no futuro sem reescrever tudo.

## Decisão

Monólito com **arquitetura em camadas**, com pacotes organizados primeiro por camada e,
dentro de cada uma, por módulo de negócio (`identity`, `catalog`, `billing`, `content`):

```
br.com.nevvesdev.coursesubs
├── config            # beans e configurações do Spring
├── controller        # camada HTTP (requests, responses, validação de entrada)
├── service           # casos de uso e regras de aplicação (transações)
├── domain            # entidades, enums, regras de negócio e interfaces (repositórios, portas)
├── infrastructure    # implementações técnicas (Stripe, segurança)
└── exception         # exceções de negócio e tratamento global de erros
```

**Regra de dependência:** `controller → service → domain`. A `infrastructure` implementa
interfaces definidas no `domain` (ex.: `PaymentGateway`), e o `domain` nunca depende de
`controller` ou de `infrastructure`.

Outras decisões de estilo: records para DTOs, injeção por construtor e **sem Lombok**
(menos mágica e menos atrito com versões novas do JDK e do Spring).

## Alternativas consideradas

- **Clean/Hexagonal completa:** isolamento maior, mas cerimônia excessiva para este escopo.
- **Pacote por funcionalidade (feature-first):** boa coesão, mas o objetivo aqui é deixar as
  camadas explícitas para quem lê o repositório.

## Consequências

- Estrutura previsível e fácil de navegar.
- O Stripe fica atrás de uma interface, o que permite testar sem rede.
- Se algum módulo crescer, é possível extraí-lo para um módulo próprio, já que as fronteiras
  por pacote existem desde o início.