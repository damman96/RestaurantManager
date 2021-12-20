package restaurantmanager.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomBigDecimal;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomString;

import restaurantmanager.product.ModifyProductDto;
import restaurantmanager.product.Product;
import restaurantmanager.product.ProductDto;

public abstract class ProductFixture {
	
	public static Product createProductEntityWithNulls() {
		return Product.builder().build();
	}
	
	public static Product createProductEntity(final Long id) {
		return Product.builder()
				.id(id)
				.name(createRandomString())
				.category(createRandomString())
				.description(createRandomString())
				.price(createRandomBigDecimal())
				.productType(createRandomString())
				.build();
	}
	
	public static Product createProductEntity(final Long id, final String productType) {
		return Product.builder()
				.id(id)
				.name(createRandomString())
				.category(createRandomString())
				.description(createRandomString())
				.price(createRandomBigDecimal())
				.productType(productType)
				.build();
	}
	
	public static Product createProductEntity(final Long id, final String category, final String productType) {
		return Product.builder()
				.id(id)
				.name(createRandomString())
				.category(category)
				.description(createRandomString())
				.price(createRandomBigDecimal())
				.productType(productType)
				.build();
	}
	
	public static Product createProductEntityFromModifyDto(final Long id, final ModifyProductDto modifyProductDto) {
		return Product.builder()
				.id(id)
				.name(modifyProductDto.getName())
				.category(modifyProductDto.getCategory())
				.description(modifyProductDto.getDescription())
				.price(modifyProductDto.getPrice())
				.productType(modifyProductDto.getProductType())
				.build();
	}
	
	public static ModifyProductDto createModifyProductDtoWithNulls() {
		return ModifyProductDto.builder().build();
	}
	
	public static ModifyProductDto createModifyProductDto() {
		return ModifyProductDto.builder()
				.name(createRandomString())
				.category(createRandomString())
				.description(createRandomString())
				.price(createRandomBigDecimal())
				.productType(createRandomString())
				.build();
	}
	
	public static void assertProduct(final ProductDto result, final ModifyProductDto modifyProductDto) {
		assertThat(result.getName()).isEqualTo(modifyProductDto.getName());
		assertThat(result.getCategory()).isEqualTo(modifyProductDto.getCategory());
		assertThat(result.getDescription()).isEqualTo(modifyProductDto.getDescription());
		assertThat(result.getPrice()).isEqualTo(modifyProductDto.getPrice());
		assertThat(result.getProductType()).isEqualTo(modifyProductDto.getProductType());
	}
}
