package dao.imple;

import dao.DepartmentDao;
import java.util.ArrayList;
import model.DepartmentModel;

public class DepartmentDaoImpl implements DepartmentDao{

    @Override
    public boolean saveEmployee(DepartmentModel dm) {
        return true;  
    }

    @Override
    public DepartmentModel getEmployee(DepartmentModel dm) {
        return dm; 
    }

    @Override
    public ArrayList<DepartmentModel> getEmployee() {
        ArrayList<DepartmentModel> list = new ArrayList<>();
        return list;
    }

    @Override
    public boolean updateEmployee(DepartmentModel dm) {
        return true; 
    }

    @Override
    public boolean deleteEmployee(DepartmentModel dm) {
        return true; 
    }
    
}
