create table users (
    id int primary key auto_increment,
    name nvarchar(100) not null,
    email nvarchar(100) unique not null,
    password varchar(255) not null,
    signUp_time timestamp not null default current_timestamp
);

create table emails (
    id int primary key auto_increment,
    sender_id int not null,
    subject nvarchar(255) not null,
    body nvarchar(10000) not null,
    send_time timestamp default current_timestamp,

    foreign key (sender_id) references users(id)
);

create table email_recipients (
    id int primary key auto_increment,
    email_id int not null,
    recipient_id int not null,
    read_time timestamp default null,

    foreign key (email_id) references emails(id),
    foreign key (recipient_id) references users(id)
);