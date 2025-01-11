package com.raidiam.consents.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfiguration {

    /**
     * Configure Clock to UTC
     *
     * @return  Clock
     */
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
