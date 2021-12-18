package restaurantmanager.product;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	private final ProductService productService;
	
	public ProductController(final ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping
	public ResponseEntity<List<ProductDto>> getAllProducts() {
		return ResponseEntity.ok(this.productService.getAllProducts());
	}
	
	@GetMapping("/byCategory/{category}")
	public ResponseEntity<List<ProductDto>> getAllProductsByCategory(@PathVariable final String category) {
		return ResponseEntity.ok(this.productService.getAllProductsByCategory(category));
	}
	
	@GetMapping("/byProductType/{productType}")
	public ResponseEntity<List<ProductDto>> getAllProductsByProductType(@PathVariable final String productType) {
		return ResponseEntity.ok(this.productService.getAllProductsByProductType(productType));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable final Long id) {
		return ResponseEntity.ok(this.productService.getProductById(id));
	}
	
	@PostMapping
	public ResponseEntity<ProductDto> addProduct(@RequestBody final ModifyProductDto modifyProductDto) {
		return ResponseEntity.ok(this.productService.addProduct(modifyProductDto));
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<ProductDto> updateProduct(@PathVariable final Long id,
													@RequestBody final ModifyProductDto modifyProductDto) {
		return ResponseEntity.ok(this.productService.updateProduct(id, modifyProductDto));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ProductDto> deleteProductById(@PathVariable final Long id) {
		return ResponseEntity.ok(this.productService.deleteProductById(id));
		
	}
}
