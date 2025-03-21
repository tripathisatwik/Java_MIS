package controller;

import dao.DepartmentDao;
import dao.imple.DepartmentDaoImpl;
import java.util.ArrayList;
import model.DepartmentModel;

public class DepartmentComtroller {

    public int department_id;
    public String department_code;
    public String department_name;

    DepartmentDao dd = new DepartmentDaoImpl();

    public ArrayList<DepartmentModel> getDepartmentList() {
        return dd.getEmployee();
    }
}
