package io.pravega.binder.provisioning;

import org.springframework.cloud.stream.provisioning.ProducerDestination;

public class PravegaProducerDestination implements ProducerDestination {

    private String name;

    public PravegaProducerDestination(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getNameForPartition(int partition) {
        return this.name + "-" + partition;
    }
}
