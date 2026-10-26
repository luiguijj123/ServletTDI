package mx.unam.cursos.model;
import java.util.ArrayList;
import java.util.List;

public class PagoForm {
    private List<Pago> pagos = new ArrayList<>();
    
    public List<Pago> getPagos() { 
        return pagos; 
    }
    
    public void setPagos(List<Pago> pagos) { 
        this.pagos = pagos; 
    }
}
