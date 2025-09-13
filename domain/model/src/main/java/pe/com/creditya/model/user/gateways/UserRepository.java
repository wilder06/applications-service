package pe.com.creditya.model.user.gateways;

import pe.com.creditya.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserRepository {
    Mono<User> getUserByDocumentNumber(String documentNumber);
    Flux<User> getUsersByEmails(List<String> emails);

}
