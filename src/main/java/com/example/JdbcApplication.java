package com.example;

import com.example.dao.ProductDao;
import com.example.dao.impl.ProductDaoImpl;
import com.example.domain.CategorizedProduct;
import com.example.domain.Product;
import com.example.mapper.CategorizedProductRowMapper;
import com.example.mapper.ProductRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class JdbcApplication {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcApplication.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(JdbcApplication.class, args);

        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);

        // DataSource dataSource = context.getBean(DataSource.class);
        jdbcTemplate.getDataSource(); // Base de datos


        createProductsTable(jdbcTemplate);
        insertProducts(jdbcTemplate);

        // Get numero de productos
        final Long numProducts = count(jdbcTemplate);
        LOG.info("Número de productos: {} ", numProducts);

        // Guardar un Producto
        final Product newProduct = new Product(4L, "Hat-VS", "gentleman's hat", new BigDecimal(100.99));
        boolean isStored = save(jdbcTemplate, newProduct);
        LOG.info("Producto guardado?: {} ", isStored);

        // Búsqueda de productos:
        final Product productId1 = findById(jdbcTemplate, 1L);
        LOG.info("findById {}", productId1);

        final List<Product> products = findAll(jdbcTemplate);
        LOG.info("findAll {} ", products);

        final List<Product> productsFiltered = findAllByDescriptionContains(jdbcTemplate, "mobile");
        LOG.info("findAllByDescription {} ", productsFiltered);

        // Actualizar un producto:
        final Product productUpdate = new Product(1L, "iPhone", "Apple mobile(Edited)", new BigDecimal("1466.50"));
        boolean isUpdated = update(jdbcTemplate, productUpdate);
        LOG.info("iPhone isUpdated: {} ", isUpdated);

        // Borrar producto:
        boolean isDeleted = deleteById(jdbcTemplate, 1L);
        LOG.info("iPhone isDeleted: {} ", isDeleted);

        // Uso de otras clases:
        ProductDao productDao = context.getBean(ProductDaoImpl.class);

        createTableWithAutoIncrement(jdbcTemplate);
        List<Product> productsToInsert = List.of(
                new Product(null, "Product1", "Description1", new BigDecimal("102.88")),
                new Product(null, "Product2", "Description2", new BigDecimal("102.88")),
                new Product(null, "Product3", "Description3", new BigDecimal("102.88"))
        );
        productDao.initializeBatch(productsToInsert);


        // Find by nameMap:
        List<Product> res = productDao.findByNameMap("Product3");
        if (res.size() != 1)
            throw new Error("The expected results have not been obtained");
        LOG.info("Product3 found: {} ", res);

        // Find by nameBean:
        List<Product> resFindByNameBean = productDao.findByNameBean(new Product(null, "Product2", null, null));
        if (resFindByNameBean.size() != 1)
            throw new Error("The expected results have not been obtained");
        LOG.info("Product2 found: {} ", resFindByNameBean);

        productDao.initialize();
        final List<Product> productsInsertBySimpleJdbc = findAll(jdbcTemplate);
        LOG.info("findAll {} ", productsInsertBySimpleJdbc);

        // ManyToOne:
        createTableWithAutoIncrement(jdbcTemplate);
        insertProducts(jdbcTemplate);
        createTableOneToManyAutoIncrement(jdbcTemplate);
        assignsCategories(jdbcTemplate);

        final List<CategorizedProduct> categorizedProducts = findAllCategorizedProducts(jdbcTemplate); //
        LOG.info("findAll categorized products: {} ", categorizedProducts);

    }

    private static List<CategorizedProduct> findAllCategorizedProducts(JdbcTemplate jdbcTemplate) {
        final String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, new CategorizedProductRowMapper(jdbcTemplate));
    }

    private static void createTableOneToManyAutoIncrement(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("DROP TABLE IF EXISTS categories;");
        jdbcTemplate.execute("""
                CREATE TABLE categories (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    category_name VARCHAR(255),
                    product_id INT
                );
                """);
    }
    private static void assignsCategories(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("INSERT INTO categories VALUES (?, ?, ?)", 1L, "Mobile", 1L);
        jdbcTemplate.update("INSERT INTO categories VALUES (?, ?, ?)", 2L, "Mobile", 2L);
        jdbcTemplate.update("INSERT INTO categories VALUES (?, ?, ?)", 3L, "Wear", 3L);
        jdbcTemplate.update("INSERT INTO categories VALUES (?, ?, ?)", 4L, "Technology", 1L);
    }
    private static void createTableWithAutoIncrement(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("DROP TABLE IF EXISTS products;");
        jdbcTemplate.execute("""
                CREATE TABLE products (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(255),
                    description VARCHAR(255),
                    price DECIMAL 
                );
                """);
    }

    public static void createProductsTable(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("DROP TABLE IF EXISTS products;");
        jdbcTemplate.execute("""
                CREATE TABLE products (
                    id INT PRIMARY KEY,
                    name VARCHAR(255),
                    description VARCHAR(255),
                    price DECIMAL 
                );
                """);
    }

    private static void insertProducts(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("INSERT INTO products VALUES (?, ?, ?, ?)", 1L, "iPhone", "Apple mobile", new BigDecimal("944.50"));
        jdbcTemplate.update("INSERT INTO products VALUES (?, ?, ?, ?)", 2L, "OnePlus 10", "Android mobile", new BigDecimal("600.29"));
        jdbcTemplate.update("INSERT INTO products VALUES (?, ?, ?, ?)", 3L, "t-shirt-XS", "basketball t-shirt", new BigDecimal("944.50"));
    }

    private static boolean save(JdbcTemplate jdbcTemplate, Product product) {
        String sql = "INSERT INTO products VALUES (?, ?, ?, ?);";
        return jdbcTemplate.update(sql, product.id(), product.name(), product.description(), product.price()) > 0;
    }

    private static boolean update(JdbcTemplate jdbcTemplate, Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ? WHERE id = ?";
        return jdbcTemplate.update(sql, product.name(), product.description(), product.price(), product.id()) > 0;
    }

    private static boolean deleteById(JdbcTemplate jdbcTemplate, Long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    private static Long count(JdbcTemplate jdbcTemplate) {
        String sql = "SELECT COUNT(*) FROM products;";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    private static Product findById(JdbcTemplate jdbcTemplate, long id) {
        final String sql = "SELECT * FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new ProductRowMapper(), id);
    }

    private static List<Product> findAll(JdbcTemplate jdbcTemplate) {
        final String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, new ProductRowMapper());
    }

    private static List<Product> findAllByDescriptionContains(JdbcTemplate jdbcTemplate, String description) {
        final String sql = "SELECT * FROM products WHERE description LIKE '%" + description + "%'";
        return jdbcTemplate.query(sql, new ProductRowMapper());
    }
}
