package dev.melick.repositories;

import dev.melick.models.Employee;
import dev.melick.models.Request;

import javax.persistence.criteria.CriteriaBuilder;

import static dev.melick.utils.ConnectionUtil.getConnectionUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpRepo {

    private static EmpRepo er = new EmpRepo();

    public Employee getByUserName(String username){
        try (Connection connection = getConnectionUtil().getConnection()){

        String sql = "SELECT * FROM employees WHERE username = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if(rs.next()) {

            Employee emp = new Employee();

            emp.setEmpId(rs.getInt("emp_id"));
            emp.setFirstName(rs.getString("first_name"));
            emp.setLastName(rs.getString("last_name"));
            emp.setUsername(rs.getString("username"));
            emp.setPassword(rs.getString("password"));
            emp.setDepartment(rs.getString("department"));
            emp.setSupervisor(rs.getBoolean("supervisor"));
            emp.setDeptHead(rs.getBoolean("dept_head"));
            emp.setEligible(rs.getBigDecimal("eligible"));
            emp.setAwarded(rs.getBigDecimal("awarded"));
            emp.setPending(rs.getBigDecimal("pending"));

            return emp;
        }

        }catch(SQLException e){
        e.printStackTrace();
        }
        return null;
    }

    public Employee getByEmpId(Integer empId){
        try (Connection connection = getConnectionUtil().getConnection()){

            System.out.println("emp Id received in EmpRepo getByEmpId: " + empId);
            String sql = "SELECT * FROM employees WHERE emp_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                Employee emp = new Employee();

                emp.setEmpId(rs.getInt("emp_id"));
                emp.setFirstName(rs.getString("first_name"));
                emp.setLastName(rs.getString("last_name"));
                emp.setUsername(rs.getString("username"));
                emp.setPassword(rs.getString("password"));
                emp.setDepartment(rs.getString("department"));
                emp.setSupervisor(rs.getBoolean("supervisor"));
                emp.setDeptHead(rs.getBoolean("dept_head"));
                emp.setEligible(rs.getBigDecimal("eligible"));
                emp.setAwarded(rs.getBigDecimal("awarded"));
                emp.setPending(rs.getBigDecimal("pending"));

                return emp;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Employee getSupByDept(String dept){
        try (Connection connection = getConnectionUtil().getConnection()){

            String sql = "SELECT * FROM employees WHERE department = ? AND supervisor IS TRUE";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, dept);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                Employee emp = new Employee();

                emp.setEmpId(rs.getInt("emp_id"));
                emp.setFirstName(rs.getString("first_name"));
                emp.setLastName(rs.getString("last_name"));
                emp.setUsername(rs.getString("username"));
                emp.setPassword(rs.getString("password"));
                emp.setDepartment(rs.getString("department"));
                emp.setSupervisor(rs.getBoolean("supervisor"));
                emp.setDeptHead(rs.getBoolean("dept_head"));
                emp.setEligible(rs.getBigDecimal("eligible"));
                emp.setAwarded(rs.getBigDecimal("awarded"));
                emp.setPending(rs.getBigDecimal("pending"));

                return emp;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Employee getDeptHeadByDept(String dept){
        try (Connection connection = getConnectionUtil().getConnection()){

            String sql = "SELECT * FROM employees WHERE department = ? AND dept_head IS TRUE";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, dept);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                Employee emp = new Employee();

                emp.setEmpId(rs.getInt("emp_id"));
                emp.setFirstName(rs.getString("first_name"));
                emp.setLastName(rs.getString("last_name"));
                emp.setUsername(rs.getString("username"));
                emp.setPassword(rs.getString("password"));
                emp.setDepartment(rs.getString("department"));
                emp.setSupervisor(rs.getBoolean("supervisor"));
                emp.setDeptHead(rs.getBoolean("dept_head"));
                emp.setEligible(rs.getBigDecimal("eligible"));
                emp.setAwarded(rs.getBigDecimal("awarded"));
                emp.setPending(rs.getBigDecimal("pending"));

                return emp;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Integer getBencoId(){
        try (Connection connection = getConnectionUtil().getConnection()){

            String sql = "SELECT * FROM employees WHERE department = ? AND NOT supervisor";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "benco");
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                Employee emp = new Employee();

                emp.setEmpId(rs.getInt("emp_id"));

                return emp.getEmpId();
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void setEligible(Integer empId, BigDecimal amount){

        try (Connection connection = getConnectionUtil().getConnection()){

            String sql = "UPDATE employees SET eligible = ? where emp_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setBigDecimal(1, amount);
            ps.setInt(2, empId);

            ps.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void setPending(Integer empId, BigDecimal amount){

        try (Connection connection = getConnectionUtil().getConnection()){

            String sql = "UPDATE employees SET pending = ? where emp_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setBigDecimal(1, amount);
            ps.setInt(2, empId);

            ps.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

/*    public static void main(String[] args){
        Employee vinnie = er.getByUserName("vincent.diesel");
        System.out.println(vinnie);

    }*/
}
