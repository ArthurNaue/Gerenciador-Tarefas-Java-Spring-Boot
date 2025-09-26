-- Para criar as categorias e Tarefas quando o nosso projeto iniciar

INSERT INTO categoria (nome) VALUES ('Trabalho');
INSERT INTO categoria (nome) VALUES ('Estudos');
INSERT INTO categoria (nome) VALUES ('Pessoal');
INSERT INTO categoria (nome) VALUES ('Saúde');
INSERT INTO categoria (nome) VALUES ('Urgente');


INSERT INTO tarefa (titulo, descricao, responsavel, prioridade, status, data_criacao, data_limite)
VALUES 
 ('Preparar slides', 'Aula sobre MVC e Thymeleaf', 'Ryan', 'ALTA', 'PENDENTE', CURRENT_DATE, '2026-08-20'),
 ('Estudar Security', 'Ler docs Spring Security', 'Ryan', 'MEDIA', 'PENDENTE', CURRENT_DATE, '2026-09-05'),
 ('Exercício JPA', 'CRUD com validação', 'Ana', 'BAIXA', 'PENDENTE', CURRENT_DATE, '2026-08-25'),
 ('Refatorar services', 'Separar responsabilidades', 'Igor', 'MEDIA', 'PENDENTE', CURRENT_DATE, '2026-08-28'),
 ('Revisar modelos', 'Ajustar entidade Categoria', 'Luiza', 'ALTA', 'PENDENTE', CURRENT_DATE, '2026-08-22'),
 ('Feedback cliente', 'Coletar feedback do protótipo', 'Time', 'MEDIA', 'PENDENTE', CURRENT_DATE, '2026-09-01');


INSERT into users(username, password, role) VALUES ('admin', 'admin', 'ADMIN');