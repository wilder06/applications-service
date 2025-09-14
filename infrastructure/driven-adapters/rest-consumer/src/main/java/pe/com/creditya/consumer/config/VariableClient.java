package pe.com.creditya.consumer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "adapter.restconsumer")
public class VariableClient {
    private String pathFindUserByDocumentNumber;
    private String pathFindUsersByEmails;
    private String url;
    private String timeout;
}
