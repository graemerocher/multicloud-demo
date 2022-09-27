package app.os;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.function.Consumer;

import io.micronaut.context.annotation.Requires;
import io.micronaut.objectstorage.ObjectStorageEntry;
import io.micronaut.objectstorage.ObjectStorageOperations;
import io.micronaut.objectstorage.request.UploadRequest;
import io.micronaut.objectstorage.response.UploadResponse;
import jakarta.inject.Singleton;

@Singleton
@Requires(missingBeans = ObjectStorageOperations.class)
public class ClasspathObjectStorage implements ObjectStorageOperations<Object, Object, URL> {
    @Override
    public UploadResponse<Object> upload(UploadRequest request) {
        return UploadResponse.of(
                request.getKey(),
                null,
                null
        );
    }

    @Override
    public UploadResponse<Object> upload(UploadRequest request, Consumer<Object> requestConsumer) {
        return UploadResponse.of(
                request.getKey(),
                null,
                null
        );
    }

    @Override
    public <E extends ObjectStorageEntry<?>> Optional<E> retrieve(String key) {
        String path = "images/" + key;
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
    public URL delete(String key) {
        String path = "images/" + key;
        return getClass().getClassLoader().getResource(path);
    }
}
