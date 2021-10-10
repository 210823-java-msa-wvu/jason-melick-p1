package dev.melick.services;

import dev.melick.models.Attachment;
import dev.melick.models.Employee;
import dev.melick.models.Request;
import dev.melick.repositories.AttRepo;
import dev.melick.repositories.EmpRepo;
import dev.melick.repositories.ReqRepo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;


public class ReqService {

    private static ReqRepo rr = new ReqRepo();
    private static AttRepo ar = new AttRepo();
    private static ReqService rs = new ReqService();
    private static EmpService es = new EmpService();
    private static AttService as = new AttService();
    private static EmpRepo er = new EmpRepo();
    private static Logger logger = LogManager.getLogger(ReqService.class);



    public void createRequest(Request newRequest) {

        logger.info("request received in ReqService createRequest() method: " + newRequest);
        //System.out.println("request received in ReqService createRequest() method: " + newRequest);

        BigDecimal costMultiplier = BigDecimal.valueOf(0.0);

        switch (newRequest.getEventType()) {

            case "university": {
                costMultiplier = BigDecimal.valueOf(0.8);
                break;
            }
            case "seminar": {
                costMultiplier = BigDecimal.valueOf(0.6);
                break;
            }
            case "certPrep": {
                costMultiplier = BigDecimal.valueOf(0.75);
                break;
            }
            case "certification": {
                costMultiplier = BigDecimal.valueOf(1);
                break;
            }
            case "techTraining": {
                costMultiplier = BigDecimal.valueOf(0.9);
                break;
            }
            case "other": {
                costMultiplier = BigDecimal.valueOf(0.3);
                break;
            }
        }

        //System.out.println(String.valueOf(newRequest.getStartsOn()));
        LocalDate todayDate = LocalDate.now();
        LocalDate startDate = newRequest.getStartsOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        //System.out.println("today: " + String.valueOf(todayDate) + " start: " + String.valueOf(startDate));
        //System.out.println("days between" + String.valueOf(DAYS.between(startDate, todayDate)));
        if (DAYS.between(todayDate, startDate) <= 14){
            newRequest.setUrgent(true);
        }

        BigDecimal proj = newRequest.getEventCost().multiply(costMultiplier);
        newRequest.setProjAmt(proj);

        if(newRequest.getProjAmt().compareTo(BigDecimal.valueOf(1000.00)) > 0) {
            proj = (BigDecimal.valueOf(1000));
        }

        newRequest.setFinalAmt(proj);

        Employee emp = er.getByEmpId(newRequest.getReqBy());

        if(newRequest.getFinalAmt().compareTo(emp.getEligible()) > 0){
            newRequest.setFinalAmt(emp.getEligible());
        }
        BigDecimal prevEligible = emp.getEligible();
        BigDecimal newEligible = prevEligible.subtract(newRequest.getFinalAmt());
        BigDecimal prevPending = emp.getPending();
        BigDecimal newPending = prevPending.add(newRequest.getFinalAmt());

        er.setEligible(newRequest.getReqBy(), newEligible);
        er.setPending(newRequest.getReqBy(), newPending);

        if (newRequest.isSupPreApp() && newRequest.getSpaAttString() != null) {

            newRequest.setSupAppBy(es.getSupId(newRequest.getReqBy()));
            newRequest.setSupApp(true);

            Integer attId = as.createAttachment(newRequest.getReqBy(), "sup pre approval", newRequest.getSpaAttString());
            newRequest.setSpaAtt(attId);

        }
        if (newRequest.isDhPreApp() && newRequest.getDhpaAttString() != null) {

            newRequest.setSupAppBy(es.getSupId(newRequest.getReqBy()));
            newRequest.setSupApp(true);
            newRequest.setDhAppBy(es.getDeptHeadId(newRequest.getReqBy()));

            Integer attId = as.createAttachment(newRequest.getReqBy(), "dh pre approval", newRequest.getDhpaAttString());
            newRequest.setDhpaAtt(attId);

        }
        if (newRequest.getSupAppBy() == null) {

            newRequest.setSupAppBy(es.getSupId(newRequest.getReqBy()));
        }
        if (newRequest.getDhAppBy() == null) {

            newRequest.setDhAppBy(es.getDeptHeadId(newRequest.getReqBy()));
        }
        if (newRequest.getEventAttString() != null) {

            newRequest.setEventAtt(true);
            Integer attId = as.createAttachment(newRequest.getReqBy(), "event information", newRequest.getEventAttString());
            newRequest.setEventAttId(attId);

        }

        //System.out.println("request after processing in ReqService createRequest" + newRequest);

        rr.addReq(newRequest);

        Integer supId = es.getSupId(newRequest.getReqBy());
        Integer dhId = es.getDeptHeadId(newRequest.getReqBy());

        System.out.println("Simulating email sent to Employee's direct supervisor notifying them to check pending requests. Supervisor's Employee ID: " + supId);
        System.out.println("Simulating email sent to Employee's department head notifying them to check pending requests. Dept Head's Employee ID: " + dhId);
    }

