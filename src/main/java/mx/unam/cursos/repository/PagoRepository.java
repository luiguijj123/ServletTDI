package mx.unam.cursos.repository;

import java.math.BigDecimal;
import java.util.List;
import mx.unam.cursos.model.Pago;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PagoRepository {
    private final JdbcTemplate jdbc;
    public PagoRepository(JdbcTemplate jdbc) { 
        this.jdbc = jdbc; 
    }

    public List<Pago> listar() { 
        return consultar(false); 
    }
    
    public List<Pago> listarParaActualizar() { 
        return consultar(true); 
    }

    private List<Pago> consultar(boolean bloquear) {
        return jdbc.query("SELECT id, fecha, mes, tarjeta, transaccion, gastos, pago FROM pagos_tarjeta ORDER BY id"
            + (bloquear ? " FOR UPDATE" : ""), (rs, fila) -> {
                Pago pago = new Pago();
                pago.setId(rs.getLong("id"));
                pago.setFecha(rs.getDate("fecha").toLocalDate().toString());
                pago.setMes(rs.getString("mes"));
                pago.setTarjeta(rs.getString("tarjeta"));
                pago.setTransaccion(rs.getString("transaccion"));
                BigDecimal gastos = rs.getBigDecimal("gastos");
                pago.setGastos(gastos == null ? "" : gastos.toPlainString());
                BigDecimal importe = rs.getBigDecimal("pago");
                pago.setPago(importe == null ? "" : importe.toPlainString());
                return pago;
            }
        );
    }

    public void actualizar(Pago pago) {
        BigDecimal importe = pago.getPago().isBlank() ? null : new BigDecimal(pago.getPago());
        jdbc.update("UPDATE pagos_tarjeta SET pago = ? WHERE id = ?", importe, pago.getId());
    }
}
