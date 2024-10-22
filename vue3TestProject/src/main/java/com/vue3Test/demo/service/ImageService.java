package com.vue3Test.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vue3Test.demo.DTO.ImageDTO;
import com.vue3Test.demo.model.ImageModel;
import com.vue3Test.demo.repository.ImageRepository;

import jakarta.transaction.Transactional;

@Service
public class ImageService {
	@Autowired
    private ImageRepository imageRepository;
	
	@Value("${upload.dir}") // application.properties에서 경로를 가져옴
    private String uploadDir;
    
	
	//ImageList
    public List<ImageModel> getAllImages() {
        return imageRepository.findAll();
    }
    
    //ImageCreate
    public String saveImage(MultipartFile file) throws IOException {
    	 // 파일 저장 경로 설정
        String uploadDir = "D:\\uploads\\"; // 적절한 경로로 설정
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리가 없으면 생성
        }
        
        // 파일 이름 생성
        String fileName = file.getOriginalFilename();
        File destinationFile = new File(directory, fileName);
        
        // 파일 저장
        file.transferTo(destinationFile);
        return fileName;
    }
    public void saveImageInfo(ImageModel imageModel) {
    	imageRepository.save(imageModel);
    }
    
    //ImageDetail
    public ImageDTO getImageById(Long id) {
        Optional<ImageModel> imageEntity = imageRepository.findById(id);
        if (imageEntity.isPresent()) {
            ImageDTO imageDTO = new ImageDTO();
            imageDTO.setImage_id(imageEntity.get().getImage_id());
            imageDTO.setImage_name(imageEntity.get().getImage_name());
            imageDTO.setImage_src(imageEntity.get().getImage_src());
            imageDTO.setImage_text(imageEntity.get().getImage_text());
            imageDTO.setCrete_user(imageEntity.get().getCrete_user());
            imageDTO.setCrate_dt(imageEntity.get().getCrate_dt());
            imageDTO.setUpdate_user(imageEntity.get().getUpdate_user());
            imageDTO.setUpdate_dt(imageEntity.get().getUpdate_dt());
            return imageDTO;
            
        }
        return null;
    }
    
    // ImageID로 이미지 찾기
    public Optional<ImageModel> findById(Long id) {
        return imageRepository.findById(id);
    }
    
    @Transactional
    public void deleteImageById(Long id) {
        imageRepository.deleteById(id);
    }
}
