create table address (id int8 not null, city varchar(255), street varchar(255), zip varchar(255), primary key (id));
alter table airport add column address_id int8;
alter table airport add constraint FK_airport_to_address foreign key (address_id) references address;
