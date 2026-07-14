package jhony.ruiz.sigevi.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
@Configuration
public class MapperConfig {
    @Bean(name = "defaultMapper")
    @Primary
    public ModelMapper getDefaultMapper() {

      return new ModelMapper();
    }
}