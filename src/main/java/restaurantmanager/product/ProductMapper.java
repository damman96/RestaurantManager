package restaurantmanager.product;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
	
	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
	
	ProductDto map(Product product);
	
	Product map(ProductDto productDto);
	
	ModifyProductDto mapFromModify(Product product);
	
	Product mapToModify(ModifyProductDto modifyProductDto);
	
}
