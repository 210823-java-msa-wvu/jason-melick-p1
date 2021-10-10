package dev.melick.repositories;

import dev.melick.models.Attachment;
import dev.melick.models.Request;

import static dev.melick.utils.ConnectionUtil.getConnectionUtil;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReqRepo {

    public void addReq(Request r) {

        System.out.println("new request received in ReqRepo addReq" + r);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (Connection connection = getConnectionUtil().getConnection()) {

            String sql = "INSERT INTO requests (req_by, starts_on, start_time, time_missed, urgent, institution, inst_city, inst_state, event_type, event_desc, event_cost, event_att, event_att_id, justification, sup_pre_app, spa_att, dh_pre_app, dhpa_att, proj_amt, grade_format, min_grade, sup_app, dh_app, final_amt, sup_app_by, dh_app_by) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, r.getReqBy());
            ps.setDate(2, Date.valueOf(dateFormat.format(r.getStartsOn())));
            ps.setString(3, r.getStartTime() + ":00");
            ps.setInt(4, r.getTimeMissed());
            ps.setBoolean(5, r.isUrgent());
            ps.setString(6, r.getInstitution());
            ps.setString(7, r.getInstCity());
            ps.setString(8, r.getInstState());
            ps.setString(9, r.getEventType());
            ps.setString(10, r.getEventDesc());
            ps.setBigDecimal(11, r.getEventCost());
            //System.out.println("Program can't set BigDecimal in prepare statement");
            ps.setBoolean(12, r.isEventAtt());
            if (r.isEventAtt()) {
                ps.setInt(13, r.getEventAttId());
            } else {
                ps.setInt(13, 1);
            }
            ps.setString(14, r.getJustification());
            ps.setBoolean(15, r.isSupPreApp());
            if (r.isSupPreApp()) {
                ps.setInt(16, r.getSpaAtt());
            } else {
                ps.setInt(16, 1);
            }
            ps.setBoolean(17, r.isDhPreApp());
            if (r.isDhPreApp()) {
                ps.setInt(18, r.getDhpaAtt());
            } else {
                ps.setInt(18, 1);
            }
            ps.setBigDecimal(19, r.getProjAmt());
            ps.setString(20, r.getGradeFormat());
            ps.setString(21, r.getMinPass());
            ps.setBoolean(22, r.isSupApp());
            ps.setBoolean(23, r.isDhApp());
            ps.setBigDecimal(24, r.getFinalAmt());
            //System.out.println("ReqRepo addRequest() okay to here.");
            ps.setInt(25, r.getSupAppBy());
            ps.setInt(26, r.getDhAppBy());
            System.out.println("prepare statement completed in ReqRepo addReq");
            System.out.println(ps);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Request> getByReqBy(Integer empId) {

        //System.out.println("getByInteger passed args: " + colName + "," + intInput);
        try (Connection connection = getConnectionUtil().getConnection()) {

            //String sql = "SELECT * FROM requests WHERE ? = ?";
            String sql = "SELECT * FROM requests WHERE req_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            //ps.setString(1, colName);
            ps.setInt(1, empId);
            //System.out.println("statement being sent: " + ps.toString());
            ResultSet rs = ps.executeQuery();

            ArrayList<Request> requests = new ArrayList<>();

            while (rs.next()) {

                Request currReq = new Request();

                currReq.setReqId(rs.getInt("req_id"));
                currReq.setReqBy(rs.getInt("req_by"));
                currReq.setReqDate(rs.getDate("req_date"));
                currReq.setStartsOn(rs.getDate("starts_on"));
                //System.out.println("getMyRequests working to here");
                currReq.setStartTime(String.valueOf(rs.getTime("start_time")));
                currReq.setUrgent(rs.getBoolean("urgent"));
                currReq.setInstitution(rs.getString("institution"));
                currReq.setInstCity(rs.getString("inst_city"));
                currReq.setInstState(rs.getString("inst_state"));
                currReq.setEventType(rs.getString("event_type"));
                currReq.setEventDesc(rs.getString("event_desc"));
                currReq.setEventCost(rs.getBigDecimal("event_cost"));
                currReq.setEventAtt(rs.getBoolean("event_att"));
                currReq.setEventAttId(rs.getInt("event_att_id"));
                currReq.setJustification(rs.getString("justification"));
                currReq.setSupPreApp(rs.getBoolean("sup_pre_app"));
                currReq.setSpaAtt(rs.getInt("spa_att"));
                currReq.setDhPreApp(rs.getBoolean("dh_pre_app"));
                currReq.setDhpaAtt(rs.getInt("dhpa_att"));
                currReq.setProjAmt(rs.getBigDecimal("proj_amt"));
                currReq.setGradeFormat(rs.getString("grade_format"));
                currReq.setPassFail(rs.getBoolean("pass_fail"));
                currReq.setPctGrade(rs.getBoolean("pass_pct"));
                currReq.setPctMin(rs.getInt("pp_min"));
                currReq.setLtrGrade(rs.getBoolean("pass_ltr"));
                currReq.setLtrMin(rs.getString("pl_min"));
                currReq.setTimeMissed(rs.getInt("time_missed"));
                currReq.setSupApp(rs.getBoolean("sup_app"));
                currReq.setSupInfReq(rs.getBoolean("sup_app_inf_req"));
                currReq.setSupInfAtt(rs.getInt("sup_app_inf_att"));
                currReq.setSupAppBy(rs.getInt("sup_app_by"));
                currReq.setDhApp(rs.getBoolean("dh_app"));
                currReq.setDhInfReq(rs.getBoolean("dh_app_inf_req"));
                currReq.setDhInfAtt(rs.getInt("dh_app_inf_att"));
                currReq.setDhAppBy(rs.getInt("dh_app_by"));
                currReq.setBencoApp(rs.getBoolean("benco_app"));
                currReq.setBencoInfReq(rs.getBoolean("benco_app_inf_req"));
                currReq.setBencoInfAtt(rs.getInt("benco_app_inf_att"));
                currReq.setBencoDowngrade(rs.getBoolean("benco_downgrade"));
                currReq.setBencoUpgrade(rs.getBoolean("benco_upgrade"));
                currReq.setEmpReject(rs.getBoolean("emp_reject"));
                currReq.setFinalAmt(rs.getBigDecimal("final_amt"));
                currReq.setGradePassed(rs.getBoolean("grade_passed"));
                currReq.setGradeBy(rs.getInt("grade_by"));
                currReq.setPresPassed(rs.getBoolean("pres_passed"));
                currReq.setPresBy(rs.getInt("pres_by"));
                currReq.setReimburse(rs.getBoolean("reimburse"));
                currReq.setDenied(rs.getBoolean("denied"));
                currReq.setDenBy(rs.getInt("den_by"));
                currReq.setDenOn(rs.getDate("den_on"));
                currReq.setDenReason(rs.getString("den_reason"));
                currReq.setGradeAtt(rs.getInt("grade_att_id"));
                currReq.setGradeSubmitted(rs.getBoolean("grade_submitted"));
                currReq.setPresGrade(rs.getBoolean("presgrade"));
                currReq.setWhyBencoUpgrade(rs.getString("why_benco_upgrade"));
                currReq.setMinPass(rs.getString("min_grade"));

                requests.add(currReq);

            }
            return requests;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Request> getSupRequests(Integer supId) {

        //System.out.println("getByInteger passed args: " + colName + "," + intInput);
        try (Connection connection = getConnectionUtil().getConnection()) {

            System.out.println("Employee ID received inside ReqService getEmployeeRequests(): " + supId);
            //String sql = "SELECT * FROM requests WHERE ? = ?";
            String sql = "SELECT * FROM requests WHERE sup_app_by = ? AND NOT sup_app AND NOT denied";
            PreparedStatement ps = connection.prepareStatement(sql);
            //ps.setString(1, colName);
            ps.setInt(1, supId);
            //System.out.println("statement being sent: " + ps.toString());
            ResultSet rs = ps.executeQuery();

            ArrayList<Request> requests = new ArrayList<>();

            while (rs.next()) {

                Request currReq = new Request();

                currReq.setReqId(rs.getInt("req_id"));
                currReq.setReqBy(rs.getInt("req_by"));
                currReq.setReqDate(rs.getDate("req_date"));
                currReq.setStartsOn(rs.getDate("starts_on"));
                //System.out.println("getMyRequests working to here");
                currReq.setStartTime(String.valueOf(rs.getTime("start_time")));
                currReq.setUrgent(rs.getBoolean("urgent"));
                currReq.setInstitution(rs.getString("institution"));
                currReq.setInstCity(rs.getString("inst_city"));
                currReq.setInstState(rs.getString("inst_state"));
                currReq.setEventType(rs.getString("event_type"));
                currReq.setEventDesc(rs.getString("event_desc"));
                currReq.setEventCost(rs.getBigDecimal("event_cost"));
                currReq.setEventAtt(rs.getBoolean("event_att"));
                currReq.setEventAttId(rs.getInt("event_att_id"));
                currReq.setJustification(rs.getString("justification"));
                currReq.setSupPreApp(rs.getBoolean("sup_pre_app"));
                currReq.setSpaAtt(rs.getInt("spa_att"));
                currReq.setDhPreApp(rs.getBoolean("dh_pre_app"));
                currReq.setDhpaAtt(rs.getInt("dhpa_att"));
                currReq.setProjAmt(rs.getBigDecimal("proj_amt"));
                currReq.setGradeFormat(rs.getString("grade_format"));
                currReq.setPassFail(rs.getBoolean("pass_fail"));
                currReq.setPctGrade(rs.getBoolean("pass_pct"));
                currReq.setPctMin(rs.getInt("pp_min"));
                currReq.setLtrGrade(rs.getBoolean("pass_ltr"));
                currReq.setLtrMin(rs.getString("pl_min"));
                currReq.setTimeMissed(rs.getInt("time_missed"));
                currReq.setSupApp(rs.getBoolean("sup_app"));
                currReq.setSupInfReq(rs.getBoolean("sup_app_inf_req"));
                currReq.setSupInfAtt(rs.getInt("sup_app_inf_att"));
                currReq.setSupAppBy(rs.getInt("sup_app_by"));
                currReq.setDhApp(rs.getBoolean("dh_app"));
                currReq.setDhInfReq(rs.getBoolean("dh_app_inf_req"));
                currReq.setDhInfAtt(rs.getInt("dh_app_inf_att"));
                currReq.setDhAppBy(rs.getInt("dh_app_by"));
                currReq.setBencoApp(rs.getBoolean("benco_app"));
                currReq.setBencoInfReq(rs.getBoolean("benco_app_inf_req"));
                currReq.setBencoInfAtt(rs.getInt("benco_app_inf_att"));
                currReq.setBencoDowngrade(rs.getBoolean("benco_downgrade"));
                currReq.setBencoUpgrade(rs.getBoolean("benco_upgrade"));
                currReq.setEmpReject(rs.getBoolean("emp_reject"));
                currReq.setFinalAmt(rs.getBigDecimal("final_amt"));
                currReq.setGradePassed(rs.getBoolean("grade_passed"));
                currReq.setGradeBy(rs.getInt("grade_by"));
                currReq.setPresPassed(rs.getBoolean("pres_passed"));
                currReq.setPresBy(rs.getInt("pres_by"));
                currReq.setReimburse(rs.getBoolean("reimburse"));
                currReq.setDenied(rs.getBoolean("denied"));
                currReq.setDenBy(rs.getInt("den_by"));
                currReq.setDenOn(rs.getDate("den_on"));
                currReq.setDenReason(rs.getString("den_reason"));
                currReq.setGradeAtt(rs.getInt("grade_att_id"));
                currReq.setGradeSubmitted(rs.getBoolean("grade_submitted"));
                currReq.setPresGrade(rs.getBoolean("presgrade"));
                currReq.setWhyBencoUpgrade(rs.getString("why_benco_upgrade"));

                requests.add(currReq);

            }
            return requests;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Request> getDhRequests(Integer dhId) {

        //System.out.println("getByInteger passed args: " + colName + "," + intInput);
        try (Connection connection = getConnectionUtil().getConnection()) {
            System.out.println("ReqRepo getDhRequests() called. dhId = " + dhId);
            //String sql = "SELECT * FROM requests WHERE ? = ?";
            String sql = "SELECT * FROM requests WHERE dh_app_by = ? AND NOT dh_app AND NOT denied";
            PreparedStatement ps = connection.prepareStatement(sql);
            //ps.setString(1, colName);
            ps.setInt(1, dhId);
            //System.out.println("statement being sent: " + ps.toString());
            ResultSet rs = ps.executeQuery();

            ArrayList<Request> requests = new ArrayList<>();

            while (rs.next()) {

                Request currReq = new Request();

                currReq.setReqId(rs.getInt("req_id"));
                currReq.setReqBy(rs.getInt("req_by"));
                currReq.setReqDate(rs.getDate("req_date"));
                currReq.setStartsOn(rs.getDate("starts_on"));
                //System.out.println("getMyRequests working to here");
                currReq.setStartTime(String.valueOf(rs.getTime("start_time")));
                currReq.setUrgent(rs.getBoolean("urgent"));
                currReq.setInstitution(rs.getString("institution"));
                currReq.setInstCity(rs.getString("inst_city"));
                currReq.setInstState(rs.getString("inst_state"));
                currReq.setEventType(rs.getString("event_type"));
                currReq.setEventDesc(rs.getString("event_desc"));
                currReq.setEventCost(rs.getBigDecimal("event_cost"));
                currReq.setEventAtt(rs.getBoolean("event_att"));
                currReq.setEventAttId(rs.getInt("event_att_id"));
                currReq.setJustification(rs.getString("justification"));
                currReq.setSupPreApp(rs.getBoolean("sup_pre_app"));
                currReq.setSpaAtt(rs.getInt("spa_att"));
                currReq.setDhPreApp(rs.getBoolean("dh_pre_app"));
                currReq.setDhpaAtt(rs.getInt("dhpa_att"));
                currReq.setProjAmt(rs.getBigDecimal("proj_amt"));
                currReq.setGradeFormat(rs.getString("grade_format"));
                currReq.setPassFail(rs.getBoolean("pass_fail"));
                currReq.setPctGrade(rs.getBoolean("pass_pct"));
                currReq.setPctMin(rs.getInt("pp_min"));
                currReq.setLtrGrade(rs.getBoolean("pass_ltr"));
                currReq.setLtrMin(rs.getString("pl_min"));
                currReq.setTimeMissed(rs.getInt("time_missed"));
                currReq.setSupApp(rs.getBoolean("sup_app"));
                currReq.setSupInfReq(rs.getBoolean("sup_app_inf_req"));
                currReq.setSupInfAtt(rs.getInt("sup_app_inf_att"));
                currReq.setSupAppBy(rs.getInt("sup_app_by"));
                currReq.setDhApp(rs.getBoolean("dh_app"));
                currReq.setDhInfReq(rs.getBoolean("dh_app_inf_req"));
                currReq.setDhInfAtt(rs.getInt("dh_app_inf_att"));
                currReq.setDhAppBy(rs.getInt("dh_app_by"));
                currReq.setBencoApp(rs.getBoolean("benco_app"));
                currReq.setBencoInfReq(rs.getBoolean("benco_app_inf_req"));
                currReq.setBencoInfAtt(rs.getInt("benco_app_inf_att"));
                currReq.setBencoDowngrade(rs.getBoolean("benco_downgrade"));
                currReq.setBencoUpgrade(rs.getBoolean("benco_upgrade"));
                currReq.setEmpReject(rs.getBoolean("emp_reject"));
                currReq.setFinalAmt(rs.getBigDecimal("final_amt"));
                currReq.setGradePassed(rs.getBoolean("grade_passed"));
                currReq.setGradeBy(rs.getInt("grade_by"));
                currReq.setPresPassed(rs.getBoolean("pres_passed"));
                currReq.setPresBy(rs.getInt("pres_by"));
                currReq.setReimburse(rs.getBoolean("reimburse"));
                currReq.setDenied(rs.getBoolean("denied"));
                currReq.setDenBy(rs.getInt("den_by"));
                currReq.setDenOn(rs.getDate("den_on"));
                currReq.setDenReason(rs.getString("den_reason"));
                currReq.setGradeAtt(rs.getInt("grade_att_id"));
                currReq.setGradeSubmitted(rs.getBoolean("grade_submitted"));
                currReq.setPresGrade(rs.getBoolean("presgrade"));
                currReq.setWhyBencoUpgrade(rs.getString("why_benco_upgrade"));
                System.out.println("ReqRepo getEmployeeRequets() Prepared Statement: " + ps);
                requests.add(currReq);

            }
            //requests.forEach(request -> System.out.println(request));
            return requests;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Request> getAllPendingRequests(){

        try (Connection connection = getConnectionUtil().getConnection()) {
            System.out.println("ReqRepo getAllPendingRequests() called.");
            //String sql = "SELECT * FROM requests WHERE ? = ?";
            String sql = "SELECT * FROM requests WHERE NOT(sup_app AND dh_app AND benco_app AND reimburse) AND NOT(denied)";
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            ArrayList<Request> requests = new ArrayList<>();

            while (rs.next()) {

                Request currReq = new Request();

                currReq.setReqId(rs.getInt("req_id"));
                currReq.setReqBy(rs.getInt("req_by"));
                currReq.setReqDate(rs.getDate("req_date"));
                currReq.setStartsOn(rs.getDate("starts_on"));
                //System.out.println("getMyRequests working to here");
                currReq.setStartTime(String.valueOf(rs.getTime("start_time")));
                currReq.setUrgent(rs.getBoolean("urgent"));
                currReq.setInstitution(rs.getString("institution"));
                currReq.setInstCity(rs.getString("inst_city"));
                currReq.setInstState(rs.getString("inst_state"));
                currReq.setEventType(rs.getString("event_type"));
                currReq.setEventDesc(rs.getString("event_desc"));
                currReq.setEventCost(rs.getBigDecimal("event_cost"));
                currReq.setEventAtt(rs.getBoolean("event_att"));
                currReq.setEventAttId(rs.getInt("event_att_id"));
                currReq.setJustification(rs.getString("justification"));
                currReq.setSupPreApp(rs.getBoolean("sup_pre_app"));
                currReq.setSpaAtt(rs.getInt("spa_att"));
                currReq.setDhPreApp(rs.getBoolean("dh_pre_app"));
                currReq.setDhpaAtt(rs.getInt("dhpa_att"));
                currReq.setProjAmt(rs.getBigDecimal("proj_amt"));
                currReq.setGradeFormat(rs.getString("grade_format"));
                currReq.setPassFail(rs.getBoolean("pass_fail"));
                currReq.setPctGrade(rs.getBoolean("pass_pct"));
                currReq.setPctMin(rs.getInt("pp_min"));
                currReq.setLtrGrade(rs.getBoolean("pass_ltr"));
                currReq.setLtrMin(rs.getString("pl_min"));
                currReq.setTimeMissed(rs.getInt("time_missed"));
                currReq.setSupApp(rs.getBoolean("sup_app"));
                currReq.setSupInfReq(rs.getBoolean("sup_app_inf_req"));
                currReq.setSupInfAtt(rs.getInt("sup_app_inf_att"));
                currReq.setSupAppBy(rs.getInt("sup_app_by"));
                currReq.setDhApp(rs.getBoolean("dh_app"));
                currReq.setDhInfReq(rs.getBoolean("dh_app_inf_req"));
                currReq.setDhInfAtt(rs.getInt("dh_app_inf_att"));
                currReq.setDhAppBy(rs.getInt("dh_app_by"));
                currReq.setBencoApp(rs.getBoolean("benco_app"));
                currReq.setBencoInfReq(rs.getBoolean("benco_app_inf_req"));
                currReq.setBencoInfAtt(rs.getInt("benco_app_inf_att"));
                currReq.setBencoDowngrade(rs.getBoolean("benco_downgrade"));
                currReq.setBencoUpgrade(rs.getBoolean("benco_upgrade"));
                currReq.setEmpReject(rs.getBoolean("emp_reject"));
                currReq.setFinalAmt(rs.getBigDecimal("final_amt"));
                currReq.setGradePassed(rs.getBoolean("grade_passed"));
                currReq.setGradeBy(rs.getInt("grade_by"));
                currReq.setPresPassed(rs.getBoolean("pres_passed"));
                currReq.setPresBy(rs.getInt("pres_by"));
                currReq.setReimburse(rs.getBoolean("reimburse"));
                currReq.setDenied(rs.getBoolean("denied"));
                currReq.setDenBy(rs.getInt("den_by"));
                currReq.setDenOn(rs.getDate("den_on"));
                currReq.setDenReason(rs.getString("den_reason"));
                currReq.setGradeAtt(rs.getInt("grade_att_id"));
                currReq.setGradeSubmitted(rs.getBoolean("grade_submitted"));
                currReq.setPresGrade(rs.getBoolean("presgrade"));
                currReq.setWhyBencoUpgrade(rs.getString("why_benco_upgrade"));
                currReq.setMinPass(rs.getString("min_grade"));
                //System.out.println("ReqRepo getEmployeeRequets() Prepared Statement: " + ps);
                requests.add(currReq);

            }
            //requests.forEach(request -> System.out.println(request));
            return requests;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public ArrayList<Request> getReimbursements(){

        try (Connection connection = getConnectionUtil().getConnection()) {
            System.out.println("ReqRepo getAllPendingRequests() called.");
            //String sql = "SELECT * FROM requests WHERE ? = ?";
            String sql = "SELECT * FROM requests WHERE reimburse";
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            ArrayList<Request> requests = new ArrayList<>();

            while (rs.next()) {

                Request currReq = new Request();

                currReq.setReqId(rs.getInt("req_id"));
                currReq.setReqBy(rs.getInt("req_by"));
                currReq.setReqDate(rs.getDate("req_date"));
                currReq.setStartsOn(rs.getDate("starts_on"));
                //System.out.println("getMyRequests working to here");
                currReq.setStartTime(String.valueOf(rs.getTime("start_time")));
                currReq.setUrgent(rs.getBoolean("urgent"));
                currReq.setInstitution(rs.getString("institution"));
                currReq.setInstCity(rs.getString("inst_city"));
                currReq.setInstState(rs.getString("inst_state"));
                currReq.setEventType(rs.getString("event_type"));
                currReq.setEventDesc(rs.getString("event_desc"));
                currReq.setEventCost(rs.getBigDecimal("event_cost"));
                currReq.setEventAtt(rs.getBoolean("event_att"));
                currReq.setEventAttId(rs.getInt("event_att_id"));
                currReq.setJustification(rs.getString("justification"));
                currReq.setSupPreApp(rs.getBoolean("sup_pre_app"));
                currReq.setSpaAtt(rs.getInt("spa_att"));
                currReq.setDhPreApp(rs.getBoolean("dh_pre_app"));
                currReq.setDhpaAtt(rs.getInt("dhpa_att"));
                currReq.setProjAmt(rs.getBigDecimal("proj_amt"));
                currReq.setGradeFormat(rs.getString("grade_format"));
                currReq.setPassFail(rs.getBoolean("pass_fail"));
                currReq.setPctGrade(rs.getBoolean("pass_pct"));
                currReq.setPctMin(rs.getInt("pp_min"));
                currReq.setLtrGrade(rs.getBoolean("pass_ltr"));
                currReq.setLtrMin(rs.getString("pl_min"));
                currReq.setTimeMissed(rs.getInt("time_missed"));
                currReq.setSupApp(rs.getBoolean("sup_app"));
                currReq.setSupInfReq(rs.getBoolean("sup_app_inf_req"));
                currReq.setSupInfAtt(rs.getInt("sup_app_inf_att"));
                currReq.setSupAppBy(rs.getInt("sup_app_by"));
                currReq.setDhApp(rs.getBoolean("dh_app"));
                currReq.setDhInfReq(rs.getBoolean("dh_app_inf_req"));
                currReq.setDhInfAtt(rs.getInt("dh_app_inf_att"));
                currReq.setDhAppBy(rs.getInt("dh_app_by"));
                currReq.setBencoApp(rs.getBoolean("benco_app"));
                currReq.setBencoInfReq(rs.getBoolean("benco_app_inf_req"));
                currReq.setBencoInfAtt(rs.getInt("benco_app_inf_att"));
                currReq.setBencoDowngrade(rs.getBoolean("benco_downgrade"));
                currReq.setBencoUpgrade(rs.getBoolean("benco_upgrade"));
                currReq.setEmpReject(rs.getBoolean("emp_reject"));
                currReq.setFinalAmt(rs.getBigDecimal("final_amt"));
                currReq.setGradePassed(rs.getBoolean("grade_passed"));
                currReq.setGradeBy(rs.getInt("grade_by"));
                currReq.setPresPassed(rs.getBoolean("pres_passed"));
                currReq.setPresBy(rs.getInt("pres_by"));
                currReq.setReimburse(rs.getBoolean("reimburse"));
                currReq.setDenied(rs.getBoolean("denied"));
                currReq.setDenBy(rs.getInt("den_by"));
                currReq.setDenOn(rs.getDate("den_on"));
                currReq.setDenReason(rs.getString("den_reason"));
                currReq.setGradeAtt(rs.getInt("grade_att_id"));
                currReq.setGradeSubmitted(rs.getBoolean("grade_submitted"));
                currReq.setPresGrade(rs.getBoolean("presgrade"));
                currReq.setWhyBencoUpgrade(rs.getString("why_benco_upgrade"));
                currReq.setMinPass(rs.getString("min_grade"));
                //System.out.println("ReqRepo getEmployeeRequets() Prepared Statement: " + ps);
                requests.add(currReq);

            }
            //requests.forEach(request -> System.out.println(request));
            return requests;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addSupInfo(Request r) {

        try (Connection connection = getConnectionUtil().getConnection()) {

            String sql = "UPDATE requests set sup_app_inf_att = ? WHERE req_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, r.getSupInfAtt());
            ps.setInt(2, r.getReqId());

            System.out.println("prepare statement completed in ReqRepo addSupInfo");
            System.out.println(ps);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addDhInfo(Request r) {

        try (Connection connection = getConnectionUtil().getConnection()) {

            String sql = "UPDATE requests set dh_app_inf_att = ? WHERE req_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, r.getDhInfAtt());
            ps.setInt(2, r.getReqId());

            System.out.println("prepare statement completed in ReqRepo addDhInfo");
            System.out.println(ps);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBencoInfo(Request r) {

        try (Connection connection = getConnectionUtil().getConnection()) {

            String sql = "UPDATE requests set benco_app_inf_att = ? WHERE req_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, r.getDhInfAtt());
            ps.setInt(2, r.getReqId());

            System.out.println("prepare statement completed in ReqRepo addBencoInfo");
            System.out.println(ps);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addGradeInfo(Request r) {

        try (Connection connection = getConnectionUtil().getConnection()) {

            String sql = "UPDATE requests set grade_att_id = ?, grade_submitted = ? WHERE req_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, r.getGradeAtt());
            ps.setBoolean(2, r.isGradeSubmitted());
            ps.setInt(3, r.getReqId());

            System.out.println("prepare statement completed in ReqRepo addGradeInfo");
            System.out.println(ps);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPresInfo(Request r) {

        try (Connection connection = getConnectionUtil().getConnection()) {

            String sql = "UPDATE requests set grade_att_id = ?, grade_submitted = ? WHERE req_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, r.getGradeAtt());
            ps.setBoolean(2, r.isGradeSubmitted());
            ps.setInt(3, r.getReqId());

            System.out.println("prepare statement completed in ReqRepo addGradeInfo");
            System.out.println(ps);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmpReject(Request r) {

        try (Connection connection = getConnectionUtil().getConnection()) {

            String sql = "UPDATE requests SET denied = ?, den_by = ?, den_reason = ? WHERE req_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setBoolean(1, r.isDenied());
            ps.setInt(2, r.getReqBy());
            ps.setString(3, r.getDenReason());
            ps.setInt(4, r.getReqId());

            System.out.println("prepare statement completed in ReqRepo addGradeInfo");
            System.out.println(ps);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmpApprove(Request r){

        try(Connection connection = getConnectionUtil().getConnection()){

            String sql = "UPDATE requests SET benco_app = ?, reimburse = ? WHERE req_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setBoolean(1, r.isBencoApp());
            ps.setBoolean(2, r.isReimburse());
            ps.setInt(3, r.getReqId());
            System.out.println("Prepared Statement from ReqRepo updateEmpApprove() method: ");
            System.out.println(ps);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateReimburse(Request r){

        try(Connection connection = getConnectionUtil().getConnection()){

            String sql = "UPDATE requests SET reimburse = ? WHERE req_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setBoolean(1, r.isReimburse());
            ps.setInt(2, r.getReqId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDenied(Request r){

        try(Connection connection = getConnectionUtil().getConnection()){

            String sql = "UPDATE requests SET denied = ? WHERE req_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setBoolean(1, r.isDenied());
            ps.setInt(2, r.getReqId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void manDecision(Request r){

        try(Connection connection = getConnectionUtil().getConnection()){

            String sql = "UPDATE requests SET sup_app = ?, dh_app = ?, denied = ?, sup_app_inf_req = ?, dh_app_inf_req = ?  WHERE req_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setBoolean(1, r.isSupApp());
            ps.setBoolean(2, r.isDhApp());
            ps.setBoolean(3, r.isDenied());
            ps.setBoolean(4,r.isSupInfReq());
            ps.setBoolean(5, r.isDhInfReq());
            ps.setInt(6, r.getReqId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void bencoDecision(Request r){

        try(Connection connection = getConnectionUtil().getConnection()){

            String sql = "UPDATE requests SET sup_app = ?, dh_app = ?, benco_app = ?, denied = ?, benco_app_inf_req = ?, benco_upgrade = ?, benco_downgrade = ?, why_benco_upgrade = ? WHERE req_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setBoolean(1, r.isSupApp());
            ps.setBoolean(2, r.isDhApp());
            ps.setBoolean(3, r.isBencoApp());
            ps.setBoolean(4, r.isDenied());
            ps.setBoolean(5,r.isBencoInfReq());

            ps.setBoolean(6, r.isBencoUpgrade());
            ps.setBoolean(7, r.isBencoDowngrade());
            ps.setString(8, r.getWhyBencoUpgrade());

            ps.setInt(9, r.getReqId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
