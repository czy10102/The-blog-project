create table user
(
	id int auto_increment,
	account_id varchar(50),
	token varchar(100),
	gmt_modified bigint,
	gmt_create bigint,
	name varchar(50),
	constraint user_pk
		primary key (id)
);