    public ArrayList<Request> getMyRequests(Integer empId) {


        ArrayList<Request> requests =  rr.getByReqBy(empId);
        System.out.println("ReqService getMyRequests() method called for empId: " + empId);
        requests.forEach((request -> System.out.println("Request processed: " + request)));
        for(int i = 0; i < requests.size(); i++){

            Request r = requests.get(i);

            //System.out.println("ReqService getMyRequests() for loop processing request: " + r);
            if(r.getSupInfAtt() != null && r.getSupInfAtt() != 0 && r.getSupInfAtt() != 1) {
                //System.out.println("went into supinf if block");
                Attachment a = ar.getById(r.getSupInfAtt());
                r.setSupInfString(a.getFile());
            }
            if(r.getDhInfAtt() != null && r.getDhInfAtt() != 0 && r.getDhInfAtt() != 1){
                //System.out.println("value of dhInfAtt: " + r.getDhInfAtt());
                //System.out.println("went into dhinf if block");
                Attachment a = ar.getById(r.getDhInfAtt());
                r.setDhInfString(a.getFile());
            }
            if(r.getBencoInfAtt() != null && r.getBencoInfAtt() != 0 && r.getBencoInfAtt() != 1){
                //System.out.println("went into bencoinf if block");
                Attachment a = ar.getById(r.getBencoInfAtt());
                r.setDhInfString(a.getFile());
            }
        }
        //requests.forEach((request -> System.out.println("Request processed:" + request)));
        return requests;
    }

    public ArrayList<Request> getEmployeeRequests(Integer empId) {

        System.out.println("Employee ID received inside ReqService getEmployeeRequests(): " + empId);

        Employee manager = er.getByEmpId(empId);
        System.out.println("Employee pulled by RequestService getEmployeeRequests():" + manager);

        if (manager.isSupervisor() && !manager.isDeptHead()) {

            ArrayList<Request> requests = rr.getSupRequests(empId);

            for(Request r : requests){

                if(r.getSupInfAtt() != null && r.getSupInfAtt() != 0 && r.getSupInfAtt() != 1) {
                    //System.out.println("went into supinf if block");
                    Attachment a = ar.getById(r.getSupInfAtt());
                    r.setSupInfString(a.getFile());
                }
                if(r.getDhInfAtt() != null && r.getDhInfAtt() != 0 && r.getDhInfAtt() != 1){
                    //System.out.println("value of dhInfAtt: " + r.getDhInfAtt());
                    //System.out.println("went into dhinf if block");
                    Attachment a = ar.getById(r.getDhInfAtt());
                    r.setDhInfString(a.getFile());
                }
                if(r.getBencoInfAtt() != null && r.getBencoInfAtt() != 0 && r.getBencoInfAtt() != 1){
                    //System.out.println("went into bencoinf if block");
                    Attachment a = ar.getById(r.getBencoInfAtt());
                    r.setDhInfString(a.getFile());
                }
            }

            return requests;
        }

        if (manager.isDeptHead()) {

            System.out.println("Employee recognized as DeptHead in ReqService getEmployeeRequests() ");
            ArrayList<Request> requests = rr.getDhRequests(empId);

            return requests;

        }
        return null;
    }

    public ArrayList<Request> getBencoRequests() {

        ArrayList<Request> requests = rr.getAllPendingRequests();

        for(Request r : requests){
            if(r.isGradeSubmitted()){
                System.out.println("ReqService getBencoRequests getting ");
                Attachment grade = ar.getById(r.getGradeAtt());
                System.out.println("ReqService getBencoRequests getting ");
                for(Request req: requests){

                    req.setGradeString(grade.getFile());

                }
            }
        }
        for(Request r : requests) {
            if (r.getSupInfAtt() != null && r.getSupInfAtt() != 0 && r.getSupInfAtt() != 1) {
                //System.out.println("went into supinf if block");
                Attachment a = ar.getById(r.getSupInfAtt());
                r.setSupInfString(a.getFile());
            }
            if (r.getDhInfAtt() != null && r.getDhInfAtt() != 0 && r.getDhInfAtt() != 1) {
                //System.out.println("value of dhInfAtt: " + r.getDhInfAtt());
                //System.out.println("went into dhinf if block");
                Attachment a = ar.getById(r.getDhInfAtt());
                r.setDhInfString(a.getFile());
            }
            if (r.getBencoInfAtt() != null && r.getBencoInfAtt() != 0 && r.getBencoInfAtt() != 1) {
                //System.out.println("went into bencoinf if block");
                Attachment a = ar.getById(r.getBencoInfAtt());
                r.setDhInfString(a.getFile());
            }
        }
        return requests;
    }

