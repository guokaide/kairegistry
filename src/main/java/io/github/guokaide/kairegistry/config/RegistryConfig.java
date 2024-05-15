package io.github.guokaide.kairegistry.config;

import io.github.guokaide.kairegistry.health.DefaultHealthChecker;
import io.github.guokaide.kairegistry.health.HealthChecker;
import io.github.guokaide.kairegistry.service.DefaultRegistryService;
import io.github.guokaide.kairegistry.service.RegistryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegistryConfig {

    @Bean
    public RegistryService registryService() {
        return new DefaultRegistryService();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public HealthChecker healthChecker(RegistryService registryService) {
        return new DefaultHealthChecker(registryService);
    }
}
