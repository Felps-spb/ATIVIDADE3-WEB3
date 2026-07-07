package com.web3.repository;

import com.web3.model.Medicamento;
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
public class MedicamentoRepository {

    private final JdbcTemplate jdbcTemplate;

    public MedicamentoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Medicamento> rowMapper = (rs, rowNum) -> {
        Medicamento m = new Medicamento();
        m.setCodigo(rs.getInt("codigo"));
        m.setNome(rs.getString("nome"));
        m.setDosagem(rs.getInt("dosagem"));
        m.setTipoDosagem(rs.getString("tipo_dosagem"));
        m.setDescricao(rs.getString("descricao"));
        m.setObservacao(rs.getString("observacao"));
        return m;
    };

    public List<Medicamento> findAll() {
        return jdbcTemplate.query("SELECT * FROM medicamento ORDER BY nome", rowMapper);
    }

    public Optional<Medicamento> findById(int codigo) {
        List<Medicamento> result = jdbcTemplate.query(
            "SELECT * FROM medicamento WHERE codigo = ?", rowMapper, codigo);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public Medicamento save(Medicamento medicamento) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO medicamento (nome, dosagem, tipo_dosagem, descricao, observacao) VALUES (?, ?, ?, ?, ?)",
                new String[]{"codigo"});
            ps.setString(1, medicamento.getNome());
            ps.setInt(2, medicamento.getDosagem());
            ps.setString(3, medicamento.getTipoDosagem());
            ps.setString(4, medicamento.getDescricao());
            ps.setString(5, medicamento.getObservacao());
            return ps;
        }, keyHolder);
        medicamento.setCodigo(keyHolder.getKey().intValue());
        return medicamento;
    }

    public void update(Medicamento medicamento) {
        jdbcTemplate.update(
            "UPDATE medicamento SET nome = ?, dosagem = ?, tipo_dosagem = ?, descricao = ?, observacao = ? WHERE codigo = ?",
            medicamento.getNome(), medicamento.getDosagem(), medicamento.getTipoDosagem(),
            medicamento.getDescricao(), medicamento.getObservacao(), medicamento.getCodigo());
    }

    public void deleteById(int codigo) {
        jdbcTemplate.update("DELETE FROM medicamento WHERE codigo = ?", codigo);
    }
}
