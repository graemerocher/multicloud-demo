package app;

import java.util.Map;
import java.util.Optional;

import app.Person;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.server.types.files.StreamedFile;
import io.micronaut.objectstorage.ObjectStorageEntry;
import io.micronaut.objectstorage.ObjectStorageOperations;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.View;

@Controller("/people")
public record PersonController(
    PersonRepository repository,
    ObjectStorageOperations<?, ?, ?> objectStorageOperations) {

    @Get
    ModelAndView<?> list() {
        return new ModelAndView<>(
            "list",
            Map.of("people", repository.findAll())
        );
    }

    @Get("/{id}")
    @View("show")
    Optional<Person> show(Long id) {
        return repository.findById(id);
    }

    @Get("/images/{id}")
    Optional<StreamedFile> getImage(Long id) {
        return repository.findById(id)
                .flatMap(person -> objectStorageOperations.retrieve(person.imageId()))
                .map(e -> ((ObjectStorageEntry<?>) e).toStreamedFile());
    }
}
