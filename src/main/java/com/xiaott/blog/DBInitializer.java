package com.xiaott.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DBInitializer {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate.update("create table if not exists blogs (" +
                "id int(11) not null primary key AUTO_INCREMENT," +
                "title varchar (255) DEFAULT null," +
                "abs varchar (255) DEFAULT null," +
                "cover varchar (255) DEFAULT null," +
                "blog_html longtext CHARACTER SET utf8 COLLATE utf8_general_ci," +
                "blog_md longtext CHARACTER SET utf8 COLLATE utf8_general_ci," +
                "created_at datetime DEFAULT null," +
                "like_num int(11) DEFAULT 0," +
                "visited_num BIGINT DEFAULT 0," +
                "tags varchar (255) DEFAULT null)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;");
    }
}
