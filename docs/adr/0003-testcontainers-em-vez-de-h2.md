# ADR 0003: Testcontainers (PostgreSQL real) em vez de H2 nos testes

- **Status:** Aceito

## Contexto

O núcleo do projeto depende de comportamento do banco: restrições de unicidade para
idempotência, transações e concorrência. Um banco em memória diferente do de produção pode
passar nos testes e falhar em produção, ou o contrário.

## Decisão

- Os testes de integração rodam contra um **PostgreSQL real** via Testcontainers, na mesma
  família de versão usada no `docker-compose.yml`.
- O schema pertence ao **Flyway** (`ddl-auto: validate`). Todo teste de integração aplica as
  migrations reais em um banco limpo, então as migrations também ficam testadas.

## Alternativas consideradas

- **H2 em modo PostgreSQL:** mais rápido, mas diverge em locking, tipos e mensagens de erro,
  justamente onde o projeto precisa de garantias.

## Consequências

- Os testes exigem **Docker** em execução (local e no CI).
- A primeira execução é mais lenta (download da imagem); as seguintes são rápidas.
- Os testes de duplicata concorrente da Fase 5 serão confiáveis, pois usam a mesma
  restrição `UNIQUE` do banco de produção.