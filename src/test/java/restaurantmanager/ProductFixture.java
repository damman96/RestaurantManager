package restaurantmanager;

import static restaurantmanager.RandomUtilsFixture.createRandomLong;
import static restaurantmanager.RandomUtilsFixture.createRandomString;

import java.math.BigDecimal;

import restaurantmanager.product.ModifyProductDto;
import restaurantmanager.product.Product;

public class ProductFixture {
	
	private static final int SCALE = 2;
	
	public static Product createProductWithNulls() {
		return Product.builder().build();
	}
	
	public static Product createProductEntity(final Long id, final String productType) {
		return Product.builder()
				.id(id)
				.name(createRandomString())
				.category(createRandomString())
				.description(createRandomString())
				.price(BigDecimal.valueOf(createRandomLong(), SCALE))
				.productType(productType)
				.build();
	}
	
	public static Product createProductEntity(final Long id,
											  final String name,
											  final String category,
											  final String description,
											  final BigDecimal price,
											  final String productType) {
		return Product.builder()
				.id(id)
				.name(name)
				.category(category)
				.description(description)
				.price(price)
				.productType(productType)
				.build();
	}
	
	public static ModifyProductDto createModifyProductDtoWithNulls() {
		return ModifyProductDto.builder().build();
	}
	
	public static ModifyProductDto createModifyProductDto(final String productType) {
		return ModifyProductDto.builder()
				.name(createRandomString())
				.category(createRandomString())
				.description(createRandomString())
				.price(BigDecimal.valueOf(createRandomLong(), SCALE))
				.productType(productType)
				.build();
	}
}
