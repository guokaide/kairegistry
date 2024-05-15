package io.github.guokaide.kairegistry.service;

import io.github.guokaide.kairegistry.model.InstanceMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
public class DefaultRegistryService implements RegistryService {

    // service 和 instance 是多对多的关系
    private final MultiValueMap<String, InstanceMeta> REGISTRY = new LinkedMultiValueMap<>();

    private final Map<String, Long> SERVICE_VERSIONS = new ConcurrentHashMap<>();

    public static final Map<String, Long> INSTANCE_TIMESTAMPS = new ConcurrentHashMap<>();

    private final AtomicLong atomicLong = new AtomicLong(0);

    @Override
    public InstanceMeta register(String service, InstanceMeta instance) {
        List<InstanceMeta> instances = REGISTRY.get(service);
        if (instances != null && instances.contains(instance)) {
            log.info(" ===> instance {} already registered", instance);
            instance.setStatus(true);
            return instance;
        }
        REGISTRY.add(service, instance);
        instance.setStatus(true);

        renew(instance, service);
        SERVICE_VERSIONS.put(service, atomicLong.incrementAndGet());
        log.info(" ===> register instance {}", instance);
        return instance;
    }

    @Override
    public InstanceMeta unregister(String service, InstanceMeta instance) {
        List<InstanceMeta> instances = REGISTRY.get(service);
        if (instances == null || !instances.contains(instance)) {
            return null;
        }
        instances.removeIf(x -> x.equals(instance));
        instance.setStatus(false);

        SERVICE_VERSIONS.put(service, atomicLong.incrementAndGet());
        log.info(" ===> unregister instance {}", instance);
        return instance;
    }

    @Override
    public List<InstanceMeta> getAllInstances(String service) {
        return REGISTRY.get(service);
    }

    @Override
    public long renew(InstanceMeta instance, String... services) {
        long now = System.currentTimeMillis();
        for (String service : services) {
            INSTANCE_TIMESTAMPS.put(service + "@" + instance.toUrl(), now);
        }
        return now;
    }

    @Override
    public Long version(String service) {
        return SERVICE_VERSIONS.get(service);
    }

    @Override
    public Map<String, Long> versions(String... services) {
        return Arrays.stream(services)
                .filter(SERVICE_VERSIONS::containsKey)
                .collect(Collectors.toMap(k -> k, SERVICE_VERSIONS::get, (k, v) -> v));
    }
}
