package com.web3.repository;

import com.web3.model.Paciente;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PacienteRepository {

    private final JdbcTemplate jdbcTemplate;

    public PacienteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Paciente> rowMapper = (rs, rowNum) -> {
        Paciente p = new Paciente();
        p.setCpf(rs.getString("cpf"));
        p.setNome(rs.getString("nome"));
        p.setEndereco(rs.getString("endereco"));
        p.setContato(rs.getString("contato"));
        p.setPlanoSaude(rs.getString("plano_saude"));
        return p;
    };

    public List<Paciente> findAll() {
        return jdbcTemplate.query("SELECT * FROM paciente ORDER BY nome", rowMapper);
    }

    public Optional<Paciente> findById(String cpf) {
        List<Paciente> result = jdbcTemplate.query(
            "SELECT * FROM paciente WHERE cpf = ?", rowMapper, cpf);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public void save(Paciente paciente) {
        jdbcTemplate.update(
            "INSERT INTO paciente (cpf, nome, endereco, contato, plano_saude) VALUES (?, ?, ?, ?, ?)",
            paciente.getCpf(), paciente.getNome(), paciente.getEndereco(),
            paciente.getContato(), paciente.getPlanoSaude());
    }

    public void update(Paciente paciente) {
        jdbcTemplate.update(
            "UPDATE paciente SET nome = ?, endereco = ?, contato = ?, plano_saude = ? WHERE cpf = ?",
            paciente.getNome(), paciente.getEndereco(), paciente.getContato(),
            paciente.getPlanoSaude(), paciente.getCpf());
    }

    public void deleteById(String cpf) {
        jdbcTemplate.update("DELETE FROM paciente WHERE cpf = ?", cpf);
    }
}
