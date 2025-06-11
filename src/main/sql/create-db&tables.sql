create database milou_db;
use milou_db;

create table users (
    id int primary key auto_increment,
    name nvarchar(100) not null,
    email nvarchar(100) unique not null,
    password nvarchar(255) not null,
    signUp_time timestamp not null default current_timestamp
);

create table emails (
    id int primary key auto_increment,
    code varchar(10) unique not null,
    sender_id int not null,
    subject nvarchar(255) not null,
    body nvarchar(10000) not null,
    send_time timestamp default current_timestamp,

    foreign key (sender_id) references users(id)
);

create table recipients (
    id int primary key auto_increment,
    email_id int not null,
    recipient_id int not null,
    is_read boolean not null default false,
    read_time timestamp not null default current_timestamp,

    foreign key (email_id) references emails(id),
    foreign key (recipient_id) references users(id)
);

drop table recipients;
drop table emails;
drop table users;