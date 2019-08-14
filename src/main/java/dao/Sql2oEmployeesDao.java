
package dao;

import models.Departments;
import models.Employees;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oEmployeesDao implements EmployeesDao {
    private final Sql2o sql2o;
    public Sql2oEmployeesDao(Sql2o sql2o){
        this.sql2o = sql2o;
    }


    @Override
    public void add(Employees employees) {
        String sql = "INSERT INTO employees(emp_name, emp_position, emp_role,emp_details) VALUES (:emp_name, :emp_position, :emp_role,:emp_details)";
        try(Connection conn = sql2o.open()){
            int id = (int) conn.createQuery(sql, true)
                    .bind(employees)
                    .executeUpdate()
                    .getKey();
            employees.setId(id);
        }catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public List<Employees> getAllEmployees() {
        String sql = "SELECT * FROM employees";
        try(Connection conn = sql2o.open()){
            return conn.createQuery(sql).executeAndFetch(Employees.class);
        }
    }

    @Override
    public Employees findById(int id) {
        String sql = "SELECT * FROM employees WHERE id = :id";
        try(Connection conn = sql2o.open()){
            return conn.createQuery(sql).addParameter("id", id).executeAndFetchFirst(Employees.class);
        }
    }

    @Override
    public void addEmpToDepartments(Employees employees, Departments departments) {
        String sql = "INSERT INTO departments_employees(dpt_id, emp_id) VALUES (:dpt_id, :emp_id)";
        try(Connection conn = sql2o.open()){
            conn.createQuery(sql)
                    .addParameter("dpt_id", departments.getId())
                    .addParameter("emp_id", employees.getId())
                    .throwOnMappingFailure(false)
                    .executeUpdate();
        }catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public List<Departments> getAllDptBelongingToEmployees(int emp_id) {
        ArrayList<Departments> allDepartments = new ArrayList<>();
        String joinQuery = "SELECT dpt_id FROM departments_employees WHERE emp_id =:emp_id";
        try(Connection conn = sql2o.open()){
            List<Integer> allDepartmentIds = conn.createQuery(joinQuery)
                    .addParameter("emp_id", emp_id)
                    .executeAndFetch(Integer.class);
            for(Integer dpt_id : allDepartmentIds){
                String departmentsQuery = "SELECT * FROM departments WHERE id=:dpt_id";
                allDepartments.add(conn.createQuery(departmentsQuery)
                        .addParameter("dpt_id", dpt_id)
                        .executeAndFetchFirst(Departments.class));
            }
        }catch (Sql2oException ex){
            System.out.println(ex);
        }
        return allDepartments;
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from employees WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void clearAll() {
        String sql = "DELETE from employees";
        try(Connection conn = sql2o.open()){
            conn.createQuery(sql).executeUpdate();
        }catch (Sql2oException ex){
            System.out.println(ex);
        }
    }
}