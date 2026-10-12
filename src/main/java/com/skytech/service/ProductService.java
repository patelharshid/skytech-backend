package com.skytech.service;

import com.skytech.dto.*;
import com.skytech.entity.*;
import com.skytech.error.CustomException;
import com.skytech.repository.BrandRepository;
import com.skytech.repository.CategoryRepository;
import com.skytech.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public List<ProductResponse> getAll(String search, Long categoryId, Long brandId, ProductStatus status) {
        List<Product> products = productRepository.searchProducts((search != null && !search.trim().isEmpty()) ? search.trim() : null, categoryId, brandId, status);
        return products.stream().map(this::toResponse).toList();
    }

    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new CustomException("id", "Product not found with id: " + id));
        return toResponse(product);
    }

    public ProductResponse getBySku(String sku) {
        Product product = productRepository.findBySku(sku.trim()).orElseThrow(() -> new CustomException("sku", "Product not found with SKU: " + sku));
        return toResponse(product);
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new CustomException("name", "Product name cannot be blank");
        }
        if (request.getSku() == null || request.getSku().trim().isEmpty()) {
            throw new CustomException("sku", "Product SKU cannot be blank");
        }

        Product product = new Product();
        product.setName(request.getName().trim());
        product.setSku(request.getSku().trim().toUpperCase());
        product.setSlug(request.getName().trim().toLowerCase().replaceAll("[^a-z0-9]", "-"));
        product.setModel(request.getModel());
        product.setDescription(request.getDescription());
        product.setFeaturedImageUrl(request.getFeaturedImageUrl());
        product.setStatus(request.getStatus() != null ? request.getStatus() : ProductStatus.ACTIVE);

        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId()).ifPresent(product::setCategory);
        }
        if (request.getBrandId() != null) {
            brandRepository.findById(request.getBrandId()).ifPresent(product::setBrand);
        }

        // Pricing
        if (request.getPricing() != null) {
            ProductPricingDto productPricingDto = request.getPricing();
            ProductPricing pricing = new ProductPricing();
            pricing.setProduct(product);
            pricing.setOriginalPrice(productPricingDto.getOriginalPrice() != null ? productPricingDto.getOriginalPrice() : productPricingDto.getSellingPrice());
            pricing.setSellingPrice(productPricingDto.getSellingPrice() != null ? productPricingDto.getSellingPrice() : BigDecimal.ZERO);
            pricing.setDiscountType(productPricingDto.getDiscountType() != null ? productPricingDto.getDiscountType() : DiscountType.NONE);
            pricing.setDiscountValue(productPricingDto.getDiscountValue() != null ? productPricingDto.getDiscountValue() : BigDecimal.ZERO);

            BigDecimal finalPrice = pricing.getSellingPrice();
            if (pricing.getDiscountType() == DiscountType.PERCENTAGE && pricing.getDiscountValue().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal discount = pricing.getSellingPrice().multiply(pricing.getDiscountValue()).divide(BigDecimal.valueOf(100));
                finalPrice = pricing.getSellingPrice().subtract(discount).max(BigDecimal.ZERO);
            } else if (pricing.getDiscountType() == DiscountType.FIXED && pricing.getDiscountValue().compareTo(BigDecimal.ZERO) > 0) {
                finalPrice = pricing.getSellingPrice().subtract(pricing.getDiscountValue()).max(BigDecimal.ZERO);
            }
            pricing.setFinalPrice(finalPrice);
            product.setPricing(pricing);
        }

        // Inventory
        if (request.getInventory() != null) {
            ProductInventoryDto iDto = request.getInventory();
            ProductInventory inventory = new ProductInventory();
            inventory.setProduct(product);
            inventory.setQuantity(iDto.getQuantity() != null ? iDto.getQuantity() : 0);
            inventory.setLowStockThreshold(iDto.getLowStockThreshold() != null ? iDto.getLowStockThreshold() : 5);
            inventory.setIsInStock(inventory.getQuantity() > 0);
            product.setInventory(inventory);
        }

        // Specifications
        if (request.getSpecifications() != null) {
            for (int i = 0; i < request.getSpecifications().size(); i++) {
                ProductSpecificationDto sDto = request.getSpecifications().get(i);
                if (sDto.getSpecKey() != null && !sDto.getSpecKey().trim().isEmpty()) {
                    ProductSpecification spec = new ProductSpecification();
                    spec.setProduct(product);
                    spec.setSpecGroup(sDto.getSpecGroup() != null ? sDto.getSpecGroup() : "General");
                    spec.setSpecKey(sDto.getSpecKey());
                    spec.setSpecValue(sDto.getSpecValue() != null ? sDto.getSpecValue() : "");
                    spec.setDisplayOrder(i);
                    product.getSpecifications().add(spec);
                }
            }
        }

        // Gallery Images
        if (request.getGalleryImages() != null) {
            for (int i = 0; i < request.getGalleryImages().size(); i++) {
                String imgUrl = request.getGalleryImages().get(i);
                if (imgUrl != null && !imgUrl.trim().isEmpty()) {
                    ProductImage img = new ProductImage();
                    img.setProduct(product);
                    img.setImageUrl(imgUrl.trim());
                    img.setDisplayOrder(i);
                    img.setIsPrimary(i == 0);
                    product.getGalleryImages().add(img);
                }
            }
        }

        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException("id", "Product not found with id: " + id));

        if (request.getName() != null) product.setName(request.getName().trim());
        if (request.getSku() != null) product.setSku(request.getSku().trim().toUpperCase());
        if (request.getModel() != null) product.setModel(request.getModel().trim());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getFeaturedImageUrl() != null) product.setFeaturedImageUrl(request.getFeaturedImageUrl().trim());
        if (request.getStatus() != null) product.setStatus(request.getStatus());

        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId()).ifPresent(product::setCategory);
        }
        if (request.getBrandId() != null) {
            brandRepository.findById(request.getBrandId()).ifPresent(product::setBrand);
        }

        // Update Pricing
        if (request.getPricing() != null && product.getPricing() != null) {
            ProductPricingDto pDto = request.getPricing();
            ProductPricing pricing = product.getPricing();
            if (pDto.getSellingPrice() != null) pricing.setSellingPrice(pDto.getSellingPrice());
            if (pDto.getOriginalPrice() != null) pricing.setOriginalPrice(pDto.getOriginalPrice());
            if (pDto.getDiscountType() != null) pricing.setDiscountType(pDto.getDiscountType());
            if (pDto.getDiscountValue() != null) pricing.setDiscountValue(pDto.getDiscountValue());

            BigDecimal finalPrice = pricing.getSellingPrice();
            if (pricing.getDiscountType() == DiscountType.PERCENTAGE && pricing.getDiscountValue().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal discount = pricing.getSellingPrice().multiply(pricing.getDiscountValue()).divide(BigDecimal.valueOf(100));
                finalPrice = pricing.getSellingPrice().subtract(discount).max(BigDecimal.ZERO);
            } else if (pricing.getDiscountType() == DiscountType.FIXED && pricing.getDiscountValue().compareTo(BigDecimal.ZERO) > 0) {
                finalPrice = pricing.getSellingPrice().subtract(pricing.getDiscountValue()).max(BigDecimal.ZERO);
            }
            pricing.setFinalPrice(finalPrice);
        }

        // Update Inventory
        if (request.getInventory() != null && product.getInventory() != null) {
            ProductInventoryDto iDto = request.getInventory();
            ProductInventory inv = product.getInventory();
            if (iDto.getQuantity() != null) inv.setQuantity(Math.max(0, iDto.getQuantity()));
            if (iDto.getLowStockThreshold() != null) inv.setLowStockThreshold(iDto.getLowStockThreshold());
            inv.setIsInStock(inv.getQuantity() > 0);
        }

        // Update Specifications
        if (request.getSpecifications() != null) {
            product.getSpecifications().clear();
            for (int i = 0; i < request.getSpecifications().size(); i++) {
                ProductSpecificationDto sDto = request.getSpecifications().get(i);
                if (sDto.getSpecKey() != null && !sDto.getSpecKey().trim().isEmpty()) {
                    ProductSpecification spec = new ProductSpecification();
                    spec.setProduct(product);
                    spec.setSpecGroup(sDto.getSpecGroup() != null ? sDto.getSpecGroup() : "General");
                    spec.setSpecKey(sDto.getSpecKey());
                    spec.setSpecValue(sDto.getSpecValue() != null ? sDto.getSpecValue() : "");
                    spec.setDisplayOrder(i);
                    product.getSpecifications().add(spec);
                }
            }
        }

        // Update Gallery
        if (request.getGalleryImages() != null) {
            product.getGalleryImages().clear();
            for (int i = 0; i < request.getGalleryImages().size(); i++) {
                String imgUrl = request.getGalleryImages().get(i);
                if (imgUrl != null && !imgUrl.trim().isEmpty()) {
                    ProductImage img = new ProductImage();
                    img.setProduct(product);
                    img.setImageUrl(imgUrl.trim());
                    img.setDisplayOrder(i);
                    img.setIsPrimary(i == 0);
                    product.getGalleryImages().add(img);
                }
            }
        }

        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateStock(Long id, StockUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException("id", "Product not found with id: " + id));

        ProductInventory inv = product.getInventory();
        if (inv != null) {
            if (request.getNewQuantity() != null) {
                inv.setQuantity(Math.max(0, request.getNewQuantity()));
            } else if (request.getQuantityDelta() != null) {
                inv.setQuantity(Math.max(0, inv.getQuantity() + request.getQuantityDelta()));
            }
            if (request.getLowStockThreshold() != null) {
                inv.setLowStockThreshold(request.getLowStockThreshold());
            }
            inv.setIsInStock(inv.getQuantity() > 0);
        }

        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateStatus(Long id, ProductStatus status) {
        Product product = productRepository.findById(id).orElseThrow(() -> new CustomException("id", "Product not found with id: " + id));
        product.setStatus(status);
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public Boolean delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new CustomException("id", "Product not found with id: " + id));
        productRepository.delete(product);
        return true;
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse res = new ProductResponse();
        res.setId(product.getId());
        res.setName(product.getName());
        res.setSlug(product.getSlug());
        res.setSku(product.getSku());
        res.setModel(product.getModel());
        res.setDescription(product.getDescription());
        res.setStatus(product.getStatus());
        res.setFeaturedImageUrl(product.getFeaturedImageUrl());
        res.setCategory(product.getCategory());
        res.setBrand(product.getBrand());
        res.setCreatedDateTime(product.getCreatedDateTime());
        res.setUpdatedDateTime(product.getUpdatedDateTime());

        if (product.getPricing() != null) {
            ProductPricing p = product.getPricing();
            res.setPricing(new ProductPricingDto(
                    p.getOriginalPrice(),
                    p.getSellingPrice(),
                    p.getDiscountType(),
                    p.getDiscountValue(),
                    p.getFinalPrice(),
                    p.getDiscountPercentage()
            ));
        }

        if (product.getInventory() != null) {
            ProductInventory i = product.getInventory();
            boolean isLow = i.getQuantity() <= i.getLowStockThreshold() && i.getQuantity() > 0;
            res.setInventory(new ProductInventoryDto(
                    i.getQuantity(),
                    i.getLowStockThreshold(),
                    i.getIsInStock(),
                    isLow,
                    i.getLastRestockedAt()
            ));
        }

        if (product.getSpecifications() != null) {
            List<ProductSpecificationDto> specDtos = new ArrayList<>();
            for (ProductSpecification s : product.getSpecifications()) {
                specDtos.add(new ProductSpecificationDto(
                        s.getSpecGroup(),
                        s.getSpecKey(),
                        s.getSpecValue(),
                        s.getDisplayOrder()
                ));
            }
            res.setSpecifications(specDtos);
        }

        if (product.getGalleryImages() != null) {
            List<String> images = new ArrayList<>();
            for (ProductImage img : product.getGalleryImages()) {
                images.add(img.getImageUrl());
            }
            res.setGalleryImages(images);
        }

        return res;
    }
}
