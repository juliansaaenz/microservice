package co.sena.biblioteca.libros.config;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.springframework.stereotype.Component;

/** Guarda en memoria las últimas peticiones que llegaron a /api para mostrarlas en la vista. */
@Component
public class BitacoraEntrada {

    private static final int MAXIMO = 12;
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final Deque<Registro> registros = new ConcurrentLinkedDeque<>();

    public void registrar(String metodo, String ruta, int estado) {
        registros.addFirst(new Registro(LocalTime.now().format(HORA), metodo, ruta, estado));
        while (registros.size() > MAXIMO) {
            registros.pollLast();
        }
    }

    public List<Registro> ultimas() {
        return new ArrayList<>(registros);
    }

    public static class Registro {
        private final String hora;
        private final String metodo;
        private final String ruta;
        private final int estado;

        public Registro(String hora, String metodo, String ruta, int estado) {
            this.hora = hora;
            this.metodo = metodo;
            this.ruta = ruta;
            this.estado = estado;
        }

        public String getHora() { return hora; }
        public String getMetodo() { return metodo; }
        public String getRuta() { return ruta; }
        public int getEstado() { return estado; }
        public boolean isExito() { return estado < 400; }
    }
}
