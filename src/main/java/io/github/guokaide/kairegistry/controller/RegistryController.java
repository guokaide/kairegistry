package io.github.guokaide.kairegistry.controller;

import io.github.guokaide.kairegistry.model.InstanceMeta;
import io.github.guokaide.kairegistry.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RegistryController {


    @Autowired
    private RegistryService registryService;

    @RequestMapping("/register")
    public InstanceMeta register(@RequestParam String service, @RequestBody InstanceMeta instance) {
        return registryService.register(service, instance);
    }

    @RequestMapping("/unregister")
    public InstanceMeta unregister(@RequestParam String service, @RequestBody InstanceMeta instance) {
        return registryService.unregister(service, instance);
    }

    @RequestMapping("/findAll")
    public List<InstanceMeta> getAllInstances(@RequestParam String service) {
        return registryService.getAllInstances(service);
    }

    @RequestMapping("/renew")
    public long renew(@RequestParam String service, @RequestBody InstanceMeta instance) {
        return registryService.renew(instance, service);
    }

    @RequestMapping("/version")
    public Long version(@RequestParam String service) {
        return registryService.version(service);
    }

    @RequestMapping("/versions")
    public Map<String, Long> versions(@RequestParam String services) {
        return registryService.versions(services.split(","));
    }
}
