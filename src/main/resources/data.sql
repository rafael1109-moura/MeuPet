INSERT INTO tb_doenca (nome, descricao, tratamento) VALUES
('Raiva', 'Zoonose viral fatal', 'Prevencao vacinal'),
('Cinomose', 'Virus altamente contagioso', 'Suporte intensivo'),
('Parvovirose', 'Infeccao gastrointestinal', 'Internacao e hidratacao'),
('Rinotraqueite', 'Doenca respiratoria felina', 'Antibioticos e suporte');

INSERT INTO tb_vacina (nome, descricao, doenca_id, periodicidade_meses) VALUES
('Antirrabica', 'Dose anual', 1, 12),
('V8', 'Protecao contra Cinomose e Parvo', 2, 12),
('Antirrabica Felina', 'Dose anual', 1, 12),
('V3', 'Protecao respiratoria', 4, 6);

INSERT INTO tb_animal (nome, idade, sexo, peso, sujo, castrado) VALUES
('Thor', 3, 'MACHO', 12.5, false, true),
('Mel', 1, 'FEMEA', 4.2, true, false);

INSERT INTO tb_cachorro (id, data_last_banho, data_last_tosa, data_ultimo_passeio, raca) VALUES
(1, '2026-05-10', '2026-04-15', '2026-05-16', 'Golden_Retriever'),
(2, '2026-05-01', NULL, '2026-05-15', 'Pinscher');

INSERT INTO tb_animal (nome, idade, sexo, peso, sujo, castrado) VALUES
('Mingau', 2, 'MACHO', 5.0, false, true),
('Luna', 4, 'FEMEA', 6.1, false, true);

INSERT INTO tb_gato (id, areia_suja, raca) VALUES
(3, false, 'Siames'),
(4, true, 'Persa');

INSERT INTO tb_tarefa (titulo, descricao, categoria, prioridade, data_prevista, concluida, animal_id, vacina_id) VALUES
('Passear com o Thor', 'Passeio no parque', 'PESSOAL', 'MEDIA', '2026-09-16', false, 1, NULL),
('Comprar racao do Mingau', NULL, 'PESSOAL', 'ALTA', '2026-09-10', false, 3, NULL),
('Proxima dose V8 do Thor', 'Dose anual', 'VACINACAO', 'ALTA', '2026-05-10', false, 1, 2),
('Consulta de rotina da Luna', 'Check-up anual', 'CONSULTA', 'MEDIA', '2026-10-01', false, 4, NULL);