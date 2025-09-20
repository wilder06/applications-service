package pe.com.creditya.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.com.creditya.security.constants.Constants;
import pe.com.creditya.security.jwt.JwtProperties;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
@Configuration
@RequiredArgsConstructor
public class JwtKeyConfig {

    private final JwtProperties jwtProperties;

    @Bean
    public PublicKey jwtPublicKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance(Constants.JCEKS_PREFIX);

            keyStore.load( new FileInputStream(jwtProperties.getKeystoreLocation()),
                    jwtProperties.getKeystorePassword().toCharArray());

            Certificate cert = keyStore.getCertificate(jwtProperties.getKeyAlias());
            return cert.getPublicKey();
        } catch (Exception e) {
            throw new IllegalStateException(Constants.LOG_MISSING_FAILED_PUBLIC_KEY, e);
        }
    }
}

