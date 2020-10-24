import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "get movie info by movie's id - not found"

    request {
        url "/movie/Movie-Id-NotFound"
        method GET()
        headers {
            contentType applicationJson()
        }
    }

    response {
        status NOT_FOUND()
        headers {
            contentType applicationJson()
        }
        body (
                url: value(producer(regex(url())), consumer("http://localhost:8010/movie/Movie-Id-NotFound")),
                type: "NOT_FOUND",
                details: ["Movie with id Movie-Id-NotFound not found!"]
        )
    }
}