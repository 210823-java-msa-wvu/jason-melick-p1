package dev.melick.models;

import java.math.BigDecimal;
import java.util.Date;

public class Request {

    // Basic Information and Start Date
    private Integer reqId; // Request serial number primary key
    private Integer reqBy; // empId of the employee who generated the request
    private Date    reqDate; // Date the request was generated. Autopopulates in the sql table

    private Date startsOn; // Date of the training event
    private String startTime; // Time the event actually begins on the startsOn date

    private Integer timeMissed; // Projected number of work hours the employee will miss to attend the event

    private boolean urgent; // True if the startsOn date is less than 14 days from the reqDate

    // Event Details and Justification
    private String institution; // Name of the institution providing the training
    private String instCity; // City where institution is located
    private String instState; // State the institution is located in

    private String eventType; // Type of training provided. This will determine the multiplier applied to the event cost to determine the reimbursable amount
    private String eventDesc; // Basic description of the course or seminar
    private BigDecimal eventCost; // Amount the employee is spending for the training
    boolean eventAtt; // The employee can opt to include an attachment describing the training event
    private Integer eventAttId; // Id number of the employee. This will always be the same as the reqId
    private String justification; // Employee's justification for why this should be reimbursable. Short text description

    // Supervisor and Department Head Pre-Approvals
    private boolean supPreApp; // The employee's supervisor has pre-approved the reimbursement
    private Integer spaAtt; // attId of the attached pre-approval
    private String spaAttString;
    private boolean dhPreApp; // The employee's department head has pre-approved the reimbursement
    private Integer dhpaAtt; // attId of the proof of the dept head's pre-approval
    private String dhpaAttString;

    // Projected Reimbursement amount
    private BigDecimal projAmt; // Should be the eventCost times the multiplier for the type of event.
                        // Cannot exceed the employee's eligible amount remaining for the year, unless overridden by the BenCo

    // Grading Format and minimum passing requirements
    private String gradeFormat; // Pass/Fail, Percentage, Letter, or Presentation
    private boolean passFail;
    private final String pfMin = "pass";
    private boolean pctGrade;
    private Integer pctMin;
    private boolean ltrGrade;
    private String ltrMin;
    private boolean presGrade;
    private String minPass;

    private String eventAttString;

    // approvals prior to event (4 days for each step from request date to sup approval to dept head approval to benco approval
    private boolean supApp; // direct supervisor has approved. automatically flip to true if 4 days have passed since reqDate
    private boolean supInfReq; // direct supervisor has requested additional information.
    private Integer supInfAtt; // employee attachment providing the additional information
    private Integer supAppBy; // empId of the requestor's direct supervisor
    private String supInfString; // string simulating info attachment

    private boolean dhApp; // dept head has approved. automatically flip to true if 8 days have passed since reqDate. once this is true, submit to benco for final approval
    private boolean dhInfReq; // dept head has requested additional information.
    private Integer dhInfAtt; // employee attachment providing the additional information
    private Integer dhAppBy; // empId of the requestor's dept head
    private String dhInfString; // string simulating info attachment

    private boolean bencoApp; // benco has approved. automatically notify benco sup if more than 12 days have passed since reqDate
    private boolean bencoInfReq; // benco has requested additional information.
    private Integer bencoInfAtt; // employee attachment providing the additional information
    private Integer bencoAppBy; // empId of the benco
    private String bencoInfAttString; // string simulating info attachment

    // The requested amount and projected amount are read-only. The final amount can be adjusted upwards or downwards by the BenCo.
    // If adjusted upwards and exceeds employee's available amount, the request must be marked "Exceeding available funds for reporting purposes."
    private boolean bencoDowngrade; // if the Benco has adjusted the final amount downward from the projected amount,
                            // employee must be notified and they have the option to approve or deny the final amount
    private boolean bencoUpgrade; // if the Benco has adjusted the final reimbursement upwards past the employee's available amount. Mark the request as directed above
    private String whyBencoUpgrade;
    boolean empReject; // the employee has the option to cancel the request if the amount is adjusted downwards by the BenCo

    // Final Reimbursement Amount
    private BigDecimal finalAmt; // amount after adjustment applied for event type and any manual adjustments by the BenCo

    // Final grade must be reviewed by the BenCo or presentation graded by a superior (supervisor or department head)
    private boolean gradeSubmitted; // requestor has submitted final grade for review
    private boolean gradePassed; // can only be checked by the benco. if this or presPassed is checked, the request can be reimbursed
    private Integer gradeBy; // empId of the Benco who reviewed the grade and confirmed that it passed

