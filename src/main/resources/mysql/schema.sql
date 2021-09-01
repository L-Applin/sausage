create schema if not exists sausage;
use sausage;

create table if not exists app_user (
    user_id     varchar(255) not null unique default (uuid()),
    username    varchar(255) not null unique,
    icon        varchar(255) not null default 'cat'

) engine=InnoDB;

alter table app_user add primary key app_user(user_id);
alter table app_user add foreign key app_user_users_fk(username) references users(username) on delete cascade ;

create unique index person_username_idx using hash on app_user(username);


create table if not exists review (
    review_id       varchar(255) not null unique default (uuid()),
    author_id       varchar(255) not null ,
    date_created    timestamp not null default now(),
    date_review     timestamp,
    stars           smallint not null default 0,
    text            varchar(512) not null default '',

    constraint foreign key review_author_id_fk(author_id) references app_user(user_id) on delete cascade
) engine=InnoDB;

alter table review add primary key review(review_id);

create index review_date_idx using hash on review(date_created);
create index review_auther_idx using hash on review(author_id);


create table if not exists crims_involved (
    review_id   varchar(255) not null ,
    user_id     varchar(255) not null ,

    constraint primary key (review_id, user_id),
    constraint foreign key crims_review_fk(review_id) references review(review_id),
    constraint foreign key crims_user_fk(user_id) references app_user(user_id)
) engine=InnoDB;


create table if not exists unknown_crims_involved (
    review_id   varchar(255) not null ,
    crim_name   varchar(255) not null ,

    constraint primary key (review_id, crim_name),
    constraint foreign key crims_review_fk(review_id) references review(review_id)
) engine=InnoDB;
