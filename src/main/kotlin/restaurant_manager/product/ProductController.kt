package restaurant_manager.product

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(private val applicationService: ProductApplicationService) {

    @GetMapping
    fun getProducts(): ResponseEntity<List<ProductDetailDto>> = ResponseEntity.ok(applicationService.getProducts())

    @GetMapping("/{productId}")
    fun getProductById(@PathVariable productId: Long): ResponseEntity<ProductDetailDto> =
            ResponseEntity.ok(applicationService.getProductById(productId))

    @PostMapping
    fun addProduct(@RequestBody productAddDto: ProductAddDto): ResponseEntity<String> =
            ResponseEntity.ok(applicationService.addProduct(productAddDto))

    @PutMapping("/edit/{productId}")
    fun editProduct(@RequestBody productEditDto: ProductEditDto, @PathVariable productId: Long): ResponseEntity<String> =
            ResponseEntity.ok(applicationService.editProduct(productEditDto, productId))
}
