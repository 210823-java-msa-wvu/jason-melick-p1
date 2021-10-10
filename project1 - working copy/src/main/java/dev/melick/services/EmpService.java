package dev.melick.services;

import dev.melick.models.Employee;
import dev.melick.repositories.EmpRepo;

public class EmpService {

    private static EmpRepo er = new EmpRepo();

    public boolean credPass(String password, Employee emp){

        try {
            if(emp.getPassword().equals(password)){
                return true;
            }
        }catch (NullPointerException e){
            System.out.println("Username or password not found.");
        }
        return false;
    }

    public Integer getSupId(Integer empId){

        Employee emp = er.getByEmpId(empId);
        String dept = emp.getDepartment();

        Employee sup = er.getSupByDept(dept);

        return sup.getEmpId();

    }

    public Integer getDeptHeadId(Integer empId){

        Employee emp = er.getByEmpId(empId);
        String dept = emp.getDepartment();

        Employee dh = er.getDeptHeadByDept(dept);

        return dh.getEmpId();

    }
}
