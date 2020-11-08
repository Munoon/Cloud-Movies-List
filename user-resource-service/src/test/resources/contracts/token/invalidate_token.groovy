import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "invalidating token"

    request {
        url "/microservices/token/logout"
        method POST()
        headers {
            contentType applicationJson()
        }
        body $(producer('"test_token"'), consumer("test_token"))
    }

    response {
        status NO_CONTENT()
    }
}