INSERT INTO categoria (nome) VALUES ('Trabalho');
INSERT INTO categoria (nome) VALUES ('Estudos');
INSERT INTO categoria (nome) VALUES ('Pessoal');
INSERT INTO categoria (nome) VALUES ('Saúde');
INSERT INTO categoria (nome) VALUES ('Urgente');

INSERT INTO tarefa (titulo, descricao, responsavel, prioridade, status, data_criacao, data_limite)
VALUES 
('Estudar para a prova', 'Estudar para a prova de redes do professor Rafael', 'Arthur', 'ALTA', 'PENDENTE', CURRENT_DATE, '2026-10-05'),
('Lavar a louça', 'Lavar a louça da comida que fiz para o almoço', 'Arthur', 'MEDIA', 'PENDENTE', CURRENT_DATE, '2026-10-05');

INSERT into users(username, password, role) VALUES ('admin', 'admin', 'ADMIN');