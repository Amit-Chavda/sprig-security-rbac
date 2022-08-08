# sprig-security-rbac
Demonstration of role based authentication and authorization using spring security and jwt

### Database
![RBAC drawio](https://user-images.githubusercontent.com/47694676/183420771-756b6fc3-100f-48e3-8153-1e3f8e53987b.png)

### Secured Product API
```
@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/findAll")
    @PreAuthorize(value = "hasAnyRole('ROLE_VIEWER','ROLE_ADMIN','ROLE_EDITOR','ROLE_CREATOR')")
    public List<Product> findAllProduct() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_VIEWER','ROLE_ADMIN','ROLE_EDITOR','ROLE_CREATOR')")
    public Product findProductById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping("/create")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_CREATOR')")
    public ResponseEntity<Product> createNewProduct(@RequestBody Product product) {
        product = productService.saveProduct(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/edit")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_EDITOR')")
    public Product editProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public String deleteProductByID(@PathVariable long id) {
        productService.removeById(id);
        return "success";
    }
}
```
