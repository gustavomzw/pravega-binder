package io.pravega.binder;

import io.pravega.binder.properties.PravegaBinderConfigurationProperties;
import io.pravega.binder.properties.PravegaConsumerProperties;
import io.pravega.binder.properties.PravegaExtendedBindingProperties;
import io.pravega.binder.properties.PravegaProducerProperties;
import io.pravega.binder.provisioning.PravegaChannelProvisioner;
import org.springframework.cloud.stream.binder.*;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.integration.core.MessageProducer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

public class PravegaMessageChannelBinder
        extends AbstractMessageChannelBinder<ExtendedConsumerProperties<PravegaConsumerProperties>, ExtendedProducerProperties<PravegaProducerProperties>, PravegaChannelProvisioner>
        implements ExtendedPropertiesBinder<MessageChannel, PravegaConsumerProperties, PravegaProducerProperties> {

    private final PravegaExtendedBindingProperties extendedBindingProperties;

    private final PravegaBinderConfigurationProperties configurationProperties;

    public PravegaMessageChannelBinder(PravegaChannelProvisioner provisioner,
                                       PravegaBinderConfigurationProperties configurationProperties,
                                       PravegaExtendedBindingProperties extendedBindingProperties) {
        super(new String[0], provisioner);
        this.configurationProperties = configurationProperties;
        this.extendedBindingProperties = extendedBindingProperties;
    }

    @Override
    protected MessageHandler createProducerMessageHandler(ProducerDestination destination, ExtendedProducerProperties<PravegaProducerProperties> producerProperties, MessageChannel errorChannel) throws Exception {
        return new PravegaMessageHandler(configurationProperties.getUri(), producerProperties.getExtension().getScope(), destination.getName(), producerProperties.getExtension().getRoutingKey());
    }

    @Override
    protected MessageProducer createConsumerEndpoint(ConsumerDestination destination, String group, ExtendedConsumerProperties<PravegaConsumerProperties> properties) throws Exception {
        return new PravegaMessageProducer(configurationProperties.getUri(), properties.getExtension().getScope(), destination.getName());
    }

    @Override
    public PravegaConsumerProperties getExtendedConsumerProperties(String channelName) {
        return extendedBindingProperties.getExtendedConsumerProperties(channelName);
    }

    @Override
    public PravegaProducerProperties getExtendedProducerProperties(String channelName) {
        return extendedBindingProperties.getExtendedProducerProperties(channelName);
    }

    @Override
    public String getDefaultsPrefix() {
        return extendedBindingProperties.getDefaultsPrefix();
    }

    @Override
    public Class<? extends BinderSpecificPropertiesProvider> getExtendedPropertiesEntryClass() {
        return extendedBindingProperties.getExtendedPropertiesEntryClass();
    }
}
