package co.sena.biblioteca.libros.controlador;

import co.sena.biblioteca.libros.config.BitacoraEntrada;
import co.sena.biblioteca.libros.modelo.Libro;
import co.sena.biblioteca.libros.repositorio.LibroRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Vistas Thymeleaf (el CRUD que ve el bibliotecario en el navegador). */
@Controller
public class LibroVistaController {

    private final LibroRepository repo;
    private final BitacoraEntrada bitacora;

    public LibroVistaController(LibroRepository repo, BitacoraEntrada bitacora) {
        this.repo = repo;
        this.bitacora = bitacora;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/libros";
    }

    @GetMapping("/libros")
    public String lista(Model model) {
        model.addAttribute("libros", repo.findAll(Sort.by("titulo")));
        model.addAttribute("registros", bitacora.ultimas());
        return "libros/lista";
    }

    @GetMapping("/libros/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("libro", new Libro());
        return "libros/formulario";
    }

    @GetMapping("/libros/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return repo.findById(id).map(libro -> {
            model.addAttribute("libro", libro);
            return "libros/formulario";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Ese libro ya no existe.");
            return "redirect:/libros";
        });
    }

    @PostMapping("/libros/guardar")
    public String guardar(@Valid @ModelAttribute("libro") Libro libro, BindingResult errores,
                          RedirectAttributes ra) {
        if (errores.hasErrors()) {
            return "libros/formulario";
        }
        repo.save(libro);
        ra.addFlashAttribute("exito", "Libro guardado: " + libro.getTitulo() + ".");
        return "redirect:/libros";
    }

    @PostMapping("/libros/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
        ra.addFlashAttribute("exito", "Libro eliminado.");
        return "redirect:/libros";
    }
}
