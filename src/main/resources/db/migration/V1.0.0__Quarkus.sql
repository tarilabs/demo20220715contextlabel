create extension ltree;
create table CECase (id int8 not null, ceuuid varchar(255), context jsonb, mytag ltree[], primary key (id));
create sequence hibernate_sequence start 1 increment 1;
-- will need to add indexes, maybe ~like:
CREATE INDEX mytag_gist_idx ON CECase USING GIST (mytag);
CREATE INDEX mytag_idx ON CECase USING BTREE (mytag);

INSERT INTO CECase VALUES (nextval('hibernate_sequence'), 'test1', '{"host":"milan.archivio"}', ARRAY [text2ltree('location.eu.italy.milan'), text2ltree('type.db')]);
