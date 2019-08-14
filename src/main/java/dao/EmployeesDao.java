package dao;

import models.Departments;
import models.Employees;

import java.util.List;

public interface EmployeesDao {
    //create
    void add(Employees employees);

    //Get and find all employees ...by id
    List<Employees>getAllEmployees();
    Employees findById(int id);

    //Add and get employees to department
    void addEmpToDepartments(Employees employees, Departments departments);
    List<Departments>getAllDptBelongingToEmployees(int emp_id);
    //delete
    void deleteById(int id);

    //clear
    void clearAll();

}
