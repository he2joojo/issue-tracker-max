package codesquad.kr.gyeonggidoidle.issuetracker.config;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.entity.OauthAdapter;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.entity.OauthProperties;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.entity.OauthProvider;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.repository.InMemoryProviderRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(OauthProperties.class)
public class OauthConfig {

    private final OauthProperties properties;

    @Bean
    public InMemoryProviderRepository inMemoryProviderRepository() {
        Map<String, OauthProvider> providers = OauthAdapter.getOauthProviders(properties);
        return new InMemoryProviderRepository(providers);
    }
}
