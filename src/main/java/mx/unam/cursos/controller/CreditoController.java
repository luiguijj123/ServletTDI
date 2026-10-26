package mx.unam.cursos.controller;

import jakarta.servlet.http.HttpServletResponse;
import mx.unam.cursos.model.Credito;
import mx.unam.cursos.model.Pago;
import mx.unam.cursos.model.PagoForm;
import mx.unam.cursos.service.CreditoService;
import mx.unam.cursos.service.PagoService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Controller
public class CreditoController {
    private static final Logger LOG = LoggerFactory.getLogger(CreditoController.class);
    private final CreditoService creditos;
    private final PagoService pagos;
    public CreditoController(CreditoService creditos, PagoService pagos) {
        this.creditos = creditos;
        this.pagos = pagos;
    }

    @InitBinder("credito")
    public void camposCredito(WebDataBinder binder) {
        binder.setAllowedFields("fecha", "moneda", "monto", "periodo", "cuota", "tea", "vencimiento");
    }
    @InitBinder("pagoForm")
    public void camposPagos(WebDataBinder binder) {
        binder.setAllowedFields("pagos[*].id", "pagos[*].pago");
        binder.setAutoGrowCollectionLimit(100);
    }
    @GetMapping("/")
    public String inicio() { return "redirect:/credito"; }

    @GetMapping("/credito")
    public String mostrarFormulario(Model model, HttpServletResponse response) {
        model.addAttribute("credito", new Credito());
        PagoForm formulario = new PagoForm();
        model.addAttribute("pagoForm", formulario);
        try { cargarFilas(formulario, false); }
        catch (DataAccessException ex) { errorBase(model, response, ex); }
        return "formulario";
    }

    @PostMapping("/credito")
    public String guardar(@ModelAttribute("credito") Credito credito, BindingResult erroresCredito,
            @ModelAttribute("pagoForm") PagoForm formulario, BindingResult erroresPagos,
            Model model, HttpServletResponse response) {
        try {
            if (erroresCredito.hasErrors() || erroresPagos.hasErrors()) {
                throw new IllegalArgumentException("Revisa los datos del formulario y los identificadores de las filas.");
            }
            long id = pagos.guardar(credito, formulario);
            return "redirect:/credito/" + id;
        } catch (IllegalArgumentException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("error", ex.getMessage());
        } catch (DataAccessException ex) { errorBase(model, response, ex); }
        try { cargarFilas(formulario, true); }
        catch (DataAccessException ex) { errorBase(model, response, ex); formulario.setPagos(List.of()); }
        return "formulario";
    }

    private void cargarFilas(PagoForm formulario, boolean conservar) {
        List<Pago> filas = pagos.listar();
        if (conservar && formulario.getPagos() != null) {
            for (Pago fila : filas) {
                for (Pago enviado : formulario.getPagos()) {
                    if (enviado != null && fila.getId().equals(enviado.getId())) {
                        fila.setPago(enviado.getPago());
                        break;
                    }
                }
            }
        }
        formulario.setPagos(filas);
    }
    private void errorBase(Model model, HttpServletResponse response, DataAccessException ex) {
        LOG.error("No se pudo completar la operación en MySQL", ex);
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        model.addAttribute("error", "No se pudo completar la operación. Revisa la conexión y los permisos de MySQL.");
    }

    @GetMapping("/credito/{id}")
    public String mostrarResultado(@PathVariable long id, Model model) {
        Credito credito = creditos.buscar(id);
        if (credito == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Crédito no encontrado");
        model.addAttribute("credito", credito);
        model.addAttribute("pagos", pagos.listar());
        return "resultado";
    }
}
