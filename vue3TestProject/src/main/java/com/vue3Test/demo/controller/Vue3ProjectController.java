package com.vue3Test.demo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vue3Test.demo.DTO.DevLogDTO;
import com.vue3Test.demo.DTO.ImageDTO;
import com.vue3Test.demo.model.CareerModel;
import com.vue3Test.demo.model.DevLogModel;
import com.vue3Test.demo.model.ImageModel;
import com.vue3Test.demo.model.UserModel;
import com.vue3Test.demo.service.CarrerService;
import com.vue3Test.demo.service.DevLogService;
import com.vue3Test.demo.service.ImageService;
import com.vue3Test.demo.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "http://localhost:3000")  // Vue가 실행되는 주소와 포트
public class Vue3ProjectController {
	private final Path fileStorageLocation = Paths.get("D:/uploads").toAbsolutePath().normalize();
	
	@Autowired
	private UserService userService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private CarrerService carrerService;
	@Autowired
	private DevLogService devLogService;
	
	@GetMapping("/api/users")
    public List<UserModel> getAllUsers() {
        return userService.getAllUsers();
    }
	
	@GetMapping("/api/images")
    public List<ImageModel> getAllImages() {
        return imageService.getAllImages();
    }
	
	//imagecreate
	@PostMapping(value = "/saveImageCard")
	public ResponseEntity<?> saveImageText(
			@RequestParam("file") MultipartFile file,
			@RequestParam("text") String text,
			@RequestParam("path") String path,
			@RequestParam("createDate") String createDate,
			@RequestParam("updateDate") String updateDate,
			@RequestParam("createUser") String createUser,
			@RequestParam("updateUser") String updateUser){
		if (file == null || file.isEmpty()) {
	        return ResponseEntity.badRequest().body("파일이 없습니다.");
	    }
		try {
			System.out.println("파일 이름: " + file.getOriginalFilename());
		    System.out.println("텍스트: " + text);
            // 이미지 파일 저장
            String fileName = imageService.saveImage(file);

            // 텍스트와 이미지 정보 저장
            ImageModel imageInfo = new ImageModel();
            imageInfo.setImage_name(fileName);
            imageInfo.setImage_text(text);
            imageInfo.setImage_src("D:/uploads/" + fileName); // 파일 경로 저장
            imageInfo.setCrate_dt(createDate);;
            imageInfo.setCrete_user(createUser);
            imageInfo.setUpdate_dt(updateDate);
            imageInfo.setUpdate_user(updateUser);

            imageService.saveImageInfo(imageInfo);

            return ResponseEntity.ok("이미지와 텍스트가 성공적으로 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("파일 이름: " + file.getOriginalFilename());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장 중 오류 발생: " + e.getMessage());
        }
    }
	
