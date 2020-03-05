 create table TAG(
    id long auto_increment,
    tag_name varchar,
    gmt_create bigint,
    parent bigint,
    constraint TAG_pk
    primary key (id)
);