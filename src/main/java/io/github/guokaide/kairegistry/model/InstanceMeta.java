package io.github.guokaide.kairegistry.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"schema", "host", "port", "context"})
public class InstanceMeta {
    // 基本参数
    private String schema;
    private String host;
    private Integer port;
    private String context;

    // 扩展参数
    private boolean status;
    private Map<String, String> params = new HashMap<>();

    public InstanceMeta(String schema, String host, Integer port, String context) {
        this.schema = schema;
        this.host = host;
        this.port = port;
        this.context = context;
    }

    public static InstanceMeta http(String host, Integer port) {
        return new InstanceMeta("http", host, port, "kairpc");
    }

    public static InstanceMeta from(String url) {
        URI uri = URI.create(url);
        return new InstanceMeta(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath().substring(1));
    }

    public String toPath() {
        return String.format("%s:%d", host, port);
    }

    public String toUrl() {
        return String.format("%s://%s:%d/%s", schema, host, port, context);
    }

    public String toMetas() {
        return JSON.toJSONString(this.params);
    }

    public InstanceMeta addParams(Map<String, String> params) {
        this.params.putAll(params);
        return this;
    }

    public static void main(String[] args) {
        InstanceMeta instanceMeta = InstanceMeta.http("localhost", 8080)
                .addParams(Map.of("tag", "green"));
        System.out.println(JSON.toJSON(instanceMeta));
    }

}
