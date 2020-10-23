import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "update user's email"

    request {
        url "/profile/update/email"
        method POST()
        headers {
            contentType applicationJson()
            header("Authorization", "bearer DEFAULT_USER")
        }
        body (email: "newEmail@gmail.com")
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body (
                id: 100,
                name: "Nikita",
                surname: "Ivchenko",
                email: "newemail@gmail.com",
                roles: ["ROLE_ADMIN", "ROLE_USER"]
        )
    }
}