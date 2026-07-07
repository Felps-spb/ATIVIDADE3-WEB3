DROP TABLE IF EXISTS item_receituario CASCADE;
DROP TABLE IF EXISTS receituario CASCADE;
DROP TABLE IF EXISTS prontuario CASCADE;
DROP TABLE IF EXISTS consulta CASCADE;
DROP TABLE IF EXISTS medicamento CASCADE;
DROP TABLE IF EXISTS medico CASCADE;
DROP TABLE IF EXISTS paciente CASCADE;

CREATE TABLE paciente (
    cpf VARCHAR(14) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(255),
    contato VARCHAR(50),
    plano_saude VARCHAR(100)
);

CREATE TABLE medico (
    crm VARCHAR(20) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    especialidade VARCHAR(100),
    contato VARCHAR(50),
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE medicamento (
    codigo SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    dosagem INTEGER,
    tipo_dosagem VARCHAR(50),
    descricao VARCHAR(500),
    observacao VARCHAR(500)
);

CREATE TABLE consulta (
    codigo SERIAL PRIMARY KEY,
    data_hora VARCHAR(50) NOT NULL,
    data_hora_volta VARCHAR(50),
    observacao VARCHAR(500),
    paciente_cpf VARCHAR(14) NOT NULL REFERENCES paciente(cpf),
    medico_crm VARCHAR(20) NOT NULL REFERENCES medico(crm)
);

CREATE TABLE prontuario (
    codigo SERIAL PRIMARY KEY,
    descricao TEXT NOT NULL,
    observacao VARCHAR(500),
    consulta_codigo INTEGER UNIQUE NOT NULL REFERENCES consulta(codigo)
);

CREATE TABLE receituario (
    codigo SERIAL PRIMARY KEY,
    observacao VARCHAR(500),
    prontuario_codigo INTEGER NOT NULL REFERENCES prontuario(codigo)
);

CREATE TABLE item_receituario (
    codigo SERIAL PRIMARY KEY,
    dosagem INTEGER,
    intervalo_entre_doses INTEGER,
    observacao VARCHAR(500),
    receituario_codigo INTEGER NOT NULL REFERENCES receituario(codigo),
    medicamento_codigo INTEGER NOT NULL REFERENCES medicamento(codigo)
);