    private boolean presSubmitted; // employee has submitted a presentation
    private boolean presPassed; // the presentation uploaded was
    private Integer presBy; // the empId of the supervisor or dept head who reviewed the attached presentation

    private Integer gradeAtt; // attId of the submitted grade or presentation
    private String gradeString; // string simulating grade or presentation attachment

    // Final checks before reimbursement. If BenCo has approved or if employee has not rejected downward adjusted amount, reimburse is set to true
    // then the reimbursed amount is subtracted from the employee's available amount (daily automated task that subtracts all reimbursements for past year)
    private boolean reimburse; // has passed all checks and releases funds to the requestor. taken out of circulation.
    private boolean denied; // if the request is denied by the direct sup, dept head, or benco the employee is sent an email notification and all further action canceled
                            // set to true if empReject is true, but no notifications are sent. taken out of circulation
    private Date denOn; // the date the reimbursement was denied
    private String denReason; // short explanation of why the reimbursement was denied
    private Integer denBy; // empId of the person who denied the reimbursement




    //Constructors
    public Request(){}

    public Request(Integer reqId, Integer reqBy, Date reqDate, Date startsOn, String startTime, Integer timeMissed, boolean urgent, String institution, String instCity, String instState, String eventType, String eventDesc, BigDecimal eventCost, boolean eventAtt, Integer eventAttId, String justification, boolean supPreApp, Integer spaAtt, String spaAttString, boolean dhPreApp, Integer dhpaAtt, String dhpaAttString, BigDecimal projAmt, String gradeFormat, boolean passFail, boolean pctGrade, Integer pctMin, boolean ltrGrade, String ltrMin, boolean presGrade, String minPass, String eventAttString, boolean supApp, boolean supInfReq, Integer supInfAtt, Integer supAppBy, String supInfString, boolean dhApp, boolean dhInfReq, Integer dhInfAtt, Integer dhAppBy, String dhInfString, boolean bencoApp, boolean bencoInfReq, Integer bencoInfAtt, String bencoInfAttString, Integer bencoAppBy, boolean bencoDowngrade, boolean bencoUpgrade, String whyBencoUpgrade, boolean empReject, BigDecimal finalAmt, boolean gradeSubmitted, boolean gradePassed, Integer gradeBy, boolean presSubmitted, boolean presPassed, Integer presBy, Integer gradeAtt, String gradeString, boolean reimburse, boolean denied, Date denOn, String denReason, Integer denBy) {
        this.reqId = reqId;
        this.reqBy = reqBy;
        this.reqDate = reqDate;
        this.startsOn = startsOn;
        this.startTime = startTime;
        this.timeMissed = timeMissed;
        this.urgent = urgent;
        this.institution = institution;
        this.instCity = instCity;
        this.instState = instState;
        this.eventType = eventType;
        this.eventDesc = eventDesc;
        this.eventCost = eventCost;
        this.eventAtt = eventAtt;
        this.eventAttId = eventAttId;
        this.justification = justification;
        this.supPreApp = supPreApp;
        this.spaAtt = spaAtt;
        this.spaAttString = spaAttString;
        this.dhPreApp = dhPreApp;
        this.dhpaAtt = dhpaAtt;
        this.dhpaAttString = dhpaAttString;
        this.projAmt = projAmt;
        this.gradeFormat = gradeFormat;
        this.passFail = passFail;
        this.pctGrade = pctGrade;
        this.pctMin = pctMin;
        this.ltrGrade = ltrGrade;
        this.ltrMin = ltrMin;
        this.presGrade = presGrade;
        this.minPass = minPass;
        this.eventAttString = eventAttString;
        this.supApp = supApp;
        this.supInfReq = supInfReq;
        this.supInfAtt = supInfAtt;
        this.supAppBy = supAppBy;
        this.supInfString = supInfString;
        this.dhApp = dhApp;
        this.dhInfReq = dhInfReq;
        this.dhInfAtt = dhInfAtt;
        this.dhAppBy = dhAppBy;
        this.dhInfString = dhInfString;
        this.bencoApp = bencoApp;
        this.bencoInfReq = bencoInfReq;
        this.bencoInfAtt = bencoInfAtt;
        this.bencoInfAttString = bencoInfAttString;
        this.bencoAppBy = bencoAppBy;
        this.bencoDowngrade = bencoDowngrade;
        this.bencoUpgrade = bencoUpgrade;
        this.whyBencoUpgrade = whyBencoUpgrade;
        this.empReject = empReject;
        this.finalAmt = finalAmt;
        this.gradeSubmitted = gradeSubmitted;
        this.gradePassed = gradePassed;
        this.gradeBy = gradeBy;
        this.presSubmitted = presSubmitted;
        this.presPassed = presPassed;
        this.presBy = presBy;
        this.gradeAtt = gradeAtt;
        this.gradeString = gradeString;
        this.reimburse = reimburse;
        this.denied = denied;
        this.denOn = denOn;
        this.denReason = denReason;
        this.denBy = denBy;
    }

