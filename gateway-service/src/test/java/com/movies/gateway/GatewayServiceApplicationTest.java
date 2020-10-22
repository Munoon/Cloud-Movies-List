package com.movies.gateway;

import com.movies.common.user.UserRoles;
import com.movies.common.user.UserTo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "security.oauth2.client.accessTokenUri=http://localhost:8030/uaa/oauth/token",
                "security.oauth2.client.userAuthorizationUri=http://localhost:8030/uaa/oauth/authorize",
                "security.oauth2.client.clientId=testClient",
                "security.oauth2.client.clientSecret=password"
        }
)
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "com.movies:user-resource-service:+:stubs:8090"
)
class GatewayServiceApplicationTest {
    @Test
    void updateProfileContractTest() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        headers.set("Authorization", "DEFAULT_USER");

        Map<String, String> body = new HashMap<>();
        body.put("name", "newName");
        body.put("surname", "newSurname");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<UserTo> responseEntity = restTemplate.postForEntity("http://localhost:8090/profile/update", entity, UserTo.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserTo actual = responseEntity.getBody();
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(100);
        assertThat(actual.getName()).isEqualTo("newName");
        assertThat(actual.getSurname()).isEqualTo("newSurname");
        assertThat(actual.getEmail()).isEqualTo("munoongg@gmail.com");
        assertThat(actual.getRoles()).usingDefaultComparator().isEqualTo(Set.of(UserRoles.ROLE_ADMIN, UserRoles.ROLE_USER));
    }
}