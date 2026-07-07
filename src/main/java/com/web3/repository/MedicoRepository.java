package com.web3.repository;

import com.web3.model.Medico;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MedicoRepository {

    private final JdbcTemplate jdbcTemplate;

    public MedicoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Medico> rowMapper = (rs, rowNum) -> {
        Medico m = new Medico();
        m.setCrm(rs.getString("crm"));
        m.setNome(rs.getString("nome"));
        m.setEspecialidade(rs.getString("especialidade"));
        m.setContato(rs.getString("contato"));
        m.setSenha(rs.getString("senha"));
        return m;
    };

    public List<Medico> findAll() {
        return jdbcTemplate.query("SELECT * FROM medico ORDER BY nome", rowMapper);
    }

    public Optional<Medico> findById(String crm) {
        List<Medico> result = jdbcTemplate.query(
            "SELECT * FROM medico WHERE crm = ?", rowMapper, crm);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public Optional<Medico> findByCrmAndSenha(String crm, String senha) {
        List<Medico> result = jdbcTemplate.query(
            "SELECT * FROM medico WHERE crm = ? AND senha = ?", rowMapper, crm, senha);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public void save(Medico medico) {
        jdbcTemplate.update(
            "INSERT INTO medico (crm, nome, especialidade, contato, senha) VALUES (?, ?, ?, ?, ?)",
            medico.getCrm(), medico.getNome(), medico.getEspecialidade(),
            medico.getContato(), medico.getSenha());
    }

    public void update(Medico medico) {
        jdbcTemplate.update(
            "UPDATE medico SET nome = ?, especialidade = ?, contato = ?, senha = ? WHERE crm = ?",
            medico.getNome(), medico.getEspecialidade(), medico.getContato(),
            medico.getSenha(), medico.getCrm());
    }

    public void deleteById(String crm) {
        jdbcTemplate.update("DELETE FROM medico WHERE crm = ?", crm);
    }
}
