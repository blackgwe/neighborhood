insert into organisation(title,latitude,longitude,zipCode,createdDate) values ('Bibli Wila',47.420925093115116, 8.843167941378159,'8492','1993-02-26');
insert into organisation(title,latitude,longitude,zipCode,createdDate) values ('Bibli Turbenthal',47.43847965594044, 8.845340125596715,'03677','1994-11-25');
insert into organisation(title,latitude,longitude,zipCode,createdDate) values ('Uster','8610',47.35170092923746, 8.716647102671649,'1994-12-25');

insert into product(name, ean) values ('test1', 123123);
insert into product(name) values ('test2');
insert into product(name) values ('test3');
insert into product(name) values ('test4');

insert into productPatron(firstName, lastName, contactData) values ('firstName', 'lastName','address/tel1');
insert into productPatron(firstName, lastName, contactData) values ('firstName', 'lastName','address/tel2');
insert into productPatron(firstName, lastName, contactData) values ('firstName', 'lastName','address/tel3');
insert into productPatron(firstName, lastName, contactData) values ('firstName', 'lastName','address/tel4');
insert into productPatron(firstName, lastName, contactData) values ('firstName', 'lastName','address/tel5');

insert into catalogue(organisation_id,product_id, patron_id) values (1,1,1);
insert into catalogue(organisation_id,product_id, patron_id) values (2,2,1);
insert into catalogue(organisation_id,product_id, patron_id) values (2,3,1);
insert into catalogue(organisation_id,product_id, patron_id) values (1,2,2);
insert into catalogue(organisation_id,product_id, patron_id) values (3,4,2);