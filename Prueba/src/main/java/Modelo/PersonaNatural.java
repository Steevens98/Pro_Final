package Modelo;

/**
 *
 * @author Usuario
 */
public class PersonaNatural extends Contacto{
    private String apellido;
    private String fechaNacimiento;

    public PersonaNatural(String id,String nombre, String apellido, String telefono, String email, String pais, String fechaNacimiento) {
        super(id,nombre, telefono, email, pais);
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @Override
    public String toString() {
        return super.toString() + ", apellido='" + apellido + '\'' + ", fechaNacimiento='" + fechaNacimiento + '\'';
    }
    
}
