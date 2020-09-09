package restaurant_manager.product

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import restaurant_manager.exceptions.BadRequestException
import restaurant_manager.exceptions.NotFoundException
import restaurant_manager.infrastructure.ProductDAO
import java.math.BigDecimal
import java.util.stream.Collectors

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTestIT(
        @Autowired private val restTemplate: TestRestTemplate,
        @Autowired private val productDAO: ProductDAO,
        @Autowired private val productRepository: ProductRepository) {

    @LocalServerPort
    private val port = 0

    private val headers = HttpHeaders()
    private val request: HttpEntity<*> = HttpEntity<Any>(headers)

    companion object {
        private const val ROOT_URL = "http://localhost:"
        private const val API_PATH = "/products/"
        private const val EDIT = "edit/"
    }

    @BeforeAll
    fun beforeAll() {
        headers["Content-Type"] = "application/json"
    }

    @Test
    fun getProducts_Should_ReturnListOfProductDetailDto_When_ProductsExist() {

        val result: ResponseEntity<List<ProductDetailDto>> = restTemplate.exchange(
                ROOT_URL + port + API_PATH,
                HttpMethod.GET,
                request,
                object : ParameterizedTypeReference<List<ProductDetailDto>>() {})

        val expectedResult = productDAO.findAllByOrderByIdAsc()
                .stream()
                .map(productRepository::mapProductToProductDetailDto)
                .collect(Collectors.toList())

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertEquals(expectedResult.size, result.body!!.size)
    }

    @Test
    fun getProductById_Should_ReturnProductDetailDto_When_ProductWithGivenIdExists() {
        val productId = 1L

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH + productId,
                HttpMethod.GET,
                request,
                ProductDetailDto::class.java)

        val expectedResult = productDAO.findById(productId)
                .map(productRepository::mapProductToProductDetailDto)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertFalse(expectedResult.isEmpty)
        assertEquals(expectedResult.get().toString(), result.body.toString())
    }

    @Test
    fun getProductById_Should_ThrowNotFoundException_When_ProductWithGivenIdDoesNotExist() {
        val productId = 99L

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH + productId,
                HttpMethod.GET,
                request,
                NotFoundException::class.java)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertNotNull(result.body)
        assertTrue(result.body!!.message!!.contains("Product with id: $productId was not found!"))
    }

    @Test
    fun addProduct_Should_ReturnProductWasAddedString_When_CorrectInputParameters() {

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH,
                HttpMethod.POST,
                HttpEntity(productAddDto, headers),
                String::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertEquals("Product was added!", result.body)
    }

    @Test
    fun addProduct_Should_ThrowBadRequestException_When_IncorrectInputParameters() {

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH,
                HttpMethod.POST,
                HttpEntity(ProductAddDto(), headers),
                BadRequestException::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
        assertNotNull(result.body)
        assertTrue(result.body!!.message!!.contains("addProduct() Wrong input parameters for dto = ${ProductAddDto()}"))
    }

    @Test
    fun editProduct_Should_ReturnProductWasEditedString_When_CorrectInputParameters() {
        val productId = 1L

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH + EDIT + productId,
                HttpMethod.PUT,
                HttpEntity(productEditDto, headers),
                String::class.java)

        val expectedResult = productDAO.findById(1L)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertEquals("Product was edited!", result.body)
        assertFalse(expectedResult.isEmpty)
        assertEquals(expectedResult.get().name, this.productEditDto.name)
        assertEquals(expectedResult.get().description, this.productEditDto.description)
        assertEquals(expectedResult.get().category.category, this.productEditDto.category)
        assertEquals(expectedResult.get().subcategory.subcategory, this.productEditDto.subcategory)
    }

    @Test
    fun editProduct_Should_ThrowBadRequestException_When_IncorrectInputParameters() {
        val productId = 1L

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH + EDIT + productId,
                HttpMethod.PUT,
                HttpEntity(ProductEditDto(), headers),
                BadRequestException::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
        assertNotNull(result.body)
        assertTrue(result.body!!.message!!.contains("editProduct() Wrong input parameters for dto = ${ProductEditDto()}"))
    }

    @Test
    fun editProduct_Should_ThrowNotFoundException_When_NotExistingProductId() {
        val productId = 99L

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH + EDIT + productId,
                HttpMethod.PUT,
                HttpEntity(productEditDto, headers),
                NotFoundException::class.java)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertNotNull(result.body)
        assertTrue(result.body!!.message!!.contains("Product with id: $productId was not found!"))
    }

    private val productAddDto: ProductAddDto
        get() = ProductAddDto(
                name = "TestName",
                description = "TestDescription",
                price = BigDecimal.ZERO,
                category = "Empty",
                subcategory = "Empty"
        )

    private val productEditDto: ProductEditDto
        get() = ProductEditDto(
                name = "TestName",
                description = "TestDescription",
                price = BigDecimal.ZERO,
                category = "Empty",
                subcategory = "Empty"
        )
}
