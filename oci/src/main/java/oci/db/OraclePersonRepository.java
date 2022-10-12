package oci.db;

import app.PersonRepository;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;

@JdbcRepository(dialect = Dialect.ORACLE)
@Replaces(PersonRepository.class)
public interface OraclePersonRepository extends PersonRepository {
}
