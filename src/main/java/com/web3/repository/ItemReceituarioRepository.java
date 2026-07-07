package com.web3.repository;

import com.web3.model.ItemReceituario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ItemReceituarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public ItemReceituarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ItemReceituario> rowMapper = (rs, rowNum) -> {
        ItemReceituario item = new ItemReceituario();
        item.setCodigo(rs.getInt("codigo"));
        item.setDosagem(rs.getInt("dosagem"));
        item.setIntervaloEntreDoses(rs.getInt("intervalo_entre_doses"));
        item.setObservacao(rs.getString("observacao"));
        item.setReceituarioCodigo(rs.getInt("receituario_codigo"));
        item.setMedicamentoCodigo(rs.getInt("medicamento_codigo"));
        item.setMedicamentoNome(rs.getString("medicamento_nome"));
        return item;
    };

    public List<ItemReceituario> findByReceituarioCodigo(int receituarioCodigo) {
        return jdbcTemplate.query(
            "SELECT ir.*, m.nome as medicamento_nome " +
            "FROM item_receituario ir " +
            "JOIN medicamento m ON ir.medicamento_codigo = m.codigo " +
            "WHERE ir.receituario_codigo = ? " +
            "ORDER BY ir.codigo",
            rowMapper, receituarioCodigo);
    }

    public void save(ItemReceituario item) {
        jdbcTemplate.update(
            "INSERT INTO item_receituario (dosagem, intervalo_entre_doses, observacao, receituario_codigo, medicamento_codigo) VALUES (?, ?, ?, ?, ?)",
            item.getDosagem(), item.getIntervaloEntreDoses(), item.getObservacao(),
            item.getReceituarioCodigo(), item.getMedicamentoCodigo());
    }
}
