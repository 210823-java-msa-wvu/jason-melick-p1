package dev.melick.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.melick.models.Request;
import dev.melick.repositories.ReqRepo;
import dev.melick.services.ReqService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SubmitServlet extends HttpServlet {

    private static ObjectMapper om = new ObjectMapper();
    private static ReqRepo rr = new ReqRepo();
    private static ReqService rs = new ReqService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        om.setDateFormat(df);

        System.out.println("POST request received from Submit page");

        try {
            Request newReq = om.readValue(request.getReader(), Request.class);
            System.out.println("Request received in SubmitServlet doPost try block" + newReq);
            rs.createRequest(newReq);
            response.setStatus(200);
            //response.sendRedirect("view-my-requests.html");
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
