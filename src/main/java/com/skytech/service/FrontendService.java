package com.skytech.service;

import com.skytech.configuration.AwsConfiguration;
import com.skytech.dto.BannerResponse;
import com.skytech.dto.FrontendProductDetailDto;
import com.skytech.dto.FrontendProductDto;
import com.skytech.dto.InquiryRequest;
import com.skytech.entity.*;
import com.skytech.error.CustomException;
import com.skytech.repository.CategoryRepository;
import com.skytech.repository.ProductRepository;
import com.skytech.repository.TopMarqueeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class FrontendService {

    @Autowired
    TopMarqueeRepository topMarqueeRepository;

    @Autowired
    BannerService bannerService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    InquiryService inquiryService;

    @Autowired
    AwsConfiguration awsConfiguration;

    public List<TopMarquee> getAllTopMarquee() {
        return topMarqueeRepository.findByIsActiveTrueOrderByCreatedDateTimeDesc();
    }

    public List<BannerResponse> getAllBanner() {
        return bannerService.getActive();
    }

    public Inquiry saveInquiry(InquiryRequest request) {
        return inquiryService.create(request);
    }

    public List<Category> getAllCategory() {
        List<Category> categories = categoryRepository.findAllByOrderByIdAsc();
        for (Category cat : categories) {
            if (cat.getImageUrl() != null && !cat.getImageUrl().isBlank()) {
                cat.setImageUrl(resolveImageUrl(cat.getImageUrl()));
            }
        }
        return categories;
    }


    public List<FrontendProductDto> getTop8Products() {
        return productRepository.findTop8ByStatusAndCategory().stream().map(product -> {
            ProductPricing pricing = product.getPricing();

            FrontendProductDto frontendProductDto = new FrontendProductDto();
            frontendProductDto.setId(product.getId());
            frontendProductDto.setName(product.getName());
            frontendProductDto.setCategory(product.getCategory() != null ? product.getCategory().getName() : "laptops");
            frontendProductDto.setBrand(product.getBrand() != null ? product.getBrand().getName() : null);
            frontendProductDto.setModel(product.getModel());
            frontendProductDto.setPrice(pricing != null ? pricing.getFinalPrice() != null
                    ? pricing.getFinalPrice() : pricing.getSellingPrice() != null
                    ? pricing.getSellingPrice() : BigDecimal.ZERO
                    : BigDecimal.ZERO);
            frontendProductDto.setOriginalPrice(pricing != null ? pricing.getOriginalPrice() : null);
            frontendProductDto.setSellingPrice(pricing != null ? pricing.getSellingPrice() : null);
            frontendProductDto.setDiscountPercentage(pricing != null ? pricing.getDiscountPercentage() : null);
            frontendProductDto.setImage(resolveImageUrl(product.getFeaturedImageUrl()));
            return frontendProductDto;
        }).toList();
    }

    public FrontendProductDetailDto getProductDetailById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new CustomException("id", "Product not found with id: " + id));
        return toProductDetailDto(product);
    }

    private FrontendProductDetailDto toProductDetailDto(Product product) {
        BigDecimal price = BigDecimal.ZERO;
        BigDecimal originalPrice = null;
        BigDecimal sellingPrice = null;
        BigDecimal discountPercentage = null;

        if (product.getPricing() != null) {
            ProductPricing pricing = product.getPricing();
            originalPrice = pricing.getOriginalPrice();
            sellingPrice = pricing.getSellingPrice();
            discountPercentage = pricing.getDiscountPercentage();
            price = pricing.getFinalPrice() != null ? pricing.getFinalPrice() : pricing.getSellingPrice();
        }

        List<String> images = new ArrayList<>();
        if (product.getFeaturedImageUrl() != null && !product.getFeaturedImageUrl().isBlank()) {
            images.add(resolveImageUrl(product.getFeaturedImageUrl()));
        }
        if (product.getGalleryImages() != null) {
            for (ProductImage img : product.getGalleryImages()) {
                if (img.getImageUrl() != null && !img.getImageUrl().isBlank()) {
                    images.add(resolveImageUrl(img.getImageUrl()));
                }
            }
        }

        String processor = null;
        String storage = null;
        String ram = null;
        String screen = null;
        String graphic = null;
        String os = null;
        String connection = null;
        List<String> specs = new ArrayList<>();

        if (product.getSpecifications() != null) {
            for (ProductSpecification spec : product.getSpecifications()) {
                String key = spec.getSpecKey() != null ? spec.getSpecKey().trim() : "";
                String val = spec.getSpecValue() != null ? spec.getSpecValue().trim() : "";
                if (!val.isEmpty()) {
                    specs.add(key.isEmpty() ? val : key + ": " + val);

                    String lowerKey = key.toLowerCase();
                    if (lowerKey.contains("processor") || lowerKey.contains("cpu"))
                        processor = val;
                    else if (lowerKey.contains("storage") || lowerKey.contains("ssd") || lowerKey.contains("hdd"))
                        storage = val;
                    else if (lowerKey.contains("ram") || lowerKey.contains("memory"))
                        ram = val;
                    else if (lowerKey.contains("screen") || lowerKey.contains("display"))
                        screen = val;
                    else if (lowerKey.contains("graphic") || lowerKey.contains("gpu"))
                        graphic = val;
                    else if (lowerKey.contains("os") || lowerKey.contains("operating system"))
                        os = val;
                    else if (lowerKey.contains("connection") || lowerKey.contains("port")
                            || lowerKey.contains("connectivity"))
                        connection = val;
                }
            }
        }

        boolean inStock = product.getInventory() == null || Boolean.TRUE.equals(product.getInventory().getIsInStock());
        int quantity = product.getInventory() != null && product.getInventory().getQuantity() != null
                ? product.getInventory().getQuantity()
                : 0;

        return FrontendProductDetailDto.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .category(product.getCategory() != null ? product.getCategory().getName() : "laptops")
                .brand(product.getBrand() != null ? product.getBrand().getName() : null)
                .model(product.getModel())
                .sku(product.getSku())
                .condition("Certified Refurbished")
                .price(price)
                .originalPrice(originalPrice)
                .sellingPrice(sellingPrice)
                .discountPercentage(discountPercentage)
                .image(resolveImageUrl(product.getFeaturedImageUrl()))
                .images(images)
                .processor(processor)
                .storage(storage)
                .ram(ram)
                .screen(screen)
                .graphic(graphic)
                .os(os)
                .connection(connection)
                .specs(specs)
                .inStock(inStock)
                .quantity(quantity)
                .build();
    }

    private String resolveImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return "";
        }
        if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://") || imageUrl.startsWith("/")) {
            return imageUrl;
        }
        if (awsConfiguration != null) {
            try {
                return awsConfiguration.getPresignedUrl(imageUrl);
            } catch (Exception ignored) {
                return imageUrl;
            }
        }
        return imageUrl;
    }
}