    // Getters and Setters

    public Integer getReqId() {
        return reqId;
    }

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
    }

    public Integer getReqBy() {
        return reqBy;
    }

    public void setReqBy(Integer reqBy) {
        this.reqBy = reqBy;
    }

    public Date getReqDate() {
        return reqDate;
    }

    public void setReqDate(Date reqDate) {
        this.reqDate = reqDate;
    }

    public Date getStartsOn() {
        return startsOn;
    }

    public void setStartsOn(Date startsOn) {
        this.startsOn = startsOn;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getTimeMissed() {
        return timeMissed;
    }

    public void setTimeMissed(Integer timeMissed) {
        this.timeMissed = timeMissed;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getInstCity() {
        return instCity;
    }

    public void setInstCity(String instCity) {
        this.instCity = instCity;
    }

    public String getInstState() {
        return instState;
    }

    public void setInstState(String instState) {
        this.instState = instState;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public BigDecimal getEventCost() {
        return eventCost;
    }

    public void setEventCost(BigDecimal eventCost) {
        this.eventCost = eventCost;
    }

    public boolean isEventAtt() {
        return eventAtt;
    }

    public void setEventAtt(boolean eventAtt) {
        this.eventAtt = eventAtt;
    }

    public Integer getEventAttId() {
        return eventAttId;
    }

    public void setEventAttId(Integer eventAttId) {
        this.eventAttId = eventAttId;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public boolean isSupPreApp() {
        return supPreApp;
    }

    public void setSupPreApp(boolean supPreApp) {
        this.supPreApp = supPreApp;
    }

    public Integer getSpaAtt() {
        return spaAtt;
    }

    public void setSpaAtt(Integer spaAtt) {
        this.spaAtt = spaAtt;
    }

    public boolean isDhPreApp() {
        return dhPreApp;
    }

    public void setDhPreApp(boolean dhPreApp) {
        this.dhPreApp = dhPreApp;
    }

    public Integer getDhpaAtt() {
        return dhpaAtt;
    }

    public void setDhpaAtt(Integer dhpaAtt) {
        this.dhpaAtt = dhpaAtt;
    }

    public String getSpaAttString() {
        return spaAttString;
    }

    public void setSpaAttString(String spaAttString) {
        this.spaAttString = spaAttString;
    }

    public String getDhpaAttString() {
        return dhpaAttString;
    }

    public void setDhpaAttString(String dhpaAttString) {
        this.dhpaAttString = dhpaAttString;
    }

    public BigDecimal getProjAmt() {
        return projAmt;
    }

    public void setProjAmt(BigDecimal projAmt) {
        this.projAmt = projAmt;
    }

    public String getGradeFormat() {
        return gradeFormat;
    }

    public void setGradeFormat(String gradeFormat) {
        this.gradeFormat = gradeFormat;
    }

    public boolean isPassFail() {
        return passFail;
    }

    public void setPassFail(boolean passFail) {
        this.passFail = passFail;
    }

    public String getPfMin() {
        return pfMin;
    }

    public boolean isPctGrade() {
        return pctGrade;
    }

    public void setPctGrade(boolean pctGrade) {
        this.pctGrade = pctGrade;
    }

    public Integer getPctMin() {
        return pctMin;
    }

    public void setPctMin(Integer pctMin) {
        this.pctMin = pctMin;
    }

    public boolean isLtrGrade() {
        return ltrGrade;
    }

    public void setLtrGrade(boolean ltrGrade) {
        this.ltrGrade = ltrGrade;
    }

    public String getLtrMin() {
        return ltrMin;
    }

    public void setLtrMin(String ltrMin) {
        this.ltrMin = ltrMin;
    }

    public boolean isPresGrade() {
        return presGrade;
    }

    public String getMinPass() {
        return minPass;
    }

    public void setMinPass(String minPass) {
        this.minPass = minPass;
    }

    public void setPresGrade(boolean presGrade) {
        this.presGrade = presGrade;
    }

    public String getEventAttString() {
        return eventAttString;
    }

    public void setEventAttString(String eventAttString) {
        this.eventAttString = eventAttString;
    }

    public boolean isSupApp() {
        return supApp;
    }

    public void setSupApp(boolean supApp) {
        this.supApp = supApp;
    }

    public boolean isSupInfReq() {
        return supInfReq;
    }

    public void setSupInfReq(boolean supInfReq) {
        this.supInfReq = supInfReq;
    }

    public Integer getSupInfAtt() {
        return supInfAtt;
    }

    public void setSupInfAtt(Integer supInfAtt) {
        this.supInfAtt = supInfAtt;
    }

    public Integer getSupAppBy() {
        return supAppBy;
    }

    public void setSupAppBy(Integer supAppBy) {
        this.supAppBy = supAppBy;
    }

    public boolean isDhApp() {
        return dhApp;
    }

    public void setDhApp(boolean dhApp) {
        this.dhApp = dhApp;
    }

    public boolean isDhInfReq() {
        return dhInfReq;
    }

    public void setDhInfReq(boolean dhInfReq) {
        this.dhInfReq = dhInfReq;
    }

    public Integer getDhInfAtt() {
        return dhInfAtt;
    }

    public void setDhInfAtt(Integer dhInfAtt) {
        this.dhInfAtt = dhInfAtt;
    }

    public Integer getDhAppBy() {
        return dhAppBy;
    }

    public void setDhAppBy(Integer dhAppBy) {
        this.dhAppBy = dhAppBy;
    }

    public boolean isBencoApp() {
        return bencoApp;
    }

    public void setBencoApp(boolean bencoApp) {
        this.bencoApp = bencoApp;
    }

    public boolean isBencoInfReq() {
        return bencoInfReq;
    }

    public void setBencoInfReq(boolean bencoInfReq) {
        this.bencoInfReq = bencoInfReq;
    }

    public Integer getBencoInfAtt() {
        return bencoInfAtt;
    }

    public void setBencoInfAtt(Integer bencoInfAtt) {
        this.bencoInfAtt = bencoInfAtt;
    }

    public Integer getBencoAppBy() {
        return bencoAppBy;
    }

    public void setBencoAppBy(Integer bencoAppBy) {
        this.bencoAppBy = bencoAppBy;
    }

    public boolean isBencoDowngrade() {
        return bencoDowngrade;
    }

    public void setBencoDowngrade(boolean bencoDowngrade) {
        this.bencoDowngrade = bencoDowngrade;
    }

    public boolean isBencoUpgrade() {
        return bencoUpgrade;
    }

    public void setBencoUpgrade(boolean bencoUpgrade) {
        this.bencoUpgrade = bencoUpgrade;
    }

    public String getWhyBencoUpgrade() {
        return whyBencoUpgrade;
    }

    public void setWhyBencoUpgrade(String whyBencoUpgrade) {
        this.whyBencoUpgrade = whyBencoUpgrade;
    }

    public boolean isEmpReject() {
        return empReject;
    }

    public void setEmpReject(boolean empReject) {
        this.empReject = empReject;
    }

    public BigDecimal getFinalAmt() {
        return finalAmt;
    }

    public void setFinalAmt(BigDecimal finalAmt) {
        this.finalAmt = finalAmt;
    }

    public boolean isGradeSubmitted() {
        return gradeSubmitted;
    }

    public void setGradeSubmitted(boolean gradeSubmitted) {
        this.gradeSubmitted = gradeSubmitted;
    }

    public boolean isGradePassed() {
        return gradePassed;
    }

    public void setGradePassed(boolean gradePassed) {
        this.gradePassed = gradePassed;
    }

    public Integer getGradeBy() {
        return gradeBy;
    }

    public void setGradeBy(Integer gradeBy) {
        this.gradeBy = gradeBy;
    }

    public boolean isPresSubmitted() {
        return presSubmitted;
    }

    public void setPresSubmitted(boolean presSubmitted) {
        this.presSubmitted = presSubmitted;
    }

    public boolean isPresPassed() {
        return presPassed;
    }

    public void setPresPassed(boolean presPassed) {
        this.presPassed = presPassed;
    }

    public Integer getPresBy() {
        return presBy;
    }

    public void setPresBy(Integer presBy) {
        this.presBy = presBy;
    }

    public Integer getGradeAtt() {
        return gradeAtt;
    }

    public void setGradeAtt(Integer gradeAtt) {
        this.gradeAtt = gradeAtt;
    }

    public boolean isReimburse() {
        return reimburse;
    }

    public void setReimburse(boolean reimburse) {
        this.reimburse = reimburse;
    }

    public boolean isDenied() {
        return denied;
    }

    public void setDenied(boolean denied) {
        this.denied = denied;
    }

    public Date getDenOn() {
        return denOn;
    }

    public void setDenOn(Date denOn) {
        this.denOn = denOn;
    }

    public String getDenReason() {
        return denReason;
    }

    public void setDenReason(String denReason) {
        this.denReason = denReason;
    }

    public Integer getDenBy() {
        return denBy;
    }

    public void setDenBy(Integer denBy) {
        this.denBy = denBy;
    }

    public String getSupInfString() {
        return supInfString;
    }

    public void setSupInfString(String supInfString) {
        this.supInfString = supInfString;
    }

    public String getDhInfString() {
        return dhInfString;
    }

    public void setDhInfString(String dhInfString) {
        this.dhInfString = dhInfString;
    }

    public String getBencoInfAttString() {
        return bencoInfAttString;
    }

    public void setBencoInfAttString(String bencoInfAttString) {
        this.bencoInfAttString = bencoInfAttString;
    }

    public String getGradeString() {
        return gradeString;
    }

    public void setGradeString(String gradeString) {
        this.gradeString = gradeString;
    }

    @Override
    public String toString() {
        return "Request{" +
                "reqId=" + reqId +
                ", reqBy=" + reqBy +
                ", reqDate=" + reqDate +
                ", startsOn=" + startsOn +
                ", startTime='" + startTime + '\'' +
                ", timeMissed=" + timeMissed +
                ", urgent=" + urgent +
                ", institution='" + institution + '\'' +
                ", instCity='" + instCity + '\'' +
                ", instState='" + instState + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventDesc='" + eventDesc + '\'' +
                ", eventCost=" + eventCost +
                ", eventAtt=" + eventAtt +
                ", eventAttId=" + eventAttId +
                ", justification='" + justification + '\'' +
                ", supPreApp=" + supPreApp +
                ", spaAtt=" + spaAtt +
                ", spaAttString='" + spaAttString + '\'' +
                ", dhPreApp=" + dhPreApp +
                ", dhpaAtt=" + dhpaAtt +
                ", dhpaAttString='" + dhpaAttString + '\'' +
                ", projAmt=" + projAmt +
                ", gradeFormat='" + gradeFormat + '\'' +
                ", passFail=" + passFail +
                ", pfMin='" + pfMin + '\'' +
                ", pctGrade=" + pctGrade +
                ", pctMin=" + pctMin +
                ", ltrGrade=" + ltrGrade +
                ", ltrMin='" + ltrMin + '\'' +
                ", presGrade=" + presGrade +
                ", minPass='" + minPass + '\'' +
                ", eventAttString='" + eventAttString + '\'' +
                ", supApp=" + supApp +
                ", supInfReq=" + supInfReq +
                ", supInfAtt=" + supInfAtt +
                ", supAppBy=" + supAppBy +
                ", supInfString='" + supInfString + '\'' +
                ", dhApp=" + dhApp +
                ", dhInfReq=" + dhInfReq +
                ", dhInfAtt=" + dhInfAtt +
                ", dhAppBy=" + dhAppBy +
                ", dhInfString='" + dhInfString + '\'' +
                ", bencoApp=" + bencoApp +
                ", bencoInfReq=" + bencoInfReq +
                ", bencoInfAtt=" + bencoInfAtt +
                ", bencoAppBy=" + bencoAppBy +
                ", bencoInfAttString='" + bencoInfAttString + '\'' +
                ", bencoDowngrade=" + bencoDowngrade +
                ", bencoUpgrade=" + bencoUpgrade +
                ", whyBencoUpgrade='" + whyBencoUpgrade + '\'' +
                ", empReject=" + empReject +
                ", finalAmt=" + finalAmt +
                ", gradeSubmitted=" + gradeSubmitted +
                ", gradePassed=" + gradePassed +
                ", gradeBy=" + gradeBy +
                ", presSubmitted=" + presSubmitted +
                ", presPassed=" + presPassed +
                ", presBy=" + presBy +
                ", gradeAtt=" + gradeAtt +
                ", gradeString='" + gradeString + '\'' +
                ", reimburse=" + reimburse +
                ", denied=" + denied +
                ", denOn=" + denOn +
                ", denReason='" + denReason + '\'' +
                ", denBy=" + denBy +
                '}';
    }
}
