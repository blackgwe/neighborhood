insert into organisation(title,zipCode,releaseDate) values ('Bibli Wila','8492','1993-02-26');
insert into organisation(title,zipCode,releaseDate) values ('Bibli Turbenthal','03677','1994-11-25');
insert into organisation(title,zipCode,releaseDate) values ('Uster','8610','1994-12-25');

insert into product(name) values ('test1');
insert into product(name) values ('test2');
insert into product(name) values ('test3');
insert into product(name) values ('test4');

insert into productPatron(firstName, lastName, contactData) values ('firstName', 'lastName','address/tel1');
insert into productPatron(firstName, lastName, contactData) values ('firstName', 'lastName','address/tel2');
insert into productPatron(firstName, lastName, contactData) values ('firstName', 'lastName','address/tel3');
insert into productPatron(firstName, lastName, contactData) values ('firstName', 'lastName','address/tel4');
insert into productPatron(firstName, lastName, contactData) values ('firstName', 'lastName','address/tel5');

insert into catalog(organisation_id,product_id, patron_id) values (1,1,1);
insert into catalog(organisation_id,product_id, patron_id) values (2,2,1);
insert into catalog(organisation_id,product_id, patron_id) values (2,3,1);
insert into catalog(organisation_id,product_id, patron_id) values (1,2,2);
insert into catalog(organisation_id,product_id, patron_id) values (3,4,2);