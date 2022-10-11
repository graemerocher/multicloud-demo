package app;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

@MappedEntity
public record Person(@Id
                     @GeneratedValue 
                     Long id, 
                     String name, 
                     String imageId) {
    public Person(String name, String imageId) {
        this(null, name, imageId);
    }
}
