package restaurantmanager.utils;

import static restaurantmanager.utils.RandomUtilsFixture.createRandomLong;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomString;

import java.math.BigDecimal;

import restaurantmanager.product.ModifyProductDto;
import restaurantmanager.product.Product;

public abstract class ProductFixture {
	
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
