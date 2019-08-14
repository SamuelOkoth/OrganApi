import com.google.gson.Gson;
import dao.Sql2oNewsDao;
import dao.Sql2oEmployeesDao;
import dao.Sql2oDepartmentsDao;
import models.Departments;
import exceptions.ApiException;
import models.Employees;
import models.News;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        Sql2oDepartmentsDao departmentsDao;
        Sql2oNewsDao newsDao;
        Sql2oEmployeesDao employeesDao;
        Connection conn;
        Gson gson = new Gson();

        String connectionString = "jdbc:postgresql://localhost:5432/api";
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "12345");

        departmentsDao = new Sql2oDepartmentsDao(sql2o);
        employeesDao = new Sql2oEmployeesDao(sql2o);
        newsDao = new Sql2oNewsDao(sql2o);
        conn = sql2o.open();

        //create
        post("/departments/new", "application/json", (req, res) -> {
            Departments departments = gson.fromJson(req.body(), Departments.class);
            departmentsDao.add(departments);
            res.status(201);
            res.type("application/json");
            return gson.toJson(departmentsDao.getAllDepartments());
        });

        //read all

        get("/departments", "application/json", (req, res) -> { //accept a request in format JSON from an app
            res.type("application/json");
            return gson.toJson(departmentsDao.getAllDepartments());//send it back to be displayed
        });
        get("/departments/:id", "application/json", (req, res) -> { //accept a request in format JSON from an app
            res.type("application/json");
            int dpt_id = Integer.parseInt(req.params("id"));

            Departments departmentsToFind = departmentsDao.findById(dpt_id);
            if (departmentsToFind == null){
                throw new ApiException(404, String.format("No department with the id: \"%s\" exists", req.params("id")));
            }
            res.type("application/json");
            return gson.toJson(departmentsDao.findById(dpt_id));
        });


        //news : dpt

        //Add news 2 department
        post("/departments/:id/news/new","application/json",(request, response) -> {
            int id = Integer.parseInt(request.params("id"));
            News news = gson.fromJson(request.body(),News.class);
            news.setNews_id(id);

            newsDao.add(news);
            response.type("application/json");
            response.status(201);
            return gson.toJson(news);
        });
        //access news for a certain department
        get("/departments/:id/dptNews", "application/json", (request, response) -> {
            int id = Integer.parseInt(request.params("id"));
          //  Departments departmentToFind = departmentsDao.findById(id);
            return gson.toJson(newsDao.getAllNews());
        });
        post("/departments/:dpt_id/employees/new", "application/json", (req, res) -> {
            int dpt_id = Integer.parseInt(req.params("dpt_id"));
            Employees employees = gson.fromJson(req.body(), Employees.class);
            employees.setId(dpt_id);
            employeesDao.add(employees);
            res.status(201);
            res.type("application/json");
            return gson.toJson(employees);
        });

        //Add an employee
        post("/employees/new", "application/json", (request, response) -> {
            Employees employees = gson.fromJson(request.body(), Employees.class);
            employeesDao.add(employees);
            response.type("application/json");
            response.status(201);
            return gson.toJson(employees);
        });
        //access all employees
        get("/employees", "application/json", (request, response) ->
        {
            response.type("application/json");
            return gson.toJson(employeesDao.getAllEmployees());
        });
        //Assign a department to an employee
        post("/employees/emp_id/departments/:dpt_id","application/json",(request, response) -> {
            int emp_id = Integer.parseInt(request.params("emp_id"));
            int dpt_id = Integer.parseInt(request.params("dpt_id"));
            Employees empFound = employeesDao.findById(emp_id);
            Departments dptFound = departmentsDao.findById(dpt_id);

            if (dptFound != null && empFound!= null){
                departmentsDao.addDptToEmployees(dptFound,empFound);
                response.type("application/json");
                response.status(201);
                return gson.toJson(String.format("Employees '%s' and Department '%s' successfully created",empFound.getEmp_name(), dptFound.getDpt_name()));
            }
            else {
                throw new ApiException(404, String.format("Unfortunately the Employee or Department has  not been found"));
            }
        });



        get("/employees/:emp_id/departments","application/json",(request, response) -> {
            int emp_id = Integer.parseInt(request.params("emp_id"));
            Employees employeesTofind = employeesDao.findById(emp_id);

            if (employeesTofind == null){
                throw new Exception("Employee with that id does not exist");
            }else if(employeesDao.getAllDptBelongingToEmployees(emp_id).size() == 0){
                return "{\"message\":\"Opps!the  Employee is not associated with any of the departments\"}";
            }else {
                return gson.toJson(employeesDao.getAllDptBelongingToEmployees(emp_id));
            }
        });


        //Add news
        post("/news/new","application/json",(request, response) -> {
            News news = gson.fromJson(request.body(),News.class);
            newsDao.add(news);
            response.type("application/json");
            response.status(201);
            return gson.toJson(news);
        });
        //Read all news
        get("/news","application/json",(request, response) -> {
            response.type("application/json");
            return gson.toJson(newsDao.getAllNews());
        });

        exception(ApiException.class, (exc, req, res) -> {
            ApiException err = (ApiException) exc;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatusCode());
            jsonMap.put("errorMessage", err.getMessage());
            res.type("application/json"); //after does not run in case of an exception.
            res.status(err.getStatusCode()); //set the status
            res.body(gson.toJson(jsonMap));  //set the output.
        });
        //FILTERS
        after((req, res) ->{
            res.type("application/json");
        });

    }
}
