package restaurant_manager.product

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import restaurant_manager.exceptions.BadRequestException
import restaurant_manager.exceptions.NotFoundException
import restaurant_manager.infrastructure.Product
import restaurant_manager.infrastructure.ProductDAO
import java.util.stream.Collectors

@Component
class ProductApplicationService(
        private val productDAO: ProductDAO,
        private val repository: ProductRepository) {

    private val LOGGER = LoggerFactory.getLogger(ProductApplicationService::class.java)

    fun getProducts(): List<ProductDetailDto> =
            productDAO.findAllByOrderByIdAsc()
                    .stream().map(repository::mapProductToProductDetailDto)
                    .collect(Collectors.toList())

    fun getProductById(productId: Long): ProductDetailDto =
            productDAO.findById(productId)
                    .map { product: Product -> repository.mapProductToProductDetailDto(product) }
                    .orElseThrow { throwNotFoundException(productId) }

    fun addProduct(dto: ProductAddDto): String =
            when {
                dto.isValid() -> saveNewProduct(dto)
                else -> this.throwBadRequestException(dto)
            }

    fun editProduct(dto: ProductEditDto, productId: Long): String =
            when {
                dto.isValid() -> saveEditedProduct(dto, productId)
                else -> this.throwBadRequestException(dto)
            }

    private fun throwNotFoundException(productId: Long): NotFoundException =
            NotFoundException("Product with id: $productId was not found!")

    private fun saveNewProduct(dto: ProductAddDto): String {
        productDAO.save(repository.mapProductToProductAddDto(dto))
        return "Product was added!"
    }

    private fun saveEditedProduct(dto: ProductEditDto, productId: Long): String {
        productDAO.findById(productId)
                .ifPresentOrElse({ product: Product -> productDAO.save(repository.mapProductEditDtoToProduct(product, dto)) },
                        { throw NotFoundException("Product with id: $productId was not found!") })
        return "Product was edited!"
    }

    private fun throwBadRequestException(dto: ProductAddDto): String {
        LOGGER.warn("addProduct() Wrong input parameters for dto = {}", dto)
        throw BadRequestException("addProduct() Wrong input parameters for dto = $dto")
    }

    private fun throwBadRequestException(dto: ProductEditDto): String {
        LOGGER.warn("editProduct() Wrong input parameters for dto = {}", dto)
        throw BadRequestException("editProduct() Wrong input parameters for dto = $dto")
    }
}
