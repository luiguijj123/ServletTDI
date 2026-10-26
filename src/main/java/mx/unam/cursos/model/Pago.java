package mx.unam.cursos.model;

public class Pago {
    private Long id;
    private String fecha;
    private String mes;
    private String tarjeta;
    private String transaccion;
    private String gastos;
    private String pago;
    
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getFecha() { 
        return fecha; 
    }
    
    public void setFecha(String fecha) { 
        this.fecha = fecha; 
    }
    
    public String getMes() { 
        return mes; 
    }
    
    public void setMes(String mes) { 
        this.mes = mes; 
    }
    
    public String getTarjeta() { 
        return tarjeta; 
    }
    
    public void setTarjeta(String tarjeta) { 
        this.tarjeta = tarjeta; 
    }
    
    public String getTransaccion() { 
        return transaccion; 
    }
    
    public void setTransaccion(String transaccion) { 
        this.transaccion = transaccion; 
    }
    
    public String getGastos() { 
        return gastos; 
    }
    
    public void setGastos(String gastos) { 
        this.gastos = gastos; 
    }
    
    public String getPago() { 
        return pago; 
    }
    
    public void setPago(String pago) { 
        this.pago = pago; 
    }
}
