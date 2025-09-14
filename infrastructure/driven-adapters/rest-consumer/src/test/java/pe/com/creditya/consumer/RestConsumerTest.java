package pe.com.creditya.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.creditya.consumer.config.VariableClient;
import pe.com.creditya.consumer.dto.UserResponse;
import pe.com.creditya.consumer.exception.AuthenticationException;
import pe.com.creditya.consumer.exception.AuthorizationException;
import pe.com.creditya.consumer.mapper.UserMapperImpl;
import pe.com.creditya.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;


class RestConsumerTest {

    private static RestConsumer restConsumer;

    private static MockWebServer mockBackEnd;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        UserMapperImpl userMapper = new UserMapperImpl();
        VariableClient variableClient = new VariableClient();
        var webClient = WebClient.builder().baseUrl(mockBackEnd.url("/").toString()).build();
        restConsumer = new RestConsumer(webClient, userMapper, variableClient);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void validateTestGetUserByDocumentNumber() throws Exception {
        UserResponse userResponse = UserResponse.builder()
                .name("demo")
                .baseSalary(new BigDecimal("213.5"))
                .email("demo@gmail.com")
                .build();

        String userResponseJson = objectMapper.writeValueAsString(userResponse);

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody(userResponseJson));

        Mono<User> mono = restConsumer.getUserByDocumentNumber("12345678","bfksdjbfkdjsbfgkjsdbgkjsdbg");

        StepVerifier.create(mono)
                .expectNextMatches(user -> user.getEmail().equals("demo@gmail.com"))
                .verifyComplete();

        RecordedRequest request = mockBackEnd.takeRequest(1, TimeUnit.SECONDS);
        Assertions.assertThat(request).isNotNull();
    }

    @Test
    void validateTestHandleServerError() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setBody("{\"error\":\"Server error\"}"));

        Mono<User> mono = restConsumer.getUserByDocumentNumber("12345678","bfksdjbfkdjsbfgkjsdbgkjsdbg");

        StepVerifier.create(mono)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().contains("Server error"))
                .verify();
    }

    @Test
    void validateTestHandleClientError() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.BAD_REQUEST.value())
                .setBody("{\"error\":\"Client error\"}"));

        Mono<User> mono = restConsumer.getUserByDocumentNumber("12345678","bfksdjbfkdjsbfgkjsdbgkjsdbg");

        StepVerifier.create(mono)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().contains("Client error"))
                .verify();
    }

    @Test
    void validateTestGetUsersByEmails() throws Exception {
        UserResponse userResponse = UserResponse.builder()
                .name("demo")
                .baseSalary(new BigDecimal("213.5"))
                .email("demo@gmail.com")
                .build();
        String userResponseJson = objectMapper.writeValueAsString(List.of(userResponse));

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody(userResponseJson));

        Flux<User> responseUsers = restConsumer.getUsersByEmails(List.of("demo@gmail.com"),"bfksdjbfkdjsbfgkjsdbgkjsdbg");

        StepVerifier.create(responseUsers)
                .expectNextMatches(user -> user.getEmail().equals("demo@gmail.com"))
                .verifyComplete();

        RecordedRequest request = mockBackEnd.takeRequest(1, TimeUnit.SECONDS);
        Assertions.assertThat(request).isNotNull();
    }

    @Test
    void validateTestGetUsersByEmails_AuthenticationError() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.UNAUTHORIZED.value())
                .setBody("{\"error\":\"Unauthorized\"}"));

        Flux<User> responseUsers = restConsumer.getUsersByEmails(List.of("demo@gmail.com"),"bfksdjbfkdjsbfgkjsdbgkjsdbg");

        StepVerifier.create(responseUsers)
                .expectErrorMatches(throwable ->
                        throwable instanceof AuthenticationException ||
                                (throwable.getCause() != null && throwable.getCause() instanceof AuthenticationException))
                .verify();

        RecordedRequest request = mockBackEnd.takeRequest(1, TimeUnit.SECONDS);
        Assertions.assertThat(request).isNotNull();
    }

    @Test
    void validateTestGetUsersByEmails_ForbiddenWithFallback() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.FORBIDDEN.value())
                .setBody("{\"error\":\"Forbidden\"}"));

        Flux<User> responseUsers = restConsumer.getUsersByEmails(List.of("demo@gmail.com"),"bfksdjbfkdjsbfgkjsdbgkjsdbg");

        StepVerifier.create(responseUsers)
                .expectErrorMatches(throwable ->
                        throwable instanceof AuthorizationException ||
                                (throwable.getCause() != null && throwable.getCause() instanceof AuthorizationException))
                .verify();

        RecordedRequest request = mockBackEnd.takeRequest(1, TimeUnit.SECONDS);
        Assertions.assertThat(request).isNotNull();
    }

}
