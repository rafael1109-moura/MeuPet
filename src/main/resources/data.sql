/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  edvaldinhs
 * Created: May 17, 2026
 */

CREATE TABLE IF NOT EXISTS tb_usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_doenca (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    tratamento VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS tb_vacina (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    doenca_id INT,
    FOREIGN KEY (doenca_id) REFERENCES tb_doenca(id)
);

CREATE TABLE IF NOT EXISTS tb_animal (
    id SERIAL PRIMARY KEY,
    tipo_animal VARCHAR(20) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    idade INT NOT NULL,
    sexo VARCHAR(10) NOT NULL,
    peso FLOAT NOT NULL,
    sujo BOOLEAN NOT NULL,
    castrado BOOLEAN NOT NULL,
    
    data_last_banho VARCHAR(20),
    data_last_tosa VARCHAR(20),
    data_ultimo_passeio VARCHAR(20),
    raca_cachorro VARCHAR(50),
    
    areia_suja BOOLEAN,
    raca_gato VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS tb_animal_doenca (
    animal_id INT NOT NULL,
    doenca_id INT NOT NULL,
    PRIMARY KEY (animal_id, doenca_id),
    FOREIGN KEY (animal_id) REFERENCES tb_animal(id) ON DELETE CASCADE,
    FOREIGN KEY (doenca_id) REFERENCES tb_doenca(id) ON DELETE CASCADE
);

INSERT INTO tb_usuario (nome, email, senha) VALUES 
('teste', 'teste@meupet.com', 'senha123'),
('alou', 'alou@meupet.com', 'admin321');

INSERT INTO tb_doenca (nome, descricao, tratamento) VALUES 
('Raiva', 'Zoonose viral fatal', 'Prevenção vacinal'),
('Cinomose', 'Vírus altamente contagioso', 'Suporte intensivo'),
('Parvovirose', 'Infecção gastrointestinal', 'Internação e hidratação'),
('Rinotraqueíte', 'Doença respiratória felina', 'Antibióticos e suporte');

INSERT INTO tb_vacina (nome, descricao, doenca_id) VALUES 
('Antirrábica', 'Dose anual', 1),
('V8', 'Proteção contra Cinomose e Parvo', 2),
('Antirrábica Felina', 'Dose anual', 1),
('V3', 'Proteção respiratória', 4);

INSERT INTO tb_animal (tipo_animal, nome, idade, sexo, peso, sujo, castrado, data_last_banho, data_last_tosa, data_ultimo_passeio, raca_cachorro, areia_suja, raca_gato) VALUES 
('CACHORRO', 'Thor', 3, 'MACHO', 12.5, false, true, '2026-05-10', '2026-04-15', '2026-05-16', 'Golden_Retriever', NULL, NULL),
('CACHORRO', 'Mel', 1, 'FEMEA', 4.2, true, false, '2026-05-01', 'Não possui', '2026-05-15', 'Pinscher', NULL, NULL),
('GATO', 'Mingau', 2, 'MACHO', 5.0, false, true, NULL, NULL, NULL, NULL, false, 'Siames'),
('GATO', 'Luna', 4, 'FEMEA', 6.1, false, true, NULL, NULL, NULL, NULL, true, 'Persa');

INSERT INTO tb_animal_doenca (animal_id, doenca_id) VALUES (2, 3), (4, 4);