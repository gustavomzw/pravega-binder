package io.pravega.binder.provisioning;

import org.springframework.cloud.stream.provisioning.ConsumerDestination;

public class PravegaConsumerDestination implements ConsumerDestination {

    private String name;

    public PravegaConsumerDestination(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}
