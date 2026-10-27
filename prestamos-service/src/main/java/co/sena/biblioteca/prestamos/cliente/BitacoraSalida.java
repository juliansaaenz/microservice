package co.sena.biblioteca.prestamos.cliente;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.springframework.stereotype.Component;

/** Guarda las últimas peticiones que ESTE servicio le hizo a libros-service. */
@Component
public class BitacoraSalida {

    private static final int MAXIMO = 12;
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final Deque<Registro> registros = new ConcurrentLinkedDeque<>();

    public void registrar(String metodo, String url, String estado, long milisegundos, boolean exito) {
        registros.addFirst(new Registro(LocalTime.now().format(HORA), metodo, url, estado, milisegundos, exito));
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
        private final String url;
        private final String estado;
        private final long milisegundos;
        private final boolean exito;

        public Registro(String hora, String metodo, String url, String estado, long milisegundos, boolean exito) {
            this.hora = hora;
            this.metodo = metodo;
            this.url = url;
            this.estado = estado;
            this.milisegundos = milisegundos;
            this.exito = exito;
        }

        public String getHora() { return hora; }
        public String getMetodo() { return metodo; }
        public String getUrl() { return url; }
        public String getEstado() { return estado; }
        public long getMilisegundos() { return milisegundos; }
        public boolean isExito() { return exito; }
    }
}
