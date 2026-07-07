package com.web3.repository;

import com.web3.model.Receituario;
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
public class ReceituarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReceituarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Receituario> rowMapper = (rs, rowNum) -> {
        Receituario r = new Receituario();
        r.setCodigo(rs.getInt("codigo"));
        r.setObservacao(rs.getString("observacao"));
        r.setProntuarioCodigo(rs.getInt("prontuario_codigo"));
        return r;
    };

    public Optional<Receituario> findByProntuarioCodigo(int prontuarioCodigo) {
        List<Receituario> result = jdbcTemplate.query(
            "SELECT * FROM receituario WHERE prontuario_codigo = ?", rowMapper, prontuarioCodigo);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public Receituario save(Receituario receituario) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO receituario (observacao, prontuario_codigo) VALUES (?, ?)",
                new String[]{"codigo"});
            ps.setString(1, receituario.getObservacao());
            ps.setInt(2, receituario.getProntuarioCodigo());
            return ps;
        }, keyHolder);
        receituario.setCodigo(keyHolder.getKey().intValue());
        return receituario;
    }
}
