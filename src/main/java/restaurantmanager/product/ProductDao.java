package restaurantmanager.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {
	
	List<Product> findAllByProductTypeIsIgnoreCase(final String productType);
}
