package oci.db;

import app.PersonRepository;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;

@JdbcRepository(dialect = Dialect.ORACLE)
@Replaces(PersonRepository.class)
@Requires(env = Environment.ORACLE_CLOUD)
public interface OraclePersonRepository extends PersonRepository {
}
