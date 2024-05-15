package io.github.guokaide.kairegistry.service;

import io.github.guokaide.kairegistry.model.InstanceMeta;

import java.util.List;

public interface RegistryService {

    InstanceMeta register(String service, InstanceMeta instance);

    InstanceMeta unregister(String service, InstanceMeta instance);

    List<InstanceMeta> getAllInstances(String service);

}
