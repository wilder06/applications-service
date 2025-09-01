package pe.com.creditya.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.com.creditya.consumer.mapper.UserMapperImpl;
import pe.com.creditya.model.user.User;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


class RestConsumerTest {

    private static RestConsumer restConsumer;

    private static MockWebServer mockBackEnd;
    private ObjectMapper objectMapper = new ObjectMapper();

    private static UserMapperImpl userMapper;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        userMapper = new UserMapperImpl();
        var webClient = WebClient.builder().baseUrl(mockBackEnd.url("/").toString()).build();
        restConsumer = new RestConsumer(webClient, userMapper);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void validateTestGet() throws Exception {
        UserResponse userResponse = UserResponse.builder()
                .documentNumber("12345678")
                .email("demo@gmail.com")
                .build();

        String userResponseJson = objectMapper.writeValueAsString(userResponse);

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody(userResponseJson));

        Mono<User> mono = restConsumer.getUserByDocumentNumber("12345678");

        StepVerifier.create(mono)
                .expectNextMatches(user -> user.getDocumentNumber().equals("12345678"))
                .verifyComplete();

        RecordedRequest request = mockBackEnd.takeRequest(1, TimeUnit.SECONDS);
        Assertions.assertThat(request).isNotNull();
        Assertions.assertThat(request.getPath()).isEqualTo("/api/v1/usuarios/12345678"); // <- validar URL
    }


}
