package uk.co.mulecode.fileservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "uk.co.mulecode.fileservice.repository")
public class JpaConfig {

}
