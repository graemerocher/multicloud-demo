package app;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

@Singleton
public class Application {
    private final PersonRepository repository;

    public Application(PersonRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        Micronaut.build(args)
                .mainClass(Application.class)
                .defaultEnvironments("dev")
                .start();
    }

    @EventListener
    void onStart(StartupEvent event) {
        Person person = new Person();
        person.setName("Graeme");
        person.setImageId("graeme@grails.org_0239.jpg");
        repository.save(person);
    }
}
