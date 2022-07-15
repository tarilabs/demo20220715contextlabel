create extension ltree;
create table CECase (id int8 not null, ceuuid varchar(255), context jsonb, mytag ltree[], primary key (id));
create sequence hibernate_sequence start 1 increment 1;
INSERT INTO CECase VALUES (nextval('hibernate_sequence'), 'test1', '{"host":"milan.archivio"}', ARRAY [text2ltree('location.eu.italy.milan'), text2ltree('type.db')]);

-- will need to add indexes, maybe ~like:
-- CREATE INDEX path_gist_idx ON test USING GIST (path);
-- CREATE INDEX path_idx ON test USING BTREE (path);
