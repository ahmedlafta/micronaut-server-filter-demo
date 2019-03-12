package micronaut.server.filter.demo

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.Options
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching

class BookServerFilterTest extends Specification {

  @Shared
  @AutoCleanup("stop")
  WireMockServer wireMockServer = new WireMockServer(Options.DYNAMIC_PORT)

  @Shared
  @AutoCleanup
  HttpClient client

  @Shared
  @AutoCleanup
  EmbeddedServer server

  void setup() {
    wireMockServer.start()
    configureFor(wireMockServer.port())
    def props = ['micronaut.http.services.library.url': "http://localhost:${wireMockServer.port()}"]
    server = ApplicationContext.run(EmbeddedServer.class, props) as EmbeddedServer

    client = HttpClient.create(server.URL)
  }

  def 'server filter is invoked once'() {
    when:
    stubFor(get(urlPathMatching("/library/books/123"))
        .willReturn(aResponse()
        .withBody('To Kill a Mockingbird')
        .withHeader("Content-Type", MediaType.TEXT_PLAIN)))

    def result = client.toBlocking().exchange(HttpRequest.GET('/books/123'), String)

    then:
    noExceptionThrown()
    result.status().code == 200
    result.body() == 'To Kill a Mockingbird'
  }

  def 'server filter is invoked twice on http client exception'() {
    when:
    stubFor(get(urlPathMatching("/library/books/123"))
        .willReturn(aResponse()
        .withBody('An error occurred')
        .withStatus(500)
        .withHeader("Content-Type", MediaType.TEXT_PLAIN)))

    client.toBlocking().exchange(HttpRequest.GET('/books/123'), String)

    then:
    def e = thrown(HttpClientResponseException)
    assert e.status.code == 500
  }
}
