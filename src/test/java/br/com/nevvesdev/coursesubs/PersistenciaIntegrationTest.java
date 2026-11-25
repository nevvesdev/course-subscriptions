package br.com.nevvesdev.coursesubs;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PersistenciaIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Flyway flyway;

    @Test
    void deveConectarEmUmPostgresReal() {
        var jdbcTemplate = new JdbcTemplate(dataSource);

        String versao = jdbcTemplate.queryForObject("select version()", String.class);

        assertThat(versao).contains("PostgreSQL");
    }

    @Test
    void deveAplicarAsMigrationsDoFlywayNaSubida() {
        MigrationInfo[] aplicadas = flyway.info().applied();

        assertThat(aplicadas).isNotEmpty();
        assertThat(aplicadas[0].getDescription()).isEqualTo("baseline");
        assertThat(aplicadas[0].getState().isApplied()).isTrue();
    }
}