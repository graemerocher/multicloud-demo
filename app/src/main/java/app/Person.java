package app;


import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

@MappedEntity
public record Person(@Id
                     @GeneratedValue @Nullable Long id, String name, String imageId) {
    public Person(String name, String imageId) {
        this(null, name, imageId);
    }
}
