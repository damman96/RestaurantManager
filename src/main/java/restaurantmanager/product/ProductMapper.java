package restaurantmanager.product;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
	
	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
	
	ProductDto map(Product product);
	
	Product mapFromModify(ModifyProductDto modifyProductDto);
	
	ModifyProductDto mapToModify(Product product);
	
}
