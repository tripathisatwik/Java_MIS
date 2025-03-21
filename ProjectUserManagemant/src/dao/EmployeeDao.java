package dao;
import java.util.ArrayList;
import model.EmployeeModel;
public interface EmployeeDao {
    //To store employee data
    public abstract boolean saveEmployee(EmployeeModel em);
    //To get single employee information
    public abstract EmployeeModel getEmployee(EmployeeModel em);
    //To get all Employess
    public abstract ArrayList<EmployeeModel> getEmployee();
    // To update employee information
    public abstract boolean updateEmployee(EmployeeModel em);
    //To delete single employees
    public abstract boolean deleteEmployee(EmployeeModel em);
}
