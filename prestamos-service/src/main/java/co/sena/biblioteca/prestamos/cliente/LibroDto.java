package co.sena.biblioteca.prestamos.cliente;

/**
 * Copia "de lectura" del JSON que responde libros-service.
 * No es una entidad: aquí no hay tabla de libros.
 */
public class LibroDto {

    private Long id;
    private String titulo;
    private String autor;
    private Integer anio;
    private Integer disponibles;

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
