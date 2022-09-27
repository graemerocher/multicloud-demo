package aws.db;

import app.PersonRepository;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;

@JdbcRepository(dialect = Dialect.MYSQL)
@Replaces(PersonRepository.class)
@Requires(env = Environment.AMAZON_EC2)
public interface MySqlPersonRepository extends PersonRepository {
}
