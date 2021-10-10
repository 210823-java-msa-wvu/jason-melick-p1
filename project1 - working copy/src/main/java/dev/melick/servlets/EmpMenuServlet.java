package dev.melick.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.melick.models.Request;
import dev.melick.repositories.EmpRepo;
import dev.melick.services.EmpService;
import dev.melick.services.ReqService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.bind.v2.TODO;
import dev.melick.models.Employee;
import dev.melick.models.Request;
import dev.melick.repositories.EmpRepo;
import dev.melick.services.EmpService;
import dev.melick.services.ReqService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EmpMenuServlet extends HttpServlet {

    private static ObjectMapper om = new ObjectMapper();
    private static EmpRepo er = new EmpRepo();
    private static EmpService es = new EmpService();
    private static ReqService rs = new ReqService();

    @Override
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        om.setDateFormat(df);

        System.out.println("GET request received by the ViewMyRequestsServlet");

        StringBuilder uriString = new StringBuilder(request.getRequestURI()); // uri = /LibraryServlet/books/1
        System.out.println(uriString);
        uriString.replace(0, request.getContextPath().length() + 1, ""); // now we have => books/1
        System.out.println(uriString);

        if (uriString.indexOf("/") != -1) {
            Integer empId = Integer.parseInt(uriString.substring(uriString.indexOf("/") + 1)); // this will set the attribute 'path' the employee ID
            //uriString.replace(uriString.indexOf("/"), uriString.length() +1, "");
            //System.out.println("Emp ID test line from inside the ViewMyRequestsServlet: " + empId);
            Employee e = er.getByEmpId(empId);

            if (e.getEmpId() != 0 ) {
                System.out.println("Employee info pulled successfully at EmpmenuServlet");
                //myRequests.forEach(System.out::println);
                response.setStatus(200);
                response.getWriter().write(om.writeValueAsString(e));
            }
        }
    }
}

