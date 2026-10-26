package mx.unam.cursos.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import mx.unam.cursos.model.Credito;
import mx.unam.cursos.repository.CreditoRepository;
import org.springframework.stereotype.Service;

@Service
public class CreditoService {
    private final CreditoRepository repository;

    public CreditoService(CreditoRepository repository) {
        this.repository = repository;
    }

    public long guardar(Credito credito) {
        validar(credito);
        return repository.guardar(credito);
    }

    public Credito buscar(long id) {
        return repository.buscar(id);
    }

    private void validar(Credito credito) {
        Map<String, String> datos = new LinkedHashMap<>();
        datos.put("fecha", credito.getFecha());
        datos.put("moneda", credito.getMoneda());
        datos.put("monto", credito.getMonto());
        datos.put("periodo", credito.getPeriodo());
        datos.put("cuota", credito.getCuota());
        datos.put("tea", credito.getTea());
        datos.put("vencimiento", credito.getVencimiento());
        for (Map.Entry<String, String> campo : datos.entrySet()) {
            String valor = campo.getValue();
            if (valor == null || valor.trim().isEmpty() || valor.trim().length() > 100) {
                throw new IllegalArgumentException("Completa todos los campos con valores de hasta 100 caracteres.");
            }
            campo.setValue(valor.trim());
        }
        credito.setFecha(datos.get("fecha"));
        credito.setMoneda(datos.get("moneda"));
        credito.setMonto(datos.get("monto"));
        credito.setPeriodo(datos.get("periodo"));
        credito.setCuota(datos.get("cuota"));
        credito.setTea(datos.get("tea"));
        credito.setVencimiento(datos.get("vencimiento"));

        if (!credito.getMoneda().equals("PEN") && !credito.getMoneda().equals("USD")) {
            throw new IllegalArgumentException("Selecciona Soles o Dólares.");
        }
        try {
            for (String campo : new String[]{"fecha", "vencimiento"}) {
                LocalDate fecha = LocalDate.parse(datos.get(campo));
                if (fecha.getYear() < 1000 || fecha.getYear() > 9999) {
                    throw new IllegalArgumentException("Las fechas deben tener un año entre 1000 y 9999.");
                }
            }
            if (Integer.parseInt(credito.getPeriodo()) < 1) {
                throw new IllegalArgumentException("El periodo debe ser un número entero de meses mayor que cero.");
            }
        } catch (NumberFormatException | DateTimeParseException ex) {
            throw new IllegalArgumentException("Revisa las fechas y el periodo: usa fechas válidas y meses enteros.");
        }
        for (String campo : new String[]{"monto", "cuota", "tea"}) {
            String valor = datos.get(campo);
            if (!valor.matches("[0-9]+(\\.[0-9]{1,2})?")) {
                throw new IllegalArgumentException("Monto, cuota y TEA deben ser números no negativos con hasta dos decimales.");
            }
            if (new BigDecimal(valor).compareTo(new BigDecimal("9999999999999.99")) > 0) {
                throw new IllegalArgumentException("Monto, cuota y TEA no pueden superar 9999999999999.99.");
            }
        }
    }
}
