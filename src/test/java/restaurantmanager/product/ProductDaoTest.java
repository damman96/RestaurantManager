package restaurantmanager.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductDaoTest {
	
	@Autowired
	private ProductDao productDao;
	
	@BeforeEach
	void setUp() {
		this.productDao.deleteAll();
	}
	
	@Test
	void findAll_Should_ReturnEmptyResult_When_NoEntitiesArePresentInDb() {
		// when
		final var result = this.productDao.findAll();
		
		// then
		assertThat(result).isEmpty();
	}
	
	@Test
	void save_Should_SaveEntity() {
		// given
		final var productToAdd = Product.builder().build();
		
		// when
		final var result = this.productDao.save(productToAdd);
		
		// then
		assertThat(result).isEqualTo(productToAdd);
	}
	
	@Test
	void findById_Should_ReturnEntity_When_EntityIsPresent() {
		// given
		final var productToAdd = Product.builder().build();
		this.productDao.save(productToAdd);
		
		// when
		final var result = this.productDao.findById(productToAdd.getId());
		
		// then
		assertThat(result)
				.isPresent()
				.contains(productToAdd);
	}
	
	@Test
	void findById_Should_ReturnEmptyResult_When_EntityIsNotPresent() {
		// given
		final var notExistingId = new Random().nextLong();
		// when
		final var result = this.productDao.findById(notExistingId);
		
		// then
		assertThat(result)
				.isNotPresent();
	}
	
	@Test
	void deleteAll_Should_ReturnEmptyResult_When_AllEntitiesWasRemoved() {
		// given
		this.productDao.save(Product.builder().build());
		this.productDao.save(Product.builder().build());
		this.productDao.save(Product.builder().build());
		
		// when
		this.productDao.deleteAll();
		final var result = this.productDao.findAll();
		
		// then
		assertThat(result).isEmpty();
	}
}