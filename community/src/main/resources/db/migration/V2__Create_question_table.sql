
create table question
(
	id int auto_increment,
	title varchar(50),
	description TEXT,
	gmt_create bigint,
	gmt_modified bigint,
	creator int default 0,
	comment_cont int default 0,
	view_count int default 0,
	like_count int default 0,
	tag varchar default 256,
	constraint question_pk
		primary key (id)
);

