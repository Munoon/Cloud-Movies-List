import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "get movie info by movie's id"

    request {
        url "/movie/Movie-Id-1"
        method GET()
        headers {
            contentType applicationJson()
        }
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body (
                id: "Movie-Id-1",
                name: "Example Movie",
                hasAvatar: false,
                registered: "2019-12-22T12:00:00"
        )
    }
}