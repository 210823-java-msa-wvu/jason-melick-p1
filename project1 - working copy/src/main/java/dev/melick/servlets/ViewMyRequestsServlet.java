package dev.melick.servlets;

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

public class ViewMyRequestsServlet extends HttpServlet {

    private static ObjectMapper om = new ObjectMapper();
    private static EmpRepo er = new EmpRepo();
    private static EmpService es = new EmpService();
    private static ReqService rs = new ReqService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        om.setDateFormat(df);

        System.out.println("GET request received by the ViewMyRequestsServlet");
        //Integer empId = Integer.parseInt(request.getParameter("empId"));
        StringBuilder uriString = new StringBuilder(request.getRequestURI());
        System.out.println(uriString);
        uriString.replace(0, request.getContextPath().length() + 1, "");
        System.out.println(uriString);

        if (uriString.indexOf("/") != -1) {
            Integer empId = Integer.parseInt(uriString.substring(uriString.indexOf("/") + 1)); // this will set the attribute 'path' to '1'
            //uriString.replace(uriString.indexOf("/"), uriString.length() +1, ""); // at this point uriString = 'books'
            System.out.println("Emp ID test line from inside the ViewMyRequestsServlet doGet() method first if block: " + empId);
            ArrayList<Request> myRequests = rs.getMyRequests(empId);

            if(myRequests.size() != 0){
                System.out.println("The view my requests servlet successfully pulled an array list from the DB");
                //myRequests.forEach(System.out::println);
                response.setStatus(200);
                response.getWriter().write(om.writeValueAsString(myRequests));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("POST request received by the ViewMyRequestsServlet");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        om.setDateFormat(df);

        StringBuilder uriString = new StringBuilder(request.getRequestURI()); // uri = /project1/view-my-requests/...
        System.out.println(uriString);
        uriString.replace(0, request.getContextPath().length() + 1, ""); // now we have => view-my-requests/...
        System.out.println(uriString);

        if (uriString.indexOf("/") != -1) {
            String switcher = uriString.substring(uriString.indexOf("/") + 1); // we now have add-sup-inf, add-dh-inf, add-benco-inf, or grade-sub
            //uriString.replace(uriString.indexOf("/"), uriString.length() +1, ""); // at this point uriString = 'books'
            System.out.println("Emp ID test line from inside the ViewMyRequestsServlet POST switcher: " + switcher);

            switch(switcher){

                case "add-sup-inf":{

                    try {
                        Request req = om.readValue(request.getReader(), Request.class);
                        System.out.println("Request received in ViewMyRequests add-sup-inf doPost block \n" + req);
                        rs.attachSupInfo(req);
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
                case "add-dh-inf":{

                    try {

                        Request req = om.readValue(request.getReader(), Request.class);
                        System.out.println("Request received in ViewMyRequests doPost add-dh-inf block: \n" + req);
                        rs.attachDhInfo(req);
                        response.setStatus(204);
                        //response.sendRedirect("empmenu.html");

                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "add-benco-inf":{

                    try {
                        Request req = om.readValue(request.getReader(), Request.class);
                        System.out.println("Request received in ViewMyRequests add-benco-inf block: " + req);
                        rs.attachBencoInfo(req);
                        response.setStatus(204);
                        //response.sendRedirect("empmenu.html");
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "grade-sub":{

                    try {
                        Request req = om.readValue(request.getReader(), Request.class);
                        System.out.println("Request received in ViewMyRequests grade-sub block: " + req);
                        rs.attachGradeInfo(req);
                        response.setStatus(204);
                        response.sendRedirect("empmenu.html");
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "emp-reject":{

                    try {
                        Request req = om.readValue(request.getReader(), Request.class);
                        System.out.println("Request received in ViewMyRequests emp-reject block: " + req);
                        rs.empRejectStatus(req);
                        response.setStatus(204);
                        response.sendRedirect("empmenu.html");
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                default: System.out.println("POST request received from an unexpected source in ViewMyRequestsServlet");

            }
        }else{
            response.setStatus(405);
        }
    }
}
