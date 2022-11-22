package oci;

import app.MessageService;
import app.Person;
import com.oracle.bmc.http.client.HttpProvider;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import jakarta.inject.Singleton;

@Singleton
@Requires(env = Environment.ORACLE_CLOUD)
public class OciMessageService implements MessageService {
    @Override
    public String getMessage(Person person) {
        return "Hi " + person.name() + "! Welcome to OCI! The http provider is " + HttpProvider.getDefault().toString();
    }
}
