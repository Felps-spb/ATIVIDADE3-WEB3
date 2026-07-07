package com.web3.repository;

import com.web3.model.Prontuario;
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
public class ProntuarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProntuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Prontuario> rowMapper = (rs, rowNum) -> {
        Prontuario p = new Prontuario();
        p.setCodigo(rs.getInt("codigo"));
        p.setDescricao(rs.getString("descricao"));
        p.setObservacao(rs.getString("observacao"));
        p.setConsultaCodigo(rs.getInt("consulta_codigo"));
        return p;
    };

    public Optional<Prontuario> findById(int codigo) {
        List<Prontuario> result = jdbcTemplate.query(
            "SELECT * FROM prontuario WHERE codigo = ?", rowMapper, codigo);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public Optional<Prontuario> findByConsultaCodigo(int consultaCodigo) {
        List<Prontuario> result = jdbcTemplate.query(
            "SELECT * FROM prontuario WHERE consulta_codigo = ?", rowMapper, consultaCodigo);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public Prontuario save(Prontuario prontuario) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO prontuario (descricao, observacao, consulta_codigo) VALUES (?, ?, ?)",
                new String[]{"codigo"});
            ps.setString(1, prontuario.getDescricao());
            ps.setString(2, prontuario.getObservacao());
            ps.setInt(3, prontuario.getConsultaCodigo());
            return ps;
        }, keyHolder);
        prontuario.setCodigo(keyHolder.getKey().intValue());
        return prontuario;
    }
}
