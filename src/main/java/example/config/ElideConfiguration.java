/*
 * Copyright 2020, Yahoo Inc.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */

package example.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;

import com.yahoo.elide.core.utils.obfuscation.IdObfuscator;

import example.services.BytesEncryptorIdObfuscator;

/**
 * Configuration for Elide.
 */
@Configuration
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class ElideConfiguration {

    /**
     * Configures a id obfuscator.
     *
     * For demonstration purposes.
     * 
     * @param securityConfigProperties the configuration
     * @return the id obfuscator
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.security.id-obfuscation", name = "enabled", havingValue = "true")
    IdObfuscator idObfuscator(SecurityConfigProperties securityConfigProperties) {
        String password = securityConfigProperties.getIdObfuscation().getPassword();
        String salt = securityConfigProperties.getIdObfuscation().getSalt();
        AesBytesEncryptor bytesEncryptor = new AesBytesEncryptor(password, salt);
        return new BytesEncryptorIdObfuscator(bytesEncryptor);
    }
}
