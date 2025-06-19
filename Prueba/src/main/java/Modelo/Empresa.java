package Modelo;

/**
 *
 * @author Usuario
 */
public class Empresa extends Contacto{
    private String rubro;
    private String direccion;

    public Empresa(String id,String nombre, String telefono, String email, String pais, String rubro, String direccion) {
        super(id,nombre, telefono, email, pais);
        this.rubro = rubro;
        this.direccion = direccion;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return super.toString() + ", rubro='" + rubro + '\'' + ", direccion='" + direccion + '\'' + '}';
    }
    
}
