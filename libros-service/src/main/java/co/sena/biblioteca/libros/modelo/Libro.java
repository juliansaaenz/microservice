package co.sena.biblioteca.libros.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Escribe el título.")
    private String titulo;

    @NotBlank(message = "Escribe el autor.")
    private String autor;

    private Integer anio;

    @NotNull(message = "Indica cuántos ejemplares hay.")
    @Min(value = 0, message = "No puede ser negativo.")
    private Integer disponibles = 1;

    public Libro() {
    }

    public Libro(String titulo, String autor, Integer anio, Integer disponibles) {
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.disponibles = disponibles;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }
    public Integer getDisponibles() { return disponibles; }
    public void setDisponibles(Integer disponibles) { this.disponibles = disponibles; }
}
