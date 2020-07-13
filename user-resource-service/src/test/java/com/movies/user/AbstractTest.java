package com.movies.user;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(scripts = { "classpath:db/data.sql" })
public abstract class AbstractTest {

}