	@GetMapping("/files/{fileName}")
	    public ResponseEntity<Resource> getImage(@PathVariable("fileName") String fileName) {
	        try {
	            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
	            Resource resource = new UrlResource(filePath.toUri());

	            if (resource.exists()) {
	                return ResponseEntity.ok()
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	                    .body(resource);
	            } else {
	                return ResponseEntity.notFound().build();
	            }
	        } catch (MalformedURLException e) {
	            return ResponseEntity.badRequest().build();
	        }
	    }
	
	 
	//imagedetail
	@GetMapping("/images/{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable("id") Long id) {
        ImageDTO imageDto = imageService.getImageById(id);
        if (imageDto != null) {
            return ResponseEntity.ok(imageDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	//imageupdate
	@PostMapping("/updateImageCard/{id}")
	public ResponseEntity<?> updateImage(
			@PathVariable("id") Long id,
	        @RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam("text") String text,
			@RequestParam("path") String path,
			@RequestParam("updateDate") String updateDate,
			@RequestParam("updateUser") String updateUser) {
	    
		 // 해당 ID로 기존 이미지 정보를 불러옵니다.
	    ImageModel imageInfo = imageService.findById(id)
	        .orElseThrow(() -> new RuntimeException("해당 ID의 이미지가 없습니다."));

		// 텍스트 업데이트
	    imageInfo.setImage_text(text);
	    imageInfo.setUpdate_dt(updateDate);  // 수정된 날짜 저장
	    imageInfo.setUpdate_user(updateUser); // 수정한 사용자 저장

		// 파일이 존재하면 파일을 저장하고 경로를 업데이트
		if (file != null && !file.isEmpty()) {
		    try {
		        String fileName = imageService.saveImage(file); // 파일 저장
		        imageInfo.setImage_name(fileName); // 파일명 업데이트
		        imageInfo.setImage_src("D:/uploads/" + fileName); // 파일 경로 저장
		    } catch (IOException e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 저장 중 오류가 발생했습니다.");
		    }
		}

		// 변경된 이미지 정보를 저장
		imageService.saveImageInfo(imageInfo);

		return ResponseEntity.ok("이미지와 텍스트가 성공적으로 업데이트되었습니다.");
	}
	
	//imagedelete
	@DeleteMapping("/deleteImageCard/{id}")
	public ResponseEntity<?>deleteImage(@PathVariable("id") Long id){
		try {
            imageService.deleteImageById(id);
            return ResponseEntity.ok("이미지가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("삭제 중 오류 발생: " + e.getMessage());
        }
	}
	
	//carrertext
	@PostMapping("/careerText/{id}")
	public ResponseEntity<CareerModel>careerText(@PathVariable("id") String id){
		System.out.println("Request ID: " + id); // ID 값 출력
		CareerModel careermodel = carrerService.getCareerById(id);
        return ResponseEntity.ok(careermodel);

	}
	
	//devlog 정보 저장
	@PostMapping("/saveDevLog")
	public ResponseEntity<String> saveDevLog(@RequestBody DevLogDTO devLogDTO) {
		try {
			DevLogModel devLogModel = devLogService.saveDevLog(devLogDTO); // 서비스 메서드 호출
	        return ResponseEntity.ok(String.valueOf(devLogModel.getId()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 저장 실패: " + e.getMessage());
	    }
	}
	
	//devlog 이미지 저장
	@PostMapping("/uploadDevImage")
	public ResponseEntity<String> uploadDevImage(@RequestParam("file") MultipartFile file) {
		String fileName = file.getOriginalFilename();
		Path destinationFile = Paths.get("D:/uploads/").resolve(fileName).normalize();
		    
	    
		try {
	        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to store file");
	    }
	    
	    // Return the HTTP URL for the uploaded image
	    String imageUrl = "http://localhost:8080/images/devlog/" + fileName;
	    return ResponseEntity.ok(imageUrl);
	}
	
	//devlog리스트의 이미지 불러오기
	@GetMapping("/images/devlog/{fileName}") 
	public ResponseEntity<Resource> serveImage(@PathVariable("fileName") String fileName) {
	    System.out.println("fileName :" + fileName); 
	    try {
	        Path filePath = Paths.get("D:/uploads/" + fileName);
	        Resource resource = new UrlResource(filePath.toUri());

	        if (resource.exists() || resource.isReadable()) {
	            return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	                .body(resource);
	        } else {
	            throw new RuntimeException("Could not read the file!");
	        }
	    } catch (MalformedURLException e) {
	        throw new RuntimeException("Error: " + e.getMessage());
	    }
	}
	
	
	//devlog리스트 불러오기
	@GetMapping("/devLoglist")
	public List<DevLogDTO> getAllDevLogsList() {
		List<DevLogModel> devLogModel= devLogService.findAllDevLogList();
		
		return devLogModel.stream()
				.map(log -> {
		            Long id = log.getId();
		            String title = log.getTitle();
		            String coverImage = log.getCoverImage();
		            String createDt = log.getCreate_dt();
		            String createUser = log.getCreate_user();
		            String updateDt = log.getUpdate_dt();
		            String updateUser = log.getUpdate_user();
		            String content = log.getContent();
		            		
		            return new DevLogDTO(id, title, coverImage, content, createDt, createUser, updateDt, updateUser);
		        })
		        .collect(Collectors.toList());
	}
	
	//devlog디테일 불러오기
	@GetMapping("devlogDetail/{id}")
	public ResponseEntity<DevLogModel> getDevLogById(@PathVariable("id") Long id) {
        DevLogModel devLogModel = devLogService.findById(id);
        if (devLogModel != null) {
            // 이미지 URL을 필요에 맞게 수정
            String imageUrl = "/images/devlog/" + devLogModel.getCoverImage();
            devLogModel.setCoverImage(imageUrl);
            return ResponseEntity.ok(devLogModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	//devlog수정
	@PutMapping("/updateDevLog/{id}")
	public ResponseEntity<?> updateDevLog(@PathVariable("id") Long id, @RequestBody DevLogDTO devLogDTO) {
	    // 데이터베이스에서 해당 id에 해당하는 개발 일지 찾기
		DevLogModel devLogModel = devLogService.findById(id);
	    
		devLogModel.setContent(devLogDTO.getContent());
		devLogModel.setUpdate_user(devLogDTO.getUpdate_user());
		devLogModel.setUpdate_dt(devLogDTO.getUpdate_dt());
		
		devLogService.saveUpdateData(devLogModel); // 데이터 저장
		
		return ResponseEntity.ok(devLogModel);
	}
	
	//devlog삭제

	@DeleteMapping("/deleteDevlog/{id}")
	public ResponseEntity<?>deleteDevlog(@PathVariable("id") Long id){
		try {
			devLogService.deleteById(id);
	        return ResponseEntity.ok("이미지가 성공적으로 삭제되었습니다.");
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("삭제 중 오류 발생: " + e.getMessage());
	    }
	}
}
