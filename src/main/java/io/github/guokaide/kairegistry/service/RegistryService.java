package io.github.guokaide.kairegistry.service;

import io.github.guokaide.kairegistry.model.InstanceMeta;

import java.util.List;
import java.util.Map;

public interface RegistryService {

    InstanceMeta register(String service, InstanceMeta instance);

    InstanceMeta unregister(String service, InstanceMeta instance);

    List<InstanceMeta> getAllInstances(String service);

    long renew(InstanceMeta instance, String... services);

    Long version(String service);

    Map<String, Long> versions(String... services);
}
