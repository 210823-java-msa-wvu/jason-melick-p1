package dev.melick.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.melick.models.Employee;
import dev.melick.repositories.EmpRepo;
import dev.melick.services.EmpService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {

    private static ObjectMapper om = new ObjectMapper();
    private static EmpRepo er = new EmpRepo();
    private static EmpService es = new EmpService();
    private static final Logger logger = LogManager.getLogger(LoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            PrintWriter pw = response.getWriter();
            logger.info("Retrieving user from the database to compare credentials. username: " + username);
            Employee emp = er.getByUserName(username);

            if (es.credPass(password, emp)) {

                emp.setPassword("PRIVATE");
                String empString = om.writeValueAsString(emp);
                //System.out.println(empString);
                Cookie empCookie = new Cookie("empInfo", empString);
                empCookie.setMaxAge(60*60);
                response.addCookie(empCookie);
                response.setStatus(200);
                response.sendRedirect("empmenu.html");
                System.out.println("credPass successful");

            } else {
                pw.write("<h1>Your username or password do not match our records. Please hit the 'back' arrow in your browser to try again.</h1>");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
/*     response.getWriter().write(om.writeValueAsString(emp));

       PrintWriter pw = response.getWriter();

        String message = "<h1>username: " + creds.getUsername() + "  password: " + creds.getPassword() + "</h1>";

        pw.write(message);
    }*/
}