    public void attachSupInfo(Request r) {
        System.out.println("ReqService attachSupInfo method called");
        System.out.println("Request received in ReqService attachSupInfo method: \n" + r);
        if (r.getSupInfString() != null && r.getSupInfString() != "") {

            System.out.println("Request received in ReqService attachSupInfo method \n" + r);
            Integer attId = as.createAttachment(r.getReqBy(), "addl sup info", r.getSupInfString());
            r.setSupInfAtt(attId);

            rr.addSupInfo(r);

            Integer supId = es.getSupId(r.getReqBy());
            System.out.println("Simulating email sent to Employees supervisor notifying them that the requested info has been attached. Supervisor's Employee ID: \n" + supId +
                                "Additional info: " + r.getSupInfString());
        }
    }

    public void attachDhInfo(Request r) {
        System.out.println("ReqService attachDhInfo() method called");
        if (r.getDhInfString() != null && r.getDhInfString() != "") {
            //System.out.println("Request received in ReqService attachDhInfo() method if block \n" + r);
            Integer attId = as.createAttachment(r.getReqBy(), "addl dh info", r.getDhInfString());
            r.setDhInfAtt(attId);

            rr.addDhInfo(r);

            Integer dhId = es.getDeptHeadId(r.getReqBy());
            System.out.println("Simulating email sent to employee's department head. Dept Head's employee id: \n" + dhId +
                    "Additional info: " + r.getDhInfString());

        }
    }

    public void attachBencoInfo(Request r) {

        if (r.getBencoInfAttString() != null && r.getBencoInfAttString() != "") {

            Integer attId = as.createAttachment(r.getReqBy(), "addl benco info", r.getBencoInfAttString());
            r.setBencoInfAtt(attId);

            rr.addBencoInfo(r);

            Integer bencoId = er.getBencoId();
            System.out.println("Simulating email to the BenCo notifying them of update. Benco's ID: \n" + bencoId +
                    "Additional info: " +r.getBencoInfAttString());
        }
    }

    public void attachGradeInfo(Request r) {

        if (r.getGradeString() != null && r.getGradeString() != "") {

            Integer attId = as.createAttachment(r.getReqBy(), "grade or presentation", r.getGradeString());

            if(r.isPresGrade()){
                r.setPresSubmitted(true);
                r.setGradeAtt(attId);
                rr.addPresInfo(r);
            } else {
                r.setGradeAtt(attId);
                r.setGradeSubmitted(true);
                rr.addGradeInfo(r);
            }
            Integer bencoId = er.getBencoId();
            System.out.println("Simulating email sent to the BenCo notifying them of the grade submittal. Benco's ID: " + bencoId);
        }
    }

    public void empRejectStatus(Request r) {

        if (r.isDenied()) {

            rr.updateEmpReject(r);

        } else {

            rr.updateEmpApprove(r);

        }
    }

    public void checkGrade(Request r){

        if(r.isReimburse()){
            rr.updateReimburse(r);
        }
        if(r.isDenied()){
            rr.updateDenied(r);
        }
    }

    public void manDecision(Request r){

        if(r.isDenied()){

            Employee emp = er.getByEmpId(r.getReqBy());
            BigDecimal prevEligible = emp.getEligible();
            BigDecimal newEligible = prevEligible.add(r.getFinalAmt());

            er.setEligible(emp.getEmpId(), newEligible);

            rr.updateDenied(r);

        }

        rr.manDecision(r);
    }

    public void bencoDecision(Request r){

        if(r.isDenied()){

            Employee emp = er.getByEmpId(r.getReqBy());
            BigDecimal prevEligible = emp.getEligible();
            BigDecimal newEligible = prevEligible.add(r.getFinalAmt());

            BigDecimal prevPending = emp.getPending();
            BigDecimal newPending = prevPending.subtract(r.getFinalAmt());

            er.setEligible(emp.getEmpId(), newEligible);
            er.setPending(emp.getEmpId(), newPending);

            rr.updateDenied(r);

        }

        if(r.isBencoApp() && (r.isPresPassed() || r.isGradePassed())){
            r.setReimburse(true);
            rr.updateReimburse(r);
        }

        rr.bencoDecision(r);

    }
}

