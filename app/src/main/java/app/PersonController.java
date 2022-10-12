package app;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.server.types.files.StreamedFile;
import io.micronaut.objectstorage.ObjectStorageEntry;
import io.micronaut.objectstorage.ObjectStorageOperations;
import io.micronaut.views.ModelAndView;

import java.util.Map;
import java.util.Optional;

@Controller("/people")
public record PersonController(
        MessageService messageService,
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
    ModelAndView<?> show(Long id) {

        Person person = repository.findById(id)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Not Found"));

        String message = messageService.getMessage(person);
        return new ModelAndView<>(
                "show",
                Map.of("person", person,
                        "message", message)
        );
    }

    @Get("/images/{name}")
    Optional<StreamedFile> getImage(String name) {
        return repository.findByName(name)
                .flatMap(person -> objectStorageOperations.retrieve(person.imageId()))
                .map(e -> ((ObjectStorageEntry<?>) e).toStreamedFile());
    }
}
