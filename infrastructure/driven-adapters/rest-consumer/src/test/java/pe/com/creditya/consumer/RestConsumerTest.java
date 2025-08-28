package pe.com.creditya.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.creditya.consumer.mapper.UserMapper;
import pe.com.creditya.model.common.exception.CustomClientException;
import pe.com.creditya.model.user.User;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class RestConsumerTest {
    private static MockWebServer mockBackEnd;
    private static RestConsumer restConsumer;
    private static ObjectMapper objectMapper;

    private static UserMapper userMapper;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockBackEnd.url("/").toString())
                .build();

        objectMapper = new ObjectMapper();

        userMapper = Mockito.mock(UserMapper.class);
        when(userMapper.toUser(any(UserResponse.class)))
                .thenAnswer(invocation -> {
                    UserResponse ur = invocation.getArgument(0);
                    return new User(ur.getDocumentNumber(), ur.getEmail());
                });

        restConsumer = new RestConsumer(webClient, userMapper);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void testGetUserByDocumentNumberSuccess() throws Exception {
        UserResponse userResponse = new UserResponse("12345678", "Jaime@gmail.com");
        String body = objectMapper.writeValueAsString(userResponse);

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody(body));
        var response = restConsumer.getUserByDocumentNumber("12345678");

        StepVerifier.create(response)
                .expectNextMatches(user ->
                        user.getDocumentNumber().equals("12345678"))
                .verifyComplete();
    }
}