package io.github.guokaide.kairegistry.health;

import io.github.guokaide.kairegistry.model.InstanceMeta;
import io.github.guokaide.kairegistry.service.DefaultRegistryService;
import io.github.guokaide.kairegistry.service.RegistryService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DefaultHealthChecker implements HealthChecker {

    private static final int DEFAULT_TIMEOUT = 20_000;

    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);

    private final RegistryService registryService;

    public DefaultHealthChecker(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Override
    public void start() {
        executor.scheduleAtFixedRate(() -> {
            try {
                log.info(" ===> Health checker started ...");
                long now = System.currentTimeMillis();
                DefaultRegistryService.INSTANCE_TIMESTAMPS.forEach((serviceAndInstance, timestamp) -> {
                    String[] serviceAndInstanceArray = serviceAndInstance.split("@");
                    String service = serviceAndInstanceArray[0];
                    String instanceUrl = serviceAndInstanceArray[1];
                    InstanceMeta instance = InstanceMeta.from(instanceUrl);
                    if (now - timestamp > DEFAULT_TIMEOUT) {
                        registryService.unregister(service, instance);
                        DefaultRegistryService.INSTANCE_TIMESTAMPS.remove(serviceAndInstance);
                    }
                });
            } catch (Exception e) {
                log.error(" ===> Health checker failed", e);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        executor.shutdown();
    }
}
