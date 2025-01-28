insert into USUARIOS (id, username, password, role)values (100, 'ana@gmail.com', '$2a$12$YWGaqAam3xYYecutO98SFu9dQ7aEAQBVSKbnYKadrK4MVBIVA8hkq', 'ROLE_ADMIN');
insert into USUARIOS (id, username, password, role)values (101, 'joao@gmail.com', '$2a$12$YWGaqAam3xYYecutO98SFu9dQ7aEAQBVSKbnYKadrK4MVBIVA8hkq', 'ROLE_CLIENTE');
insert into USUARIOS (id, username, password, role)values (102, 'bob@gmail.com', '$2a$12$YWGaqAam3xYYecutO98SFu9dQ7aEAQBVSKbnYKadrK4MVBIVA8hkq', 'ROLE_CLIENTE');
insert into USUARIOS (id, username, password, role)values (103, 'toby@gmail.com', '$2a$12$YWGaqAam3xYYecutO98SFu9dQ7aEAQBVSKbnYKadrK4MVBIVA8hkq', 'ROLE_CLIENTE');


insert into CLIENTES(id, nome, cpf, id_usuario) values (10, 'João Silva', '94234490008', 101);
insert into CLIENTES(id, nome, cpf, id_usuario) values (20, 'Roberto Gomes', '65456623099', 102);
