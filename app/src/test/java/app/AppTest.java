package app;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.function.Consumer;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.objectstorage.ObjectStorageEntry;
import io.micronaut.objectstorage.ObjectStorageOperations;
import io.micronaut.objectstorage.request.UploadRequest;
import io.micronaut.objectstorage.response.UploadResponse;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

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

    @MockBean
    ObjectStorageOperations<?, ?, ?> objectStorageOperations() {
        return new ObjectStorageOperations<>() {
            @Override
            public UploadResponse<Object> upload(UploadRequest request) {
                return null;
            }

            @Override
            public UploadResponse<Object> upload(UploadRequest request, Consumer<Object> requestConsumer) {
                return null;
            }

            @Override
            public <E extends ObjectStorageEntry<?>> Optional<E> retrieve(String key) {
                String path = "bucket/" + key;
                URL r = getClass().getClassLoader().getResource(path);
                if (r != null) {
                    @SuppressWarnings("unchecked") E entry = (E) new ObjectStorageEntry<>() {

                        @Override
                        public String getKey() {
                            return key;
                        }

                        @Override
                        public InputStream getInputStream() {
                            return getClass().getClassLoader().getResourceAsStream(path);
                        }

                        @Override
                        public Object getNativeEntry() {
                            return r;
                        }
                    };
                    return Optional.of(entry);
                }

                return Optional.empty();
            }

            @Override
            public Object delete(String key) {
                return null;
            }
        };
    }
}
