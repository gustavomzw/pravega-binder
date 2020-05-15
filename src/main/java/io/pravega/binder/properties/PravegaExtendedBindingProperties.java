package io.pravega.binder.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.binder.AbstractExtendedBindingProperties;
import org.springframework.cloud.stream.binder.BinderSpecificPropertiesProvider;

@ConfigurationProperties("spring.cloud.stream.pravega")
public class PravegaExtendedBindingProperties extends
        AbstractExtendedBindingProperties<PravegaConsumerProperties, PravegaProducerProperties, PravegaBindingProperties> {

    private static final String DEFAULTS_PREFIX = "spring.cloud.stream.pravega.default";

    @Override
    public String getDefaultsPrefix() {
        return DEFAULTS_PREFIX;
    }

    @Override
    public Class<? extends BinderSpecificPropertiesProvider> getExtendedPropertiesEntryClass() {
        return PravegaBindingProperties.class;
    }
}
