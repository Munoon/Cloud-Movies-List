import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "delete user's profile"

    request {
        url "/profile"
        method DELETE()
        headers {
            contentType applicationJson()
            header("Authorization", "bearer DEFAULT_USER")
        }
        body (password: "password")
    }

    response {
        status OK()
    }
}