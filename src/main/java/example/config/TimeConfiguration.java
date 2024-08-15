package example.config;

import java.time.Clock;
import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Time.
 */
@Configuration
public class TimeConfiguration {

    @Bean
    TimeZone timeZone() {
        return TimeZone.getDefault();
    }

    @Bean
    Clock clock(TimeZone timeZone) {
        return Clock.system(timeZone.toZoneId());
    }
}
