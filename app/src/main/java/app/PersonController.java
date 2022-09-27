package app;

import java.util.Map;
import java.util.Optional;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.server.types.files.StreamedFile;
import io.micronaut.objectstorage.ObjectStorageEntry;
import io.micronaut.objectstorage.ObjectStorageOperations;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.View;

@Controller("/people")
@ExecuteOn(TaskExecutors.IO)
public class PersonController {
    private final PersonRepository repository;
    private final ObjectStorageOperations<?, ?, ?> objectStorageOperations;

    public PersonController(PersonRepository repository, ObjectStorageOperations<?, ?, ?> objectStorageOperations) {
        this.repository = repository;
        this.objectStorageOperations = objectStorageOperations;
    }

    @Get
    @View("list")
    ModelAndView<?> list() {
        return new ModelAndView<>(
            "list",
            Map.of("people", repository.findAll())
        );
    }


    @Get("/{id}")
    @View("show")
    Person show(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Get("/images/{id}")
    Optional<StreamedFile> getImage(Long id) {
        return repository.findById(id)
                .flatMap(person -> objectStorageOperations.retrieve(person.imageId()))
                .map(e -> ((ObjectStorageEntry<?>) e).toStreamedFile());
    }
}
