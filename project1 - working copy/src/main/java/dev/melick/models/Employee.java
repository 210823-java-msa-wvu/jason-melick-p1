package dev.melick.models;

import java.math.BigDecimal;

public class Employee {

    private Integer empId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String department;
    private boolean supervisor;
    private boolean deptHead;
    private BigDecimal eligible; // $1,000 yearly. eligible = $1,000.00 - awarded - pending
    private BigDecimal awarded;  // total awarded for the year
    private BigDecimal pending;  // amount of any pending requests not yet reimbursed

    public Employee(){}

    public Employee(Integer empId, String firstName, String lastName, String username, String password, String department,
                    boolean supervisor, boolean deptHead, BigDecimal eligible, BigDecimal awarded, BigDecimal pending) {
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.department = department;
        this.supervisor = supervisor;
        this.deptHead = deptHead;
        this.eligible = eligible;
        //this.awarded = awarded;
        this.pending = pending;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isSupervisor() {
        return supervisor;
    }

    public void setSupervisor(boolean supervisor) {
        this.supervisor = supervisor;
    }

    public boolean isDeptHead() {
        return deptHead;
    }

    public void setDeptHead(boolean deptHead) {
        this.deptHead = deptHead;
    }

    public BigDecimal getEligible() {
        return eligible;
    }

    public void setEligible(BigDecimal eligible) {
        this.eligible = eligible;
    }

    public BigDecimal getAwarded() {
        return awarded;
    }

    public void setAwarded(BigDecimal awarded) {
        this.awarded = awarded;
    }

    public BigDecimal getPending() {
        return pending;
    }

    public void setPending(BigDecimal pending) {
        this.pending = pending;
    }

    @Override
    public String toString() {
        return "employee{" +
                "empId=" + empId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", department='" + department + '\'' +
                ", supervisor=" + supervisor +
                ", deptHead=" + deptHead +
                ", eligible=" + eligible +
                ", awarded=" + awarded +
                ", pending=" + pending +
                '}';
    }
}
