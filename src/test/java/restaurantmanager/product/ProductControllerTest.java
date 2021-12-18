package restaurantmanager.product;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static restaurantmanager.utils.ProductFixture.createModifyProductDto;
import static restaurantmanager.utils.ProductFixture.createModifyProductDtoWithNulls;
import static restaurantmanager.utils.ProductFixture.createProductEntity;
import static restaurantmanager.utils.ProductFixture.createProductEntityWithNulls;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomLong;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomString;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerTest {
	
	private static final String HTTP_LOCAL_HOST = "http://localhost:";
	
	private static final String SLASH = "/";
	
	private static final String PRODUCTS = "products";
	private static final String UPDATE = "update";
	private static final String DELETE = "delete";
	
	private static final String BY_CATEGORY = "byCategory";
	private static final String BY_PRODUCT_TYPE = "byProductType";
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@LocalServerPort
	private int randomServerPort;
	
	@BeforeEach
	void setUp() {
		this.productDao.deleteAll();
	}
	
	@Test
	void getAllProducts_Should_ReturnStatusCode200AndEmptyList_When_DatabaseIsEmpty() {
		// given
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + PRODUCTS;
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				new ParameterizedTypeReference<List<ProductDto>>() {
				});
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull().isEmpty();
	}
	
	@Test
	void getAllProducts_Should_ReturnStatusCode200AndResultList_When_DatabaseIsNotEmpty() {
		//given
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + PRODUCTS;
		final var products = List.of(
				createProductEntityWithNulls(),
				createProductEntityWithNulls(),
				createProductEntityWithNulls());
		this.productDao.saveAll(products);
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				new ParameterizedTypeReference<List<ProductDto>>() {
				});
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull().isNotEmpty();
		assertThat(result.getBody()).usingRecursiveComparison()
				.isEqualTo(products.stream().map(ProductMapper.INSTANCE::map).collect(toUnmodifiableList()));
	}
	
	@Test
	void getAllProductsByCategory_Should_ReturnStatusCode200AndEmptyList_When_DatabaseIsEmpty() {
		// given
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + PRODUCTS + SLASH + BY_CATEGORY + SLASH + createRandomString();
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				new ParameterizedTypeReference<List<ProductDto>>() {
				});
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull().isEmpty();
	}
	
	@Test
	void getAllProductsByCategory_Should_ReturnStatusCode200AndResultList_When_DatabaseIsNotEmpty() {
		//given
		final var category = "category";
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + PRODUCTS + SLASH + BY_CATEGORY + SLASH + category;
		
		final var products = List.of(
				createProductEntity(1L, category, createRandomString()),
				createProductEntity(2L, category, createRandomString()),
				createProductEntity(3L, category, createRandomString()));
		this.productDao.saveAll(products);
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				new ParameterizedTypeReference<List<ProductDto>>() {
				});
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull().isNotEmpty();
		assertThat(result.getBody()).usingRecursiveComparison()
				.isEqualTo(products.stream().map(ProductMapper.INSTANCE::map).collect(toUnmodifiableList()));
	}
	
	@Test
	void getAllProductsByProductType_Should_ReturnStatusCode200AndEmptyList_When_DatabaseIsEmpty() {
		// given
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + PRODUCTS + SLASH + BY_PRODUCT_TYPE + SLASH + createRandomString();
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				new ParameterizedTypeReference<List<ProductDto>>() {
				});
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull().isEmpty();
	}
	
	@Test
	void getAllProductsByProductType_Should_ReturnStatusCode200AndResultList_When_DatabaseIsNotEmpty() {
		//given
		final var productType = "productType";
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + PRODUCTS + SLASH + BY_PRODUCT_TYPE + SLASH + productType;
		
		final var products = List.of(
				createProductEntity(1L, createRandomString(), productType),
				createProductEntity(2L, createRandomString(), productType),
				createProductEntity(3L, createRandomString(), productType));
		this.productDao.saveAll(products);
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				new ParameterizedTypeReference<List<ProductDto>>() {
				});
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull().isNotEmpty();
		assertThat(result.getBody()).usingRecursiveComparison()
				.isEqualTo(products.stream().map(ProductMapper.INSTANCE::map).collect(toUnmodifiableList()));
	}
	
	@Test
	void getProductById_Should_ReturnStatusCode200AndResult_When_EntityWithGivenIdExists() {
		// given
		final var saved = this.productDao.save(createProductEntityWithNulls());
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + PRODUCTS + SLASH + saved.getId();
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				ProductDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody())
				.isNotNull()
				.isEqualTo(ProductMapper.INSTANCE.map(saved));
	}
	
	@Test
	void getProductById_Should_ReturnStatusCode404_When_EntityWithGivenIdNotExist() {
		// given
		final var id = createRandomLong();
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + PRODUCTS + SLASH + id;
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				ProductDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	void addProduct_Should_ReturnStatusCode200AndResult_When_SuccessfullyAddedProduct() {
		// given
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + PRODUCTS;
		final var productToAdd = createModifyProductDtoWithNulls();
		final var body = new HttpEntity<>(productToAdd);
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.POST,
				body,
				ProductDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isNotNull().isPositive();
		assertThat(result.getBody().getName()).isEqualTo(productToAdd.getName());
		assertThat(result.getBody().getCategory()).isEqualTo(productToAdd.getCategory());
		assertThat(result.getBody().getDescription()).isEqualTo(productToAdd.getDescription());
		assertThat(result.getBody().getPrice()).isEqualTo(productToAdd.getPrice());
		assertThat(result.getBody().getProductType()).isEqualTo(productToAdd.getProductType());
	}
	
	@Test
	void updateProduct_Should_ReturnStatusCode200_When_SuccessfullyUpdatedEntity() {
		// given
		final var saved = this.productDao.save(createProductEntityWithNulls());
		final var modifyProductDto = createModifyProductDto("productType");
		
		final var body = new HttpEntity<>(modifyProductDto);
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + PRODUCTS + SLASH + UPDATE + SLASH + saved.getId();
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.PUT,
				body,
				ProductDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(saved.getId());
		assertThat(result.getBody().getName()).isEqualTo(modifyProductDto.getName());
		assertThat(result.getBody().getCategory()).isEqualTo(modifyProductDto.getCategory());
		assertThat(result.getBody().getDescription()).isEqualTo(modifyProductDto.getDescription());
		assertThat(result.getBody().getPrice()).isEqualTo(modifyProductDto.getPrice());
		assertThat(result.getBody().getProductType()).isEqualTo(modifyProductDto.getProductType());
	}
	
	@Test
	void updateProduct_Should_ReturnStatusCode404_When_EntityWithGivenIdNotExist() {
		// given
		final var id = createRandomLong();
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + PRODUCTS + SLASH + UPDATE + SLASH + id;
		final var body = new HttpEntity<>(createModifyProductDtoWithNulls());
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.PUT,
				body,
				ProductDto.class);
		
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	void deleteProductById_Should_ReturnStatusCode200AndResult_When_EntityWithGivenIdWasDeleted() {
		// given
		final var saved = this.productDao.save(createProductEntityWithNulls());
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + PRODUCTS + SLASH + DELETE + SLASH + saved.getId();
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.DELETE,
				HttpEntity.EMPTY,
				ProductDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody())
				.isNotNull()
				.isEqualTo(ProductMapper.INSTANCE.map(saved));
	}
	
}