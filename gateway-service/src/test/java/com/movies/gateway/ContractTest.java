package com.movies.gateway;

import com.movies.common.user.UserRoles;
import com.movies.common.user.UserTo;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ContractTest extends AbstractTest {
    @Test
    void updateProfileContractTest() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "bearer DEFAULT_USER");

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