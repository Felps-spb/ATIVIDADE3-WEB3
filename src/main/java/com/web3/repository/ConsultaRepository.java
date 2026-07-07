package com.web3.repository;

import com.web3.model.Consulta;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ConsultaRepository {

    private final JdbcTemplate jdbcTemplate;

    public ConsultaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Consulta> rowMapper = (rs, rowNum) -> {
        Consulta c = new Consulta();
        c.setCodigo(rs.getInt("codigo"));
        c.setDataHora(rs.getString("data_hora"));
        c.setDataHoraVolta(rs.getString("data_hora_volta"));
        c.setObservacao(rs.getString("observacao"));
        c.setPacienteCpf(rs.getString("paciente_cpf"));
        c.setMedicoCrm(rs.getString("medico_crm"));
        return c;
    };

    private final RowMapper<Consulta> rowMapperComNomes = (rs, rowNum) -> {
        Consulta c = new Consulta();
        c.setCodigo(rs.getInt("codigo"));
        c.setDataHora(rs.getString("data_hora"));
        c.setDataHoraVolta(rs.getString("data_hora_volta"));
        c.setObservacao(rs.getString("observacao"));
        c.setPacienteCpf(rs.getString("paciente_cpf"));
        c.setMedicoCrm(rs.getString("medico_crm"));
        c.setPacienteNome(rs.getString("paciente_nome"));
        c.setMedicoNome(rs.getString("medico_nome"));
        return c;
    };

    public List<Consulta> findAll() {
        return jdbcTemplate.query(
            "SELECT c.*, p.nome as paciente_nome, m.nome as medico_nome " +
            "FROM consulta c " +
            "JOIN paciente p ON c.paciente_cpf = p.cpf " +
            "JOIN medico m ON c.medico_crm = m.crm " +
            "ORDER BY c.data_hora DESC",
            rowMapperComNomes);
    }

    public Optional<Consulta> findById(int codigo) {
        List<Consulta> result = jdbcTemplate.query(
            "SELECT c.*, p.nome as paciente_nome, m.nome as medico_nome " +
            "FROM consulta c " +
            "JOIN paciente p ON c.paciente_cpf = p.cpf " +
            "JOIN medico m ON c.medico_crm = m.crm " +
            "WHERE c.codigo = ?",
            rowMapperComNomes, codigo);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public List<Consulta> findPendentesByMedicoCrm(String crm) {
        return jdbcTemplate.query(
            "SELECT c.*, p.nome as paciente_nome, m.nome as medico_nome " +
            "FROM consulta c " +
            "JOIN paciente p ON c.paciente_cpf = p.cpf " +
            "JOIN medico m ON c.medico_crm = m.crm " +
            "LEFT JOIN prontuario pr ON c.codigo = pr.consulta_codigo " +
            "WHERE c.medico_crm = ? AND pr.codigo IS NULL " +
            "ORDER BY c.data_hora",
            rowMapperComNomes, crm);
    }

    public List<Consulta> findRealizadasByMedicoCrm(String crm) {
        return jdbcTemplate.query(
            "SELECT c.*, p.nome as paciente_nome, m.nome as medico_nome " +
            "FROM consulta c " +
            "JOIN paciente p ON c.paciente_cpf = p.cpf " +
            "JOIN medico m ON c.medico_crm = m.crm " +
            "JOIN prontuario pr ON c.codigo = pr.consulta_codigo " +
            "WHERE c.medico_crm = ? " +
            "ORDER BY c.data_hora DESC",
            rowMapperComNomes, crm);
    }

    public Consulta save(Consulta consulta) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO consulta (data_hora, data_hora_volta, observacao, paciente_cpf, medico_crm) VALUES (?, ?, ?, ?, ?)",
                new String[]{"codigo"});
            ps.setString(1, consulta.getDataHora());
            ps.setString(2, consulta.getDataHoraVolta());
            ps.setString(3, consulta.getObservacao());
            ps.setString(4, consulta.getPacienteCpf());
            ps.setString(5, consulta.getMedicoCrm());
            return ps;
        }, keyHolder);
        consulta.setCodigo(keyHolder.getKey().intValue());
        return consulta;
    }

    public void update(Consulta consulta) {
        jdbcTemplate.update(
            "UPDATE consulta SET data_hora = ?, data_hora_volta = ?, observacao = ?, paciente_cpf = ?, medico_crm = ? WHERE codigo = ?",
            consulta.getDataHora(), consulta.getDataHoraVolta(), consulta.getObservacao(),
            consulta.getPacienteCpf(), consulta.getMedicoCrm(), consulta.getCodigo());
    }

    public void deleteById(int codigo) {
        jdbcTemplate.update("DELETE FROM consulta WHERE codigo = ?", codigo);
    }
}
