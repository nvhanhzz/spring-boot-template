package sti.project.template.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sti.project.scada.base.enums.PersistableEnumModule;

@Configuration
public class JacksonConfig {

    @Bean
    public PersistableEnumModule persistableEnumModule() {
        return new PersistableEnumModule();
    }
}
