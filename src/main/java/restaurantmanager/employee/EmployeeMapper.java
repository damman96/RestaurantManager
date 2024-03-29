package restaurantmanager.employee;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {
	
	EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);
	
	EmployeeDto map(Employee employee);
	
	Employee mapFromModify(ModifyEmployeeDto modifyEmployeeDto);
	
	ModifyEmployeeDto mapToModify(Employee employee);
	
}
