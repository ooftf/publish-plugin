package com.ooftf.publish.bintray;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.PublishArtifact;
import org.gradle.api.internal.attributes.ImmutableAttributes;
import org.gradle.api.plugins.internal.AbstractConfigurationUsageContext;

import java.util.Set;

/**
 * @author 99474
 */
public class LazyConfigurationUsageContext extends AbstractConfigurationUsageContext {
    private final String configurationName;
    private final ConfigurationContainer configurations;

    public LazyConfigurationUsageContext(String name, String configurationName, Set<PublishArtifact> artifacts, ConfigurationContainer configurations, ImmutableAttributes attributes) {
        super(name, attributes, artifacts);
        this.configurationName = configurationName;
        this.configurations = configurations;
    }

    @Override
    protected Configuration getConfiguration() {
        return this.configurations.getByName(this.configurationName);
    }
}