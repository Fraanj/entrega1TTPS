package cinema.listeners;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class CinemaContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        String cantidadLatasParam = context.getInitParameter("cantidadLatas");

        int cantidadLatas = 0;
        if (cantidadLatasParam != null) {
            try {
                cantidadLatas = Integer.parseInt(cantidadLatasParam);
            } catch (NumberFormatException e) {
                System.out.println("Error al parsear cantidadLatas, usando valor por defecto: 0");
            }
        }

        context.setAttribute("cantidadLatas", cantidadLatas);
    }
}