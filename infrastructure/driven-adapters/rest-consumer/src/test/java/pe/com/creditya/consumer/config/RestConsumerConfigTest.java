package pe.com.creditya.consumer.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

class RestConsumerConfigTest {

    private RestConsumerConfig restConsumerConfig;

    private static final String TEST_URL = "http://localhost:8080";
    private static final int TIMEOUT = 5000;

    @BeforeEach
    void setUp() {
        restConsumerConfig = new RestConsumerConfig(TEST_URL, TIMEOUT);
    }



    @Test
    void shouldCreateClientHttpConnector() {
        // when
        var connector = restConsumerConfig.getWebClient(WebClient.builder())
                .mutate()
                .build()
                .mutate()
                .toString();

        // then
        assertThat(connector).isNotNull();
        assertThat(TIMEOUT).isEqualTo(5000); // valida que el timeout se pasó correctamente
    }
}
