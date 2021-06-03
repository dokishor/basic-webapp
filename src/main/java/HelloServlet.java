import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        String name = request.getParameter("username");
        String age = request.getParameter("userPassword");
        String userCaptcha = request.getParameter("userCaptcha");
        String serverCaptcha = (String) request.getSession().getAttribute("serverCaptcha");
        Integer tryingCounter = (Integer) request.getSession().getAttribute("tryingCounter");
        if (!userCaptcha.equals(serverCaptcha))
        {
            String path = "/index.jsp";
            ServletContext servletContext = getServletContext();
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(path);
            requestDispatcher.forward(request, response);
        }
        try (PrintWriter writer = response.getWriter())
        {
            writer.println("<p>Name: " + name + "</p>");
            writer.println("<p>Password: " + age + "</p>");
            writer.println("<p>Server captcha: " + serverCaptcha + "</p>");
            writer.println("<p>User captcha: " + userCaptcha + "</p>");
            writer.println("<p>Trying counter: " + tryingCounter + "</p>");
        }
    }
}
