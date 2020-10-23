import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "update user's password"

    request {
        url "/profile/update/password"
        method POST()
        headers {
            contentType applicationJson()
            header("Authorization", "bearer DEFAULT_USER")
        }
        body (
                oldPassword: "pass",
                newPassword: "newPassword"
        )
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
                email: "munoongg@gmail.com",
                roles: ["ROLE_ADMIN", "ROLE_USER"]
        )
    }
}