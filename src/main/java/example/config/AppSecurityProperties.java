/*    
 * Copyright 2020, Yahoo Inc.    
 * Licensed under the Apache License, Version 2.0    
 * See LICENSE file in project root for terms.    
 */    

package example.config;    

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;    

/**
 * Configuration properties for application security.
 */
@Data    
@ConfigurationProperties(prefix = "app.security")
public class AppSecurityProperties {
    /**
     * Whether to enable security.
     */
    private boolean enabled = false;

    /**
     * A list of origins for which cross-origin requests are allowed.
     */
    private String origin = "*";
}
