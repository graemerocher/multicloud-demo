package app;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class AppTest {

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testListPeople(PeopleClient client, PersonRepository repository) {
        assertTrue(application.isRunning());
        assertEquals(2, repository.count());
        String content = client.list();
        assertTrue(content.contains("List of People:"));
    }


    @Test
    void testLoadImage(PeopleClient client, PersonRepository repository) {
        Person first = repository.findAll().iterator().next();

        byte[] image = client.getImage(first.name());
        assertNotNull(image);
    }

    @Client("/people")
    interface PeopleClient {
        @Get
        String list();

        @Get("/images/{name}")
        byte[] getImage(String name);
    }
}
