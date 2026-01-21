insert into role(id, created_at, last_modified_at,name, created_by_id,
                 modified_by_id) values (1,'2024-04-04 12:52:44','2024-04-04 12:52:44','ADMIN',NULL,NULL);

insert into user_configuration(id, created_at, last_modified_at, created_by_id,
                               modified_by_id,image,pdf,movie) values
                                                                   (1,'2024-04-04 12:52:44','2024-04-04 12:52:44',NULL,NULL,1,1,1);

insert into users(id, created_at, last_modified_at, email, first_name, last_name, password, created_by_id,
                  modified_by_id,server_compressor,user_configuration_id,status,has_changed_password) values(1,'2024-04-04 12:52:44','2024-04-04 12:52:44','admin@admin.com','SB','Solutions','$2a$10$cSqKGvZvEGEzQhRFRyDVyuCR3Lf0e7FcpIfxd/0t5IOG9U.3flG8m',
                                         NULL,NULL,1,1,'ACTIVE',1);

insert into users_roles(user_id,roles_id)values (1,1);


insert into role(id, created_at, last_modified_at,name, created_by_id,
                 modified_by_id) values (2,'2024-04-04 12:52:44','2024-04-04 12:52:44','USER',NULL,NULL);

insert into role(id, created_at, last_modified_at,name, created_by_id,
                 modified_by_id) values (3,'2024-04-04 12:52:44','2024-04-04 12:52:44','EDITOR',NULL,NULL);

insert into role(id, created_at, last_modified_at,name, created_by_id,
                 modified_by_id) values (4,'2024-04-04 12:52:44','2024-04-04 12:52:44','API_USER',NULL,NULL);


