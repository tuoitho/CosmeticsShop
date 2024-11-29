//package com.cosmeticsellingwebsite.test;
//
//import com.cosmeticsellingwebsite.entity.*;
//import com.cosmeticsellingwebsite.enums.Gender;
//import com.cosmeticsellingwebsite.repository.*;
//import com.google.gson.*;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class initdata {
//    @Autowired
//    RoleRepository roleRepository;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private OrderRepository orderRepository;
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Autowired
//    private ProductStockRepository productStockRepository;
//
//    @PostConstruct
//    public void init() {
////        1	ADMIN
////        2	MANAGER
////       
////        3	CUSTOMER
//        Role role1 = new Role();
//        role1.setRoleName("ADMIN");
//        roleRepository.save(role1);
//        Role role2 = new Role();
//        role2.setRoleName("MANAGER");
//        roleRepository.save(role2);
//        
//        Role role4 = new Role();
//        role4.setRoleName("CUSTOMER");
//        roleRepository.save(role4);
//
//        Category category=new Category();
//        category.setCategoryName("Body");
//        categoryRepository.save(category);
//        Category category1=new Category();
//        category1.setCategoryName("Fragrance");
//        categoryRepository.save(category1);
//        Category category2=new Category();
//        category2.setCategoryName("Wellness");
//        categoryRepository.save(category2);
//        Category category3=new Category();
//        category3.setCategoryName("Skin");
//        categoryRepository.save(category3);
//        Category category4=new Category();
//        category4.setCategoryName("MakeUp");
//        categoryRepository.save(category4);
//        Category category5=new Category();
//        category5.setCategoryName("Hair");
//        categoryRepository.save(category5);
//
//
//        try {
//            // Đọc JSON từ file
//            JsonElement root;
//            root= JsonParser.parseReader(new InputStreamReader(new FileInputStream("D:\\Bodymini.json"), StandardCharsets.UTF_8));
//            parseJson(root);
////            root= JsonParser.parseReader(new InputStreamReader(new FileInputStream("D:\\BodyVietnamese.json"), StandardCharsets.UTF_8));
////            parseJson(root);
////            root = JsonParser.parseReader(new InputStreamReader(new FileInputStream("D:\\FragranceVietnamese.json"), StandardCharsets.UTF_8));
////            parseJson(root);
////            root = JsonParser.parseReader(new InputStreamReader(new FileInputStream("D:\\WellnessVietnamese.json"), StandardCharsets.UTF_8));
////            parseJson(root);
////            root = JsonParser.parseReader(new InputStreamReader(new FileInputStream("D:\\SkinVietnamese.json"), StandardCharsets.UTF_8));
////            parseJson(root);
////            root = JsonParser.parseReader(new InputStreamReader(new FileInputStream("D:\\MakeUpVietnamese.json"), StandardCharsets.UTF_8));
////            parseJson(root);
////            root = JsonParser.parseReader(new InputStreamReader(new FileInputStream("D:\\HairVietnamese.json"), StandardCharsets.UTF_8));
////            parseJson(root);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void parseJson(JsonElement element) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        List<Product> products = new ArrayList<>();
//        List<ProductStock> productStocks = new ArrayList<>();
//        JsonArray array = element.getAsJsonArray();
//
//        for (JsonElement arrayElement : array) {
//            JsonObject product = arrayElement.getAsJsonObject();
//            Product product1 = new Product();
//            product1.setProductCode(product.get("productCode").getAsString());
//            product1.setProductName(product.get("name").getAsString());
//            product1.setCost(product.get("cost").getAsDouble());
//            product1.setDescription(product.get("description").getAsString());
//            product1.setBrand(product.get("brand").getAsString());
//            product1.setIngredient(product.get("ingredient").getAsString());
//            product1.setHow_to_use(product.get("how_to_use").getAsString());
//            product1.setVolume(product.get("volume").getAsString());
//            product1.setOrigin(product.get("origin").getAsString());
//            product1.setImage(product.get("image").getAsString());
//            product1.setManufactureDate(LocalDate.parse(product.get("manufactureDate").getAsString(), formatter));
//            product1.setExpirationDate(LocalDate.parse(product.get("expirationDate").getAsString(), formatter));
//            product1.setCategory(categoryRepository.findByCategoryName(product.get("category").getAsString()));
//            products.add(product1);
//            ProductStock productStock = new ProductStock();
//            productStock.setProduct(product1);
//            productStock.setQuantity(100L);
//            productStocks.add(productStock);
//        }
//
//        // Lưu tất cả sản phẩm vào cơ sở dữ liệu
//        productRepository.saveAll(products);
//        productStockRepository.saveAll(productStocks);
//
//
//    }
//}
