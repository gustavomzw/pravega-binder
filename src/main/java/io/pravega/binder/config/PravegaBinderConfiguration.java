package io.pravega.binder.config;

import io.pravega.binder.PravegaMessageChannelBinder;
import io.pravega.binder.properties.PravegaBinderConfigurationProperties;
import io.pravega.binder.properties.PravegaExtendedBindingProperties;
import io.pravega.binder.provisioning.PravegaChannelProvisioner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.cloud.stream.binder.Binder;
import org.springframework.cloud.stream.config.BindingHandlerAdvise.MappingsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@ConditionalOnMissingBean(Binder.class)
@EnableConfigurationProperties({ PravegaBinderConfigurationProperties.class, PravegaExtendedBindingProperties.class })
public class PravegaBinderConfiguration {

    @Bean
    public PravegaChannelProvisioner pravegaChannelProvisioner(
            PravegaBinderConfigurationProperties pravegaBinderConfigurationProperties) {
        return new PravegaChannelProvisioner(pravegaBinderConfigurationProperties);
    }

    @Bean
    public PravegaMessageChannelBinder pravegaMessageChannelBinder(
            PravegaChannelProvisioner pravegaBinderProvisioner,
            PravegaBinderConfigurationProperties pravegaBinderConfigurationProperties,
            PravegaExtendedBindingProperties pravegaExtendedBindingProperties) {
        return new PravegaMessageChannelBinder(pravegaBinderProvisioner, pravegaBinderConfigurationProperties,
                pravegaExtendedBindingProperties);
    }

    @Bean
    public MappingsProvider pravegaExtendedPropertiesDefaultMappingsProvider() {
        return () -> Collections.singletonMap(
                ConfigurationPropertyName.of("spring.cloud.stream.pravega.bindings"),
                ConfigurationPropertyName.of("spring.cloud.stream.pravega.default")
        );
    }

}
