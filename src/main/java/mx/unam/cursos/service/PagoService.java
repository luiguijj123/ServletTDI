package mx.unam.cursos.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mx.unam.cursos.model.Credito;
import mx.unam.cursos.model.Pago;
import mx.unam.cursos.model.PagoForm;
import mx.unam.cursos.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PagoService {
    private final PagoRepository repository;
    private final CreditoService creditos;
    
    public PagoService(PagoRepository repository, CreditoService creditos) {
        this.repository = repository;
        this.creditos = creditos;
    }
    public List<Pago> listar() { 
        return repository.listar(); 
    }

    @Transactional
    public long guardar(Credito credito, PagoForm formulario) {
        List<Pago> actuales = repository.listarParaActualizar();
        List<Pago> enviados = formulario.getPagos();
        if (enviados == null || enviados.size() != actuales.size() || actuales.isEmpty()) {
            throw new IllegalArgumentException("La tabla de pagos está incompleta. Recarga el formulario.");
        }
        Set<Long> pendientes = new HashSet<>();
        for (Pago pago : actuales) pendientes.add(pago.getId());
        for (Pago pago : enviados) {
            if (pago == null || !pendientes.remove(pago.getId())) {
                throw new IllegalArgumentException("Las filas de pagos no son válidas. Recarga el formulario.");
            }
            String valor = pago.getPago() == null ? "" : pago.getPago().trim();
            if (!valor.isEmpty() && (valor.length() > 16 || !valor.matches("[0-9]+(\\.[0-9]{1,2})?")
                    || new BigDecimal(valor).compareTo(new BigDecimal("9999999999999.99")) > 0)) {
                throw new IllegalArgumentException("Los pagos deben ser números no negativos con hasta dos decimales y máximo 9999999999999.99.");
            }
            pago.setPago(valor);
        }
        long id = creditos.guardar(credito);
        for (Pago pago : enviados) repository.actualizar(pago);
        return id;
    }
}
