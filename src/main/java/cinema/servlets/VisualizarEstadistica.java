package cinema.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletContext;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/visualizar-estadistica")
public class VisualizarEstadistica extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext ctx = getServletContext();

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Estadísticas de Ventas</title>");
        out.println("<link rel=\"stylesheet\" href=\"css/estadisticas.css\">");
        out.println("</head><body>");
        out.println("<div class='container'>");
        out.println("<h1>Entradas vendidas por película</h1>");
        out.println("<table>");
        out.println("<tr><th>Película</th><th>Entradas Vendidas</th></tr>");

        String[] peliculas = {"LILO_STICH", "LA_MUJER_DE_LA_FILA", "DEMON_SLAYER", "LA_LUZ_QUE_IMAGINAMOS"};
        String[] nombres = {"LILO & STICH", "LA MUJER DE LA FILA", "DEMON SLAYER", "LA LUZ QUE IMAGINAMOS"};

        for (int i = 0; i < peliculas.length; i++) {
            Integer cant = (Integer) ctx.getAttribute(peliculas[i]);
            if (cant == null) cant = 0;
            out.println("<tr><td>" + nombres[i] + "</td><td>" + cant + "</td></tr>");
        }

        out.println("</table>");

        // Mostrar latas disponibles
        Integer latasDisponibles = (Integer) ctx.getAttribute("cantidadLatas");
        if (latasDisponibles == null) latasDisponibles = 0;
        out.println("<p style='margin-top:20px; font-weight:bold;'>Latas de pochoclo disponibles: "
                + latasDisponibles + "</p>");

        out.println("</div>"); // container
        out.println("</body></html>");
    }
}
