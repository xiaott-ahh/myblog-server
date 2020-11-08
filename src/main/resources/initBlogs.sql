create database IF NOT EXISTS myblog;

use myblog;

create table if not exists blogs (
    id int(11) not null primary key AUTO_INCREMENT,
    title varchar (255) not null DEFAULT '',
    abs varchar (255) not null DEFAULT '',
    cover varchar (255) not null DEFAULT '',
    blog_html longtext not null,
    blog_md longtext not null,
    created_at date not null,
    like_num int(11) not null DEFAULT 0,
    visited_num BIGINT not null DEFAULT 0,
    tags varchar (255) not null DEFAULT ''
)ENGINE=InnoDB DEFAULT CHARSET=utf-8;
