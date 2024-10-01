package com.eeit87t3.tickiteasy.product.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.image.ImageDirectory;
import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.repository.ProductRepo;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
    private ImageUtil imageUtil;
	
	@Transactional
    public ProductEntity addProduct(ProductEntity product, MultipartFile imageFile) throws IOException {
        ProductEntity savedProduct = productRepo.save(product);
        
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = imageUtil.saveImage(ImageDirectory.PRODUCT, imageFile, "product_" + savedProduct.getProductID());
            savedProduct.setProductPic(imagePath);
            savedProduct = productRepo.save(savedProduct);
        }
        
        return savedProduct;
    }
    
	@Transactional
	public ProductEntity updateProductById(Integer productID, ProductEntity newProduct, MultipartFile imageFile) throws IOException {
	    Optional<ProductEntity> optional = productRepo.findById(productID);

	    if(optional.isPresent()) {
	        ProductEntity product = optional.get();

	        // 更新圖片
	        if (imageFile != null && !imageFile.isEmpty()) {
	            if (product.getProductPic() != null) {
	                imageUtil.deleteImage(product.getProductPic());
	            }
	            String imagePath = imageUtil.saveImage(ImageDirectory.PRODUCT, imageFile, "product_" + productID);
	            product.setProductPic(imagePath);
	        } else if (newProduct.getProductPic() != null) {
	            product.setProductPic(newProduct.getProductPic());
	        }

	        // 更新其他屬性
	        product.setProductCategory(newProduct.getProductCategory());
	        product.setProductTag(newProduct.getProductTag());
	        product.setProductName(newProduct.getProductName());
	        product.setProductDesc(newProduct.getProductDesc());
	        product.setPrice(newProduct.getPrice());
	        product.setStock(newProduct.getStock());
	        product.setStatus(newProduct.getStatus());
	        product.setProdTotalReviews(newProduct.getProdTotalReviews());
	        product.setProdTotalScore(newProduct.getProdTotalScore());

	        return productRepo.save(product);
	    }

	    return null;
	}
    
    @Transactional
    public void deleteProductById(Integer productID) throws IOException {
        Optional<ProductEntity> optional = productRepo.findById(productID);
        if (optional.isPresent()) {
            ProductEntity product = optional.get();
            if (product.getProductPic() != null) {
                imageUtil.deleteImage(product.getProductPic());
            }
            productRepo.deleteById(productID);
        }
    }
	
	public ProductEntity findProductById(Integer productID) {
		Optional<ProductEntity> optional = productRepo.findById(productID);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}
	
	public List<ProductEntity> findAllProducts(){
		return productRepo.findAll();
	}
	
	

}
