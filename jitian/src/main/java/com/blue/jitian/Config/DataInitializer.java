package com.blue.jitian.Config;

import com.blue.jitian.Entity.Shop;
import com.blue.jitian.Entity.ShopCategory;
import com.blue.jitian.Entity.Product;
import com.blue.jitian.Entity.ProductCategory;
import com.blue.jitian.Service.ShopService;
import com.blue.jitian.Service.ShopCategoryService;
import com.blue.jitian.Service.ProductService;
import com.blue.jitian.Service.ProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (shopCategoryService.countTotal() > 0) {
            log.info("Data already initialized, skipping...");
            return;
        }

        log.info("Initializing test data...");

        // Create shop categories
        String[] categoryNames = {"美食", "甜点饮品", "超市便利", "水果生鲜", "早餐", "夜宵", "火锅烧烤", "快餐简餐"};
        String[] categoryIcons = {"food", "drink", "shop", "fruit", "breakfast", "night", "hotpot", "fastfood"};

        for (int i = 0; i < categoryNames.length; i++) {
            ShopCategory category = ShopCategory.builder()
                    .categoryName(categoryNames[i])
                    .icon(categoryIcons[i])
                    .sortOrder(i + 1)
                    .status(1)
                    .build();
            shopCategoryService.addCategory(category);
        }
        log.info("Created {} shop categories", categoryNames.length);

        // Create shops
        String[][] shopsData = {
            {"1", "老四川火锅", "正宗重庆老火锅，麻辣鲜香", "重庆市", "渝北区", "龙溪街道", "金龙路123号", "106.5088", "29.5647", "4.8", "1580"},
            {"1", "张记串串香", "成都味道，串串飘香", "重庆市", "渝北区", "回兴街道", "双龙大道456号", "106.5188", "29.5747", "4.6", "980"},
            {"2", "蜜雪冰城", "你爱我我爱你，蜜雪冰城甜蜜蜜", "重庆市", "渝北区", "龙溪街道", "红锦大道78号", "106.5028", "29.5587", "4.5", "2350"},
            {"2", "茶百道", "鲜果与茶，美味相融", "重庆市", "渝北区", "龙溪街道", "松石北路90号", "106.5128", "29.5687", "4.7", "1890"},
            {"3", "华润万家", "品质生活，华润万家", "重庆市", "渝北区", "龙溪街道", "金开大道100号", "106.5058", "29.5617", "4.4", "560"},
            {"4", "百果园", "好吃的水果，百果园", "重庆市", "渝北区", "回兴街道", "双凤路200号", "106.5218", "29.5777", "4.9", "1230"},
            {"5", "德克士", "脆皮炸鸡，美味早餐", "重庆市", "渝北区", "龙溪街道", "红土地路88号", "106.5068", "29.5627", "4.3", "890"},
            {"6", "楠火锅", "深夜食堂，暖心暖胃", "重庆市", "渝北区", "龙溪街道", "松牌路66号", "106.5098", "29.5657", "4.7", "760"},
            {"7", "海底捞火锅", "服务至上，火锅传奇", "重庆市", "渝北区", "龙溪街道", "新南路168号", "106.5118", "29.5677", "4.9", "3200"},
            {"8", "乡村基", "中式快餐，家的味道", "重庆市", "渝北区", "回兴街道", "双湖路300号", "106.5158", "29.5717", "4.5", "1450"},
        };

        for (String[] data : shopsData) {
            Shop shop = Shop.builder()
                    .categoryId(Integer.parseInt(data[0]))
                    .shopName(data[1])
                    .description(data[2])
                    .province(data[3])
                    .city(data[4])
                    .district(data[5])
                    .address(data[6])
                    .longitude(new BigDecimal(data[7]))
                    .latitude(new BigDecimal(data[8]))
                    .phone("023-88888888")
                    .deliveryTime(30)
                    .minOrderAmount(new BigDecimal("15.00"))
                    .deliveryFee(new BigDecimal("3.00"))
                    .packingFee(new BigDecimal("1.00"))
                    .rating(new BigDecimal(data[9]))
                    .salesCount(Integer.parseInt(data[10]))
                    .status(1)
                    .isAuth(1)
                    .build();
            shopService.addShop(shop);
        }
        log.info("Created {} shops", shopsData.length);

        // Create product categories for the first shop (fire pot)
        List<Shop> shops = shopService.getBusinessShops();
        if (!shops.isEmpty()) {
            Long shopId = shops.get(0).getShopId();
            
            String[] productCategoryNames = {"招牌锅底", "精品肉类", "特色菜品", "饮品小吃"};
            for (int i = 0; i < productCategoryNames.length; i++) {
                ProductCategory pc = ProductCategory.builder()
                        .shop_id(shopId)
                        .category_name(productCategoryNames[i])
                        .sort_order(i + 1)
                        .build();
                productCategoryService.addCategory(pc);
            }
            log.info("Created product categories for shop {}", shopId);

            // Create products
            List<ProductCategory> productCategories = productCategoryService.getCategoriesByShopId(shopId);
            if (!productCategories.isEmpty()) {
                Long categoryId = productCategories.get(0).getCategory_id();
                
                String[][] productsData = {
                    {"牛油麻辣锅底", "经典重庆味道，麻辣鲜香", "68.00", "520"},
                    {"清汤菌菇锅底", "养生菌菇，鲜美可口", "58.00", "380"},
                    {"鸳鸯锅底", "一锅两味，满足全家", "78.00", "450"},
                };

                for (String[] data : productsData) {
                    Product product = Product.builder()
                            .shop_id(shopId)
                            .category_id(categoryId)
                            .product_name(data[0])
                            .description(data[1])
                            .price(new BigDecimal(data[2]))
                            .original_price(new BigDecimal(data[2]).add(new BigDecimal("10")))
                            .stock(-1)
                            .sales_count(Integer.parseInt(data[3]))
                            .status(1)
                            .sort_order(0)
                            .build();
                    productService.addProduct(product);
                }
                log.info("Created products for shop {}", shopId);
            }
        }

        log.info("Test data initialization completed!");
    }
}
