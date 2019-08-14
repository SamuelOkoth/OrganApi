package dao;

import dao.DepartmentsDao;
import models.News;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import models.Departments;
import models.Employees;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oDepartmentsDao implements DepartmentsDao {
    private final Sql2o sql2o;
    public  Sql2oDepartmentsDao(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    @Override
    public void add(Departments departments) {
        String sql = "INSERT INTO departments (dpt_name, dpt_description, dpt_empNo) VALUES (:dpt_name, :dpt_description, :dpt_empNo)";
        try(Connection conn = sql2o.open()){
            int id = (int) conn.createQuery(sql, true)
                    .bind(departments)
                    .executeUpdate()
                    .getKey();
            departments.setId(id);
        }catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public List<Departments> getAllDepartments() {
        String sql = "SELECT * FROM departments";
        try(Connection conn = sql2o.open()){
            return conn.createQuery(sql).executeAndFetch(Departments.class);
        }
    }

    //M:M
    @Override
    public void addDptToEmployees(Departments departments, Employees employees) {
        String sql = "INSERT INTO departments_employees(dpt_id, emp_id) VALUES (:dpt_id, :emp_id)";
        try(Connection conn = sql2o.open()){
            conn.createQuery(sql)
                    .addParameter("dpt_id", departments.getId())
                    .addParameter("emp_id", employees.getId())
                    .executeUpdate();
        }catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public List<Employees> getAllEmployeesBelongingToDepartment(int dpt_id) {
        ArrayList<Employees> allEmployees = new ArrayList<>();
        String joinQuery = "SELECT emp_id FROM departments_employees WHERE dpt_id =:dpt_id";
        try(Connection conn = sql2o.open()){
            List<Integer> allEmployeesIds = conn.createQuery(joinQuery)
                    .addParameter("dpt_id", dpt_id)
                    .executeAndFetch(Integer.class);
            for(Integer emp_id: allEmployeesIds){
                String employeesQuery = "SELECT * FROM employees WHERE id=:emp_id";
                allEmployees.add(conn.createQuery(employeesQuery)
                        .addParameter("emp_id", emp_id)
                        .executeAndFetchFirst(Employees.class));
            }
        }catch (Sql2oException ex){
            System.out.println(ex);
        }
        return allEmployees;
    }

    @Override
    public Departments findById(int id) {
        String sql = "SELECT * FROM departments WHERE id=:id";
        try(Connection conn = sql2o.open()){
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Departments.class);
        }
    }

    @Override
    public List<News> getAllNews(int dpt_id) {
        String sql = "SELECT * FROM news WHERE dpt_id=:dpt_id";
        try(Connection conn = sql2o.open()){
            return conn.createQuery(sql).addParameter("dpt_id", dpt_id)
                    .throwOnMappingFailure(false)
                    .executeAndFetch(News.class);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from departments WHERE id=:id";
        String deleteJoin = "DELETE from departments_employees WHERE dpt_id = :dpt_id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
            con.createQuery(deleteJoin)
                    .addParameter("dpt_id",id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void clearAll() {
        String sql = "DELETE from departments";
        try(Connection conn = sql2o.open()){
            conn.createQuery(sql).executeUpdate();
        }catch (Sql2oException ex){
            System.out.println(ex);
        }

    }
}