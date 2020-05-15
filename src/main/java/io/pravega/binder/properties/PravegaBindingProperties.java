package io.pravega.binder.properties;

import org.springframework.cloud.stream.binder.BinderSpecificPropertiesProvider;

public class PravegaBindingProperties implements BinderSpecificPropertiesProvider {

    private PravegaConsumerProperties consumer = new PravegaConsumerProperties();

    private PravegaProducerProperties producer = new PravegaProducerProperties();

    @Override
    public PravegaConsumerProperties getConsumer() {
        return this.consumer;
    }

    public void setConsumer(PravegaConsumerProperties consumer) {
        this.consumer = consumer;
    }

    @Override
    public PravegaProducerProperties getProducer() {
        return this.producer;
    }

    public void setProducer(PravegaProducerProperties producer) {
        this.producer = producer;
    }
}
