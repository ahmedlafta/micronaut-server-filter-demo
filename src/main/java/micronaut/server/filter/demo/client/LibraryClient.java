package micronaut.server.filter.demo.client;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;

@Client("library")
public interface LibraryClient {

    @Get("/library/books/{id}")
    Mono<String> getBookName(String id);
}
