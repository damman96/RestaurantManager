package restaurantmanager.product;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static restaurantmanager.utils.ProductFixture.assertProduct;
import static restaurantmanager.utils.ProductFixture.createModifyProductDto;
import static restaurantmanager.utils.ProductFixture.createModifyProductDtoWithNulls;
import static restaurantmanager.utils.ProductFixture.createProductEntity;
import static restaurantmanager.utils.ProductFixture.createProductEntityFromModifyDto;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomString;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import restaurantmanager.NotFoundException;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	
	private static final String CATEGORY = createRandomString();
	private static final String PRODUCT_TYPE = createRandomString();
	
	private final ProductDao productDao = Mockito.mock(ProductDao.class);
	
	@InjectMocks
	private ProductService productService;
	
	@BeforeEach
	void setUp() {
		this.productDao.deleteAll();
	}
	
	@Test
	void getAllProducts_Should_ReturnEmptyList_When_EntitiesAreNotPresentInDb() {
		// given
		when(this.productDao.findAll()).thenReturn(emptyList());
		
		// when
		final var result = this.productService.getAllProducts();
		
		// then
		assertThat(result).isEmpty();
	}
	
	@Test
	void getAllProducts_Should_ReturnResultList_When_EntitiesArePresentInDb() {
		// given
		final var productsToAdd = List.of(createProductEntity(1L),
										  createProductEntity(2L),
										  createProductEntity(3L));
		
		when(this.productDao.findAll()).thenReturn(productsToAdd);
		
		// when
		final var result = this.productService.getAllProducts();
		
		// then
		assertEquals(productsToAdd, result);
	}
	
	@Test
	void getAllProductsByCategory_Should_ReturnEmptyList_When_EntitiesAreNotPresentInDb() {
		// given
		when(this.productDao.findAllByCategoryIsIgnoreCase(CATEGORY)).thenReturn(emptyList());
		
		// when
		final var result = this.productService.getAllProductsByCategory(CATEGORY);
		
		// then
		assertThat(result).isEmpty();
	}
	
	@Test
	void getAllProductsByCategory_Should_ReturnResultList_When_EntitiesArePresentInDb() {
		// given
		final var productsToAdd = List.of(createProductEntity(1L, CATEGORY, PRODUCT_TYPE),
										  createProductEntity(2L, CATEGORY, PRODUCT_TYPE),
										  createProductEntity(3L, CATEGORY, PRODUCT_TYPE));
		
		when(this.productDao.findAllByCategoryIsIgnoreCase(CATEGORY)).thenReturn(productsToAdd);
		
		// when
		final var result = this.productService.getAllProductsByCategory(CATEGORY);
		
		// then
		assertEquals(productsToAdd, result);
	}
	
	@Test
	void getAllProductsByProductType_Should_ReturnEmptyList_When_EntitiesAreNotPresentInDb() {
		// given
		when(this.productDao.findAllByProductTypeIsIgnoreCase(PRODUCT_TYPE)).thenReturn(emptyList());
		
		// when
		final var result = this.productService.getAllProductsByProductType(PRODUCT_TYPE);
		
		// then
		assertThat(result).isEmpty();
	}
	
	@Test
	void getAllProductsByProductType_Should_ReturnResultList_When_EntitiesArePresentInDb() {
		// given
		final var productsToAdd = List.of(createProductEntity(1L, PRODUCT_TYPE),
										  createProductEntity(2L, PRODUCT_TYPE),
										  createProductEntity(3L, PRODUCT_TYPE));
		
		when(this.productDao.findAllByProductTypeIsIgnoreCase(PRODUCT_TYPE)).thenReturn(productsToAdd);
		
		// when
		final var result = this.productService.getAllProductsByProductType(PRODUCT_TYPE);
		
		// then
		assertEquals(productsToAdd, result);
	}
	
	@Test
	void getProductById_Should_ReturnResult_When_EntityExists() {
		// given
		final var id = 1L;
		final var product = createProductEntity(id);
		when(this.productDao.findById(id)).thenReturn(Optional.of(product));
		
		// when
		final var result = this.productService.getProductById(id);
		
		// then
		assertThat(result).isEqualTo(ProductMapper.INSTANCE.map(product));
	}
	
	@Test
	void getProductById_Should_ThrowNotFoundException_When_EntityWithGivenIdNotExist() {
		// given
		final var id = new Random().nextLong();
		when(this.productDao.findById(id)).thenReturn(Optional.empty());
		
		// when
		final var throwable = catchThrowable(() -> this.productService.getProductById(id));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class).hasMessage("Product with id=" + id + " not found");
	}
	
	@Test
	void addProduct_Should_SaveEntity() {
		// given
		final var id = 1L;
		final var productToAdd = createModifyProductDto();
		
		// when
		final var entity = createProductEntityFromModifyDto(id, productToAdd);
		
		when(this.productDao.save(ProductMapper.INSTANCE.mapFromModify(productToAdd))).thenReturn(entity);
		final var result = this.productService.addProduct(productToAdd);
		
		// then
		assertThat(result.getId()).isNotNull().isPositive();
		assertProduct(result, productToAdd);
	}
	
	@Test
	void updateProduct_Should_UpdateProduct() {
		// given
		final var id = 1L;
		final var existingProduct = createProductEntity(id);
		when(this.productDao.findById(id)).thenReturn(Optional.of(existingProduct));
		
		final var productToUpdate = createModifyProductDto();
		
		// when
		final var entity = createProductEntityFromModifyDto(id, productToUpdate);
		
		when(this.productDao.save(entity)).thenReturn(entity);
		final var result = this.productService.updateProduct(id, productToUpdate);
		
		// then
		assertThat(result.getId()).isEqualTo(id);
		assertProduct(result, productToUpdate);
	}
	
	@Test
	void updateProduct_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		when(this.productDao.findById(id)).thenReturn(Optional.empty());
		
		// when
		final var throwable = catchThrowable(() -> this.productService.updateProduct(id, createModifyProductDtoWithNulls()));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class).hasMessage("Product with id=" + id + " not found");
	}
	
	@Test
	void deleteProductById_Should_RemoveEntity_When_EntityExists() {
		// given
		final var id = 1L;
		final var product = createProductEntity(id);
		when(this.productDao.findById(id)).thenReturn(Optional.of(product));
		
		// when
		this.productService.deleteProductById(id);
		
		//then
		verify(this.productDao, times(1)).deleteById(id);
	}
	
	@Test
	void deleteProductById_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		when(this.productDao.findById(id)).thenReturn(Optional.empty());
		
		// when
		final var throwable = catchThrowable(() -> this.productService.deleteProductById(id));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class).hasMessage("Product with id=" + id + " not found");
	}
	
	private static void assertEquals(final List<Product> productsToAdd, final List<ProductDto> result) {
		assertThat(result).isNotEmpty();
		assertThat(result).extracting(ProductDto::getId).containsAll(productsToAdd.stream().map(Product::getId).collect(toUnmodifiableList()));
		assertThat(result).extracting(ProductDto::getName).containsAll(productsToAdd.stream().map(Product::getName).collect(toUnmodifiableList()));
		assertThat(result).extracting(ProductDto::getCategory).containsAll(productsToAdd.stream().map(Product::getCategory).collect(toUnmodifiableList()));
		assertThat(result).extracting(ProductDto::getDescription).containsAll(productsToAdd.stream().map(Product::getDescription).collect(toUnmodifiableList()));
		assertThat(result).extracting(ProductDto::getPrice).containsAll(productsToAdd.stream().map(Product::getPrice).collect(toUnmodifiableList()));
		assertThat(result).extracting(ProductDto::getProductType).containsAll(productsToAdd.stream().map(Product::getProductType).collect(toUnmodifiableList()));
	}
}
