INSERT INTO tb_doenca (nome, descricao, tratamento) VALUES
('Raiva', 'Zoonose viral fatal', 'Prevencao vacinal'),
('Cinomose', 'Virus altamente contagioso', 'Suporte intensivo'),
('Parvovirose', 'Infeccao gastrointestinal', 'Internacao e hidratacao'),
('Rinotraqueite', 'Doenca respiratoria felina', 'Antibioticos e suporte');

INSERT INTO tb_vacina (nome, descricao, doenca_id) VALUES
('Antirrabica', 'Dose anual', 1),
('V8', 'Protecao contra Cinomose e Parvo', 2),
('Antirrabica Felina', 'Dose anual', 1),
('V3', 'Protecao respiratoria', 4);

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