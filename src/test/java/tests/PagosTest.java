package tests;

import mx.unam.cursos.model.*;
import mx.unam.cursos.service.*;
import mx.unam.cursos.repository.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.core.io.ClassPathResource;
import org.h2.jdbcx.JdbcDataSource;
import javax.sql.DataSource;
import java.util.UUID;

class PagosTest {
    @Configuration
    @EnableTransactionManagement
    @Import({CreditoRepository.class, CreditoService.class, PagoRepository.class, PagoService.class})
    static class Config {
        @Bean DataSource dataSource() {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL("jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=MySQL;DB_CLOSE_DELAY=-1");
            return ds;
        }
        @Bean JdbcTemplate jdbc(DataSource ds) { return new JdbcTemplate(ds); }
        @Bean DataSourceTransactionManager transactionManager(DataSource ds) { return new DataSourceTransactionManager(ds); }
    }
    AnnotationConfigApplicationContext context;
    JdbcTemplate jdbc;
    PagoService service;
    @BeforeEach void preparar() {
        context = new AnnotationConfigApplicationContext(Config.class);
        jdbc = context.getBean(JdbcTemplate.class);
        new ResourceDatabasePopulator(new ClassPathResource("schema.sql")).execute(context.getBean(DataSource.class));
        service = context.getBean(PagoService.class);
    }
    @AfterEach void cerrar() { context.close(); }
    Credito credito() {
        Credito c = new Credito();
        c.setFecha("2026-10-18"); c.setMoneda("PEN"); c.setMonto("1000.00");
        c.setPeriodo("12"); c.setCuota("100.00"); c.setTea("12.00"); c.setVencimiento("2027-10-18");
        return c;
    }
    PagoForm formulario() {
        PagoForm f = new PagoForm(); f.setPagos(service.listar()); return f;
    }
    int creditos() { return jdbc.queryForObject("SELECT COUNT(*) FROM creditos", Integer.class); }
    @Test void guardaCreditoYPagosIndependientes() {
        PagoForm f = formulario();
        f.getPagos().get(0).setPago("123.45");
        f.getPagos().get(1).setPago("0");
        f.getPagos().get(0).setTarjeta("ALTERADA");
        long id = service.guardar(credito(), f);
        assertNotNull(context.getBean(CreditoService.class).buscar(id));
        assertEquals("123.45", service.listar().get(0).getPago());
        assertEquals("0.00", service.listar().get(1).getPago());
        assertEquals("", service.listar().get(2).getPago());
        assertEquals("VISA", service.listar().get(0).getTarjeta());
    }
    @Test void rechazaImportesInvalidosSinGuardar() {
        for (String valor : new String[]{"-1", "1.234", "abc", "10000000000000", "1e3"}) {
            PagoForm f = formulario(); f.getPagos().get(9).setPago(valor);
            assertThrows(IllegalArgumentException.class, () -> service.guardar(credito(), f));
            assertEquals(0, creditos());
            assertEquals("", service.listar().get(0).getPago());
        }
    }
    @Test void rechazaFilasDuplicadasDesconocidasOIncompletas() {
        PagoForm duplicado = formulario(); duplicado.getPagos().get(1).setId(1L);
        assertThrows(IllegalArgumentException.class, () -> service.guardar(credito(), duplicado));
        PagoForm desconocido = formulario(); desconocido.getPagos().get(0).setId(999L);
        assertThrows(IllegalArgumentException.class, () -> service.guardar(credito(), desconocido));
        PagoForm incompleto = formulario(); incompleto.getPagos().remove(0);
        assertThrows(IllegalArgumentException.class, () -> service.guardar(credito(), incompleto));
        assertEquals(0, creditos());
    }
    @Test void creditoInvalidoNoModificaPagos() {
        Credito c = credito(); c.setPeriodo("0");
        PagoForm f = formulario(); f.getPagos().get(0).setPago("250");
        assertThrows(IllegalArgumentException.class, () -> service.guardar(c, f));
        assertEquals(0, creditos()); assertEquals("", service.listar().get(0).getPago());
    }
    @Test void errorAlActualizarRevierteCreditoYPagosAnteriores() {
        jdbc.execute("ALTER TABLE pagos_tarjeta ADD CONSTRAINT prueba_error CHECK (pago <> 99)");
        PagoForm f = formulario(); f.getPagos().get(0).setPago("100"); f.getPagos().get(9).setPago("99");
        assertThrows(org.springframework.dao.DataAccessException.class, () -> service.guardar(credito(), f));
        assertEquals(0, creditos()); assertEquals("", service.listar().get(0).getPago());
    }
    @Test void reinicioNoReemplazaLosPagos() {
        PagoForm f = formulario(); f.getPagos().get(0).setPago("25"); service.guardar(credito(), f);
        new ResourceDatabasePopulator(new ClassPathResource("schema.sql")).execute(context.getBean(DataSource.class));
        assertEquals(10, service.listar().size()); assertEquals("25.00", service.listar().get(0).getPago());
    }
}
