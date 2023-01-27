package schoolapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import schoolapi.dto.AlunosDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SchoolConfiguration {

    @Bean
    public Map<Integer, AlunosDto> alunosRepository() {
        return new HashMap<>();
    }
}
