package restaurantmanager.product;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static restaurantmanager.ProductFixture.createModifyProductDto;
import static restaurantmanager.ProductFixture.createModifyProductDtoWithNulls;
import static restaurantmanager.ProductFixture.createProductEntity;
import static restaurantmanager.ProductFixture.createProductWithNulls;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import restaurantmanager.NotFoundException;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductServiceTest {
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ProductService productService;
	
	private static void assertEquals(final List<Product> productsToAdd, final List<ProductDto> result) {
		assertThat(result).isNotEmpty();
		assertThat(result).extracting(ProductDto::getId)
				.containsAll(productsToAdd.stream().map(Product::getId).collect(toUnmodifiableList()));
		assertThat(result).extracting(ProductDto::getName)
				.containsAll(productsToAdd.stream().map(Product::getName).collect(toUnmodifiableList()));
		assertThat(result).extracting(ProductDto::getCategory)
				.containsAll(productsToAdd.stream().map(Product::getCategory).collect(toUnmodifiableList()));
		assertThat(result).extracting(ProductDto::getDescription)
				.containsAll(productsToAdd.stream().map(Product::getDescription).collect(toUnmodifiableList()));
		assertThat(result).extracting(ProductDto::getPrice)
				.containsAll(productsToAdd.stream().map(Product::getPrice).collect(toUnmodifiableList()));
		assertThat(result).extracting(ProductDto::getProductType)
				.containsAll(productsToAdd.stream().map(Product::getProductType).collect(toUnmodifiableList()));
	}
	
	@BeforeEach
	void setUp() {
		this.productDao.deleteAll();
	}
	
	@Test
	void getAllProducts_Should_ReturnEmptyList_When_EntitiesAreNotPresentInDb() {
		// when
		final var result = this.productService.getAllProducts();
		
		// then
		assertThat(result).isEmpty();
	}
	
	@Test
	void getAllProducts_Should_ReturnResultList_When_EntitiesArePresentInDb() {
		// given
		final var productsToAdd = List.of(createProductEntity(1L, "testType"),
										  createProductEntity(2L, "testType"),
										  createProductEntity(3L, "testType"));
		this.productDao.saveAll(productsToAdd);
		
		// when
		final var result = this.productService.getAllProducts();
		
		// then
		assertEquals(productsToAdd, result);
	}
	
	@Test
	void getAllProductsByProductType_Should_ReturnEmptyList_When_EntitiesAreNotPresentInDb() {
		// when
		final var result = this.productService.getAllProductsByProductType("notPresentProductType");
		
		// then
		assertThat(result).isEmpty();
	}
	
	@Test
	void getAllProductsByProductType_Should_ReturnResultList_When_EntitiesArePresentInDb() {
		// given
		final var productType = "testType";
		final var productsToAdd = List.of(createProductEntity(1L, productType),
										  createProductEntity(2L, productType),
										  createProductEntity(3L, productType));
		this.productDao.saveAll(productsToAdd);
		
		// when
		final var result = this.productService.getAllProductsByProductType(productType);
		
		// then
		assertEquals(productsToAdd, result);
	}
	
	@Test
	void getProductById_Should_ReturnResult_When_EntityExists() {
		// given
		final var product = createProductEntity(1L, "productType");
		this.productDao.save(product);
		
		// when
		final var result = this.productService.getProductById(product.getId());
		
		// then
		assertThat(result).isEqualTo(ProductMapper.INSTANCE.map(product));
	}
	
	@Test
	void getProductById_Should_ThrowNotFoundException_When_EntityWithGivenIdNotExist() {
		// given
		final var id = new Random().nextLong();
		
		// when
		final var throwable = catchThrowable(() -> this.productService.getProductById(id));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Product with id=" + id + " not found");
	}
	
	@Test
	void addProduct_Should_SaveEntity() {
		// given
		final var productToAdd = createModifyProductDto("testType");
		
		// when
		final var result = this.productService.addProduct(productToAdd);
		
		// then
		assertThat(result.getId()).isNotNull().isPositive();
		assertThat(result.getName()).isEqualTo(productToAdd.getName());
		assertThat(result.getCategory()).isEqualTo(productToAdd.getCategory());
		assertThat(result.getDescription()).isEqualTo(productToAdd.getDescription());
		assertThat(result.getPrice()).isEqualTo(productToAdd.getPrice());
		assertThat(result.getProductType()).isEqualTo(productToAdd.getProductType());
	}
	
	@Test
	void updateProduct_Should_UpdateProduct() {
		// given
		final var existingProduct = createProductEntity(1L, "testType");
		this.productDao.save(existingProduct);
		
		final var modifyProductDto = createModifyProductDto("newTestType");
		
		// when
		final var result = this.productService.updateProduct(existingProduct.getId(), modifyProductDto);
		
		// then
		assertThat(result.getId()).isEqualTo(existingProduct.getId());
		assertThat(result.getName()).isEqualTo(modifyProductDto.getName());
		assertThat(result.getCategory()).isEqualTo(modifyProductDto.getCategory());
		assertThat(result.getDescription()).isEqualTo(modifyProductDto.getDescription());
		assertThat(result.getPrice()).isEqualTo(modifyProductDto.getPrice());
		assertThat(result.getProductType()).isEqualTo(modifyProductDto.getProductType());
	}
	
	@Test
	void updateProduct_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		
		// when
		final var throwable = catchThrowable(() -> this.productService.updateProduct(id, createModifyProductDtoWithNulls()));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Product with id=" + id + " not found");
	}
	
	@Test
	void deleteProductById_Should_RemoveEntity_When_EntityExists() {
		// given
		final var product = createProductWithNulls();
		this.productDao.save(product);
		
		// when
		this.productService.deleteProductById(product.getId());
		
		//then
		assertThat(this.productDao.findById(product.getId())).isNotPresent();
	}
	
	@Test
	void deleteProductById_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		
		// when
		final var throwable = catchThrowable(() -> this.productService.deleteProductById(id));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Product with id=" + id + " not found");
	}
}