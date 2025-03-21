package dao.imple;

import config.DatabaseConnection;
import dao.EmployeeDao;
import java.util.ArrayList;
import model.EmployeeModel;
import java.sql.ResultSet;
import model.DepartmentModel;

public class EmployeeDaoImpl implements EmployeeDao {

    DatabaseConnection connection = DatabaseConnection.getInstance();

    @Override
    public boolean saveEmployee(EmployeeModel em) {
        boolean status = false;
        String query = "Insert into employee('') values ()";
        if (connection.iudQueryBuilder(query) > 0) {
            status = true;
        }
        return status;
    }

    @Override
    public  ArrayList<EmployeeModel> getEmployee() {
        boolean status = false;
        String query = "Select * from employee where id = " + em.getEmpNo();
        ResultSet data = connection.selectQueryBuilder(query);
        DepartmentModel dm = new DepartmentModel();
        ArrayList<EmployeeModel> List = new ArrayList();
        try {
            while (data.next()) {
                em.setFirstName(data.getString("First_Name"));
                em.setMiddleName(data.getString("Middle_Name"));
                em.setLastName(data.getString("Last_Name"));

                dm.setDepartmentId(data.getInt("DepartmentId"));
                
                em.setDepartment(dm);
                em.setDesignation(data.getString("Designation"));
                
                List.add(em)
                

            }
        } catch () {

        }

        if (connection.iudQueryBuilder(query) > 0) {
            status = true;
        }
        return status;
    }

    @Override
    public ArrayList<EmployeeModel> getEmployee() {
        ArrayList<EmployeeModel> list = new ArrayList<>();
        return list;
    }

    @Override
    public boolean updateEmployee(EmployeeModel em) {
        return true;
    }

    @Override
    public boolean deleteEmployee(EmployeeModel em) {
        return true;
    }

}
