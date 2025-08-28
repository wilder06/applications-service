package pe.com.creditya.model.user.gateways;

import pe.com.creditya.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> getUserByDocumentNumber(String documentNumber);

}
