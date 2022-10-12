package aws;

import app.MessageService;
import app.Person;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import jakarta.inject.Singleton;

@Singleton
@Requires(env = Environment.AMAZON_EC2)
public class AwsMessageService implements MessageService {
    @Override
    public String getMessage(Person person) {
        return "Hi " + person.name() + "! Welcome to AWS!";
    }
}
