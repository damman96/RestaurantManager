package restaurantmanager.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {
	
	List<Product> findAllByCategoryIsIgnoreCase(final String category);
	
	List<Product> findAllByProductTypeIsIgnoreCase(final String productType);
}
