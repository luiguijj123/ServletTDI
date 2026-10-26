package mx.unam.cursos.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import mx.unam.cursos.model.Credito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CreditoRepository {
    private final JdbcTemplate jdbc;

    public CreditoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public long guardar(Credito credito) {
        String sql = "INSERT INTO creditos (fecha, moneda, monto, periodo, cuota, tea, vencimiento) VALUES (?, ?, ?, ?, ?, ?, ?)";
        GeneratedKeyHolder clave = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setDate(1, Date.valueOf(credito.getFecha()));
            statement.setString(2, credito.getMoneda());
            statement.setBigDecimal(3, new BigDecimal(credito.getMonto()));
            statement.setInt(4, Integer.parseInt(credito.getPeriodo()));
            statement.setBigDecimal(5, new BigDecimal(credito.getCuota()));
            statement.setBigDecimal(6, new BigDecimal(credito.getTea()));
            statement.setDate(7, Date.valueOf(credito.getVencimiento()));
            return statement;
        }, clave);
        return clave.getKey().longValue();
    }

    public Credito buscar(long id) {
        List<Credito> resultados = jdbc.query(
            "SELECT fecha, moneda, monto, periodo, cuota, tea, vencimiento FROM creditos WHERE id = ?",
            (rs, fila) -> {
                Credito credito = new Credito();
                credito.setFecha(rs.getDate("fecha").toLocalDate().toString());
                credito.setMoneda(rs.getString("moneda"));
                credito.setMonto(rs.getBigDecimal("monto").toPlainString());
                credito.setPeriodo(Integer.toString(rs.getInt("periodo")));
                credito.setCuota(rs.getBigDecimal("cuota").toPlainString());
                credito.setTea(rs.getBigDecimal("tea").toPlainString());
                credito.setVencimiento(rs.getDate("vencimiento").toLocalDate().toString());
                return credito;
            }, id);
        return resultados.isEmpty() ? null : resultados.getFirst();
    }
}
