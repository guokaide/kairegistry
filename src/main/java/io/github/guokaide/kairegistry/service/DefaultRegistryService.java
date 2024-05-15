package io.github.guokaide.kairegistry.service;

import io.github.guokaide.kairegistry.model.InstanceMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Slf4j
public class DefaultRegistryService implements RegistryService {

    // service 和 instance 是多对多的关系
    private final MultiValueMap<String, InstanceMeta> REGISTRY = new LinkedMultiValueMap<>();

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
        log.info(" ===> unregister instance {}", instance);
        return instance;
    }

    @Override
    public List<InstanceMeta> getAllInstances(String service) {
        return REGISTRY.get(service);
    }
}
