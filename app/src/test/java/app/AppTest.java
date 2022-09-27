package app;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class AppTest {

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testListPeople(PeopleClient client) {
        assertTrue(application.isRunning());

        String content = client.list();
        assertTrue(content.contains("List of People:"));
    }


    @Test
    void testLoadImage(PeopleClient client, PersonRepository repository) {
        Person first = repository.findAll().iterator().next();

        byte[] image = client.getImage(first.getId());
        assertNotNull(image);
    }

    @Client("/people")
    interface PeopleClient {
        @Get
        String list();

        @Get("/images/{id}")
        byte[] getImage(Long id);
    }
}
