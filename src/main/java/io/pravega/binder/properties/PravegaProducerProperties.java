package io.pravega.binder.properties;

public class PravegaProducerProperties extends PravegaCommonProperties {

    private String routingKey;

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
}
