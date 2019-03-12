package micronaut.server.filter.demo.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import micronaut.server.filter.demo.client.LibraryClient;
import reactor.core.publisher.Mono;

@Controller("/books")
public class BookController {

    private LibraryClient client;

    public BookController(LibraryClient client) {
        this.client = client;
    }

    @Get("/{id}")
    public Mono<String> getBookName(String id) {
        return client.getBookName(id);
    }
}
