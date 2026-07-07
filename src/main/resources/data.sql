INSERT INTO medico (crm, nome, especialidade, contato, senha) VALUES
('CRM001', 'Dr. Carlos Silva', 'Cardiologia', '11999990001', '123'),
('CRM002', 'Dra. Ana Oliveira', 'Dermatologia', '11999990002', '123'),
('CRM003', 'Dr. Roberto Lima', 'Ortopedia', '11999990003', '123')
ON CONFLICT (crm) DO NOTHING;

INSERT INTO medicamento (nome, dosagem, tipo_dosagem, descricao, observacao) VALUES
('Paracetamol', 500, 'mg', 'Analgesico e antitérmico', 'Tomar apos as refeicoes'),
('Ibuprofeno', 600, 'mg', 'Anti-inflamatório nao esteroidal', 'Evitar uso prolongado'),
('Amoxicilina', 875, 'mg', 'Antibiotico de amplo espectro', 'Uso sob prescricao medica'),
('Omeprazol', 20, 'mg', 'Inibidor da bomba de protons', 'Tomar em jejum'),
('Dipirona', 500, 'mg', 'Analgesico e antitérmico', 'Evitar em caso de alergia')
ON CONFLICT DO NOTHING;
