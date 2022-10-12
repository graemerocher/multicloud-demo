package oci;

import app.PersonRepository;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class OciTest {

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Test
    void testListPeople(PeopleClient client, PersonRepository repository) {
        assertTrue(application.isRunning());
        assertEquals(2, repository.count());
        String content = client.list();
        assertTrue(content.contains("List of People:"));
    }


    @Client("/people")
    interface PeopleClient {
        @Get
        String list();

        @Get("/images/{id}")
        byte[] getImage(Long id);
    }
}
