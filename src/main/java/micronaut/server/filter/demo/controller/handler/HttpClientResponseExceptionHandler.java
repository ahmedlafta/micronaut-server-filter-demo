package micronaut.server.filter.demo.controller.handler;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Requires(classes = {HttpClientResponseException.class, ExceptionHandler.class})
public class HttpClientResponseExceptionHandler implements ExceptionHandler<HttpClientResponseException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, HttpClientResponseException exception) {
        return HttpResponse.status(exception.getStatus()).body(exception.getMessage());
    }
}
