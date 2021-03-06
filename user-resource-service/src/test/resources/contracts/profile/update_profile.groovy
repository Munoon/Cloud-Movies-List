import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "update user's name and surname"

    request {
        url "/profile/update"
        method POST()
        headers {
            contentType applicationJson()
            header("Authorization", "bearer DEFAULT_USER")
        }
        body (
                name: "newName",
                surname: "newSurname"
        )
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body (
                id: 100,
                name: "newName",
                surname: "newSurname",
                email: "munoongg@gmail.com",
                roles: ["ROLE_ADMIN", "ROLE_USER"]
        )
    }
}