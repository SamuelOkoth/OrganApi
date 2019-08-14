package models;

import java.util.Objects;

public class Departments {
    private String dpt_name;
    private String dpt_description;
    private int dpt_empNo;
    private int id;


    //Constructor


    public Departments(String dpt_name, String dpt_description, int dpt_empNo) {
        this.dpt_name = dpt_name;
        this.dpt_description = dpt_description;
        this.dpt_empNo = dpt_empNo;

    }


    //Override

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Departments that = (Departments) o;
        return dpt_empNo == that.dpt_empNo &&
                Objects.equals(dpt_name, that.dpt_name) &&
                Objects.equals(dpt_description, that.dpt_description) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dpt_name, dpt_description, dpt_empNo, id);
    }

    public String getDpt_name() {
        return dpt_name;
    }

    public void setDpt_name(String dpt_name) {
        this.dpt_name = dpt_name;
    }

    public String getDpt_description() {
        return dpt_description;
    }

    public void setDpt_description(String dpt_description) {
        this.dpt_description = dpt_description;
    }

    public int getDpt_empNo() {
        return dpt_empNo;
    }

    public void setDpt_empNo(int dpt_empNo) {
        this.dpt_empNo = dpt_empNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
//getters and setters

}
