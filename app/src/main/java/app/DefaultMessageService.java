package app;

import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;

@Singleton
@Requires(missingBeans = MessageService.class)
public class DefaultMessageService implements MessageService {
    @Override
    public String getMessage(Person person) {
        return "Hi " + person.name() + "!";
    }
}
