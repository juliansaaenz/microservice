package co.sena.biblioteca.libros.config;

import co.sena.biblioteca.libros.modelo.Libro;
import co.sena.biblioteca.libros.repositorio.LibroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Libros de ejemplo para que la demo arranque con datos. */
@Component
public class DatosIniciales implements CommandLineRunner {

    private final LibroRepository repo;

    public DatosIniciales(LibroRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        if (repo.count() > 0) {
            return;
        }
        repo.save(new Libro("Cien años de soledad", "Gabriel García Márquez", 1967, 3));
        repo.save(new Libro("La vorágine", "José Eustasio Rivera", 1924, 2));
        repo.save(new Libro("El coronel no tiene quien le escriba", "Gabriel García Márquez", 1961, 1));
        repo.save(new Libro("Clean Code", "Robert C. Martin", 2008, 4));
        repo.save(new Libro("Delirio", "Laura Restrepo", 2004, 0));
    }
}
