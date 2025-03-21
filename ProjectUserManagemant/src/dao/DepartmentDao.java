package dao;
import java.util.ArrayList;
import model.DepartmentModel;
public interface DepartmentDao {    
    //To store employee data
    public abstract boolean saveEmployee(DepartmentModel dm);
    //To get single employee information
    public abstract DepartmentModel getEmployee(DepartmentModel dm);
    //To get all Employess
    public abstract ArrayList<DepartmentModel> getEmployee();
    // To update employee information
    public abstract boolean updateEmployee(DepartmentModel dm);
    //To delete single employees
    public abstract boolean deleteEmployee(DepartmentModel dm);
}
