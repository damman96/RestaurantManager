package restaurantmanager.product;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import restaurantmanager.NotFoundException;

@Slf4j
@Service
public class ProductService {
	
	private final ProductDao productDao;
	
	public ProductService(final ProductDao productDao) {
		this.productDao = productDao;
	}
	
	List<ProductDto> getAllProducts() {
		final var products = this.productDao.findAll()
				.stream()
				.map(ProductMapper.INSTANCE::map)
				.collect(toUnmodifiableList());
		log.info("Received products={}", products);
		return products;
	}
	
	List<ProductDto> getAllProductsByCategory(final String category) {
		final var products = this.productDao.findAllByCategoryIsIgnoreCase(category)
				.stream()
				.map(ProductMapper.INSTANCE::map)
				.collect(toUnmodifiableList());
		log.info("Received products={} by category={}", products, category);
		return products;
	}
	
	List<ProductDto> getAllProductsByProductType(final String productType) {
		final var products = this.productDao.findAllByProductTypeIsIgnoreCase(productType)
				.stream()
				.map(ProductMapper.INSTANCE::map)
				.collect(toUnmodifiableList());
		log.info("Received products={} by productType={}", products, productType);
		return products;
	}
	
	ProductDto getProductById(final Long id) {
		final var receivedProduct = ProductMapper.INSTANCE.map(this.getEntityById(id));
		log.info("Received product={}", receivedProduct);
		return receivedProduct;
	}
	
	ProductDto addProduct(final ModifyProductDto modifyProductDto) {
		final var savedProduct = this.productDao.save(ProductMapper.INSTANCE.mapFromModify(modifyProductDto));
		log.info("Saved product={}", savedProduct);
		return ProductMapper.INSTANCE.map(savedProduct);
	}
	
	ProductDto updateProduct(final Long id, final ModifyProductDto modifyProductDto) {
		final var productFromDb = this.getEntityById(id);
		log.info("Received product={}", productFromDb);
		
		final var modifiedProduct = Product.builder()
				.id(productFromDb.getId())
				.name(modifyProductDto.getName())
				.category(modifyProductDto.getCategory())
				.description(modifyProductDto.getDescription())
				.price(modifyProductDto.getPrice())
				.productType(modifyProductDto.getProductType())
				.build();
		
		final var updatedProduct = ProductMapper.INSTANCE.map(this.productDao.save(modifiedProduct));
		log.info("Saved updatedProduct={}", updatedProduct);
		return updatedProduct;
	}
	
	ProductDto deleteProductById(final Long id) {
		final var removedProduct = this.getProductById(id);
		this.productDao.deleteById(removedProduct.getId());
		log.info("Removed product={}", removedProduct);
		return removedProduct;
	}
	
	private Product getEntityById(final Long id) {
		return this.productDao.findById(id)
				.orElseThrow(() -> new NotFoundException("Product with id=" + id + " not found"));
	}
}
