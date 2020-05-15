package io.pravega.binder.provisioning;

import io.pravega.binder.properties.PravegaBinderConfigurationProperties;
import io.pravega.binder.properties.PravegaConsumerProperties;
import io.pravega.binder.properties.PravegaProducerProperties;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.cloud.stream.provisioning.ProvisioningException;
import org.springframework.cloud.stream.provisioning.ProvisioningProvider;

public class PravegaChannelProvisioner
        implements ProvisioningProvider<ExtendedConsumerProperties<PravegaConsumerProperties>,
        ExtendedProducerProperties<PravegaProducerProperties>> {

    private final PravegaBinderConfigurationProperties pravegaBinderConfigurationProperties;

    public PravegaChannelProvisioner(PravegaBinderConfigurationProperties pravegaBinderConfigurationProperties) {
        this.pravegaBinderConfigurationProperties = pravegaBinderConfigurationProperties;
    }

    @Override
    public ProducerDestination provisionProducerDestination(String name, ExtendedProducerProperties<PravegaProducerProperties> properties) throws ProvisioningException {
        return new PravegaProducerDestination(name);
    }

    @Override
    public ConsumerDestination provisionConsumerDestination(String name, String group, ExtendedConsumerProperties<PravegaConsumerProperties> properties) throws ProvisioningException {
        return new PravegaConsumerDestination(name);
    }
}
