package co.sena.biblioteca.libros.controlador;

import co.sena.biblioteca.libros.modelo.Libro;
import co.sena.biblioteca.libros.repositorio.LibroRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API REST de libros. Esta es la "puerta" por la que préstamos-service
 * (u otro servicio) le habla a este microservicio. Todo viaja en JSON.
 */
@RestController
@RequestMapping("/api/libros")
public class LibroApiController {

    private final LibroRepository repo;

    public LibroApiController(LibroRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Libro> listar() {
        return repo.findAll(Sort.by("titulo"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> buscar(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Libro> crear(@Valid @RequestBody Libro libro) {
        libro.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(libro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @Valid @RequestBody Libro datos) {
        return repo.findById(id).map(libro -> {
            libro.setTitulo(datos.getTitulo());
            libro.setAutor(datos.getAutor());
            libro.setAnio(datos.getAnio());
            libro.setDisponibles(datos.getDisponibles());
            return ResponseEntity.ok(repo.save(libro));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /** Lo llama préstamos-service: descuenta un ejemplar. */
    @PutMapping("/{id}/prestar")
    public ResponseEntity<?> prestar(@PathVariable Long id) {
        Optional<Libro> encontrado = repo.findById(id);
        if (encontrado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No existe un libro con id " + id));
        }
        Libro libro = encontrado.get();
        if (libro.getDisponibles() <= 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensaje", "No quedan ejemplares de " + libro.getTitulo()));
        }
        libro.setDisponibles(libro.getDisponibles() - 1);
        return ResponseEntity.ok(repo.save(libro));
    }

    /** Lo llama préstamos-service: devuelve un ejemplar al estante. */
    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolver(@PathVariable Long id) {
        Optional<Libro> encontrado = repo.findById(id);
        if (encontrado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No existe un libro con id " + id));
        }
        Libro libro = encontrado.get();
        libro.setDisponibles(libro.getDisponibles() + 1);
        return ResponseEntity.ok(repo.save(libro));
    }
}
