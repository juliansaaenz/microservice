package co.sena.biblioteca.prestamos.modelo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Lo que llena el usuario en el formulario "Nuevo préstamo". */
public class PrestamoForm {

    @NotNull(message = "Elige un libro.")
    private Long libroId;

    @NotBlank(message = "Escribe el nombre del estudiante.")
    private String estudiante;

    public Long getLibroId() { return libroId; }
    public void setLibroId(Long libroId) { this.libroId = libroId; }
    public String getEstudiante() { return estudiante; }
    public void setEstudiante(String estudiante) { this.estudiante = estudiante; }
}
