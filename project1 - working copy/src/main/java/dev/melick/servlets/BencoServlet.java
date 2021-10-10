package dev.melick.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.bind.v2.TODO;
import dev.melick.models.Employee;
import dev.melick.models.Request;
import dev.melick.repositories.EmpRepo;
import dev.melick.repositories.ReqRepo;
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

public class BencoServlet extends HttpServlet {

    private static ObjectMapper om = new ObjectMapper();
    private static EmpRepo er = new EmpRepo();
    private static EmpService es = new EmpService();
    private static ReqService rs = new ReqService();
    private static ReqRepo rr = new ReqRepo();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        om.setDateFormat(df);

        System.out.println("GET request received by the BencoServlet");

        StringBuilder uriString = new StringBuilder(request.getRequestURI());
        System.out.println(uriString);
        uriString.replace(0, request.getContextPath().length() + 1, "");
        System.out.println("uriString: " + uriString);
        String uriValue = String.valueOf(uriString);

        if (uriValue.equals("benco/reimbursements")){

            System.out.println("GET request received in BencoServlet reimbursement if block");

            ArrayList<Request> reimbursements = rr.getReimbursements();

            if (reimbursements.size() != 0) {
                System.out.println("The reimbursementsServlet successfully retrieved reimbursements");
                reimbursements.forEach(System.out::println);
                response.setStatus(200);
                response.getWriter().write(om.writeValueAsString(reimbursements));
            } else {
                response.setStatus(204);
                response.getWriter().println("<html><body><p>There are no reimbursements to view.</p></body></html>");
            }

        } else {

            Integer empId = Integer.parseInt(uriString.substring(uriString.indexOf("/") + 1)); // this will set the attribute 'path' to the employee ID number

            System.out.println("Emp ID test line from inside the BencoServlet: " + empId);

            ArrayList<Request> requests = rs.getBencoRequests();

            if (requests.size() != 0) {
                System.out.println("The benco servlet successfully pulled an array list from the DB");
                requests.forEach(System.out::println);
                response.setStatus(200);
                response.getWriter().write(om.writeValueAsString(requests));
            } else {
                response.setStatus(204);
                response.getWriter().println("<html><body><p>You have no requests requiring action.</p></body></html>");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("POST request received by the BencoServlet");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        om.setDateFormat(df);

        StringBuilder uriString = new StringBuilder(request.getRequestURI()); // uri = /project1/view-my-requests/...
        System.out.println(uriString);
        uriString.replace(0, request.getContextPath().length() + 1, ""); // now we have => view-my-requests/...
        System.out.println(uriString);

        if (uriString.indexOf("/") != -1) {
            String switcher = uriString.substring(uriString.indexOf("/") + 1); // we now have add-sup-inf, add-dh-inf, add-benco-inf, or grade-sub
            //uriString.replace(uriString.indexOf("/"), uriString.length() +1, ""); // at this point uriString = 'books'
            System.out.println("Test line from inside the BencoServlet POST switcher: " + switcher);

            switch (switcher) {

                case "decision":{

                    try {

                        Request req = om.readValue(request.getReader(), Request.class);
                        System.out.println("Request received in ManagementServlet decision doPost block \n" + req);
                        rs.checkGrade(req);
                        rs.bencoDecision(req);
                        response.setStatus(204);
                        response.getWriter().write("Request updated");
                        //response.sendRedirect("http://localhost:8080/project1/empmenu.html");

                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
}