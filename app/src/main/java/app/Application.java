package app;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;

import app.Person;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.event.annotation.EventListener;

@Controller("/")
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
    @Transactional
    void onStart(StartupEvent event) {
        repository.deleteAll();
        var graeme = new Person("Graeme", "graeme@grails.org_0239.jpg");
        var alvaro = new Person("Alvaro", "alvaro.jpg");
        repository.saveAll(List.of(graeme, alvaro));
    }

    @Get
    HttpResponse<?> redirect() {
        return HttpResponse.redirect(URI.create("/people"));
    }
}
