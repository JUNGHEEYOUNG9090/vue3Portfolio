package com.vue3Test.demo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.vue3Test.demo.DTO.PostsDTO;
import com.vue3Test.demo.model.DevLogModel;
import com.vue3Test.demo.model.ImageModel;
import com.vue3Test.demo.model.UserModel;
import com.vue3Test.demo.service.DevLogService;
import com.vue3Test.demo.service.ImageService;
import com.vue3Test.demo.service.PostsService;
import com.vue3Test.demo.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "http://54.180.213.168:3000")  // Vue가 실행되는 주소와 포트
public class Vue3ProjectController {
	private final Path fileStorageLocation = Paths.get("/home/ec2-user/uploads/").toAbsolutePath().normalize();
	
	@Autowired
	private UserService userService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private DevLogService devLogService;
	@Autowired
	private PostsService postsService;
	
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
            imageInfo.setImage_src("/home/ec2-user/uploads/" + fileName); // 파일 경로 저장
            imageInfo.setCrate_dt(createDate);;
            imageInfo.setCrete_user(createUser);
            imageInfo.setUpdate_dt(updateDate);
            imageInfo.setUpdate_user(updateUser);

            imageService.saveImageInfo(imageInfo);

            return ResponseEntity.ok("이미지와 텍스트가 성공적으로 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("파일 이름: " + file.getOriginalFilename());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 저장 실패: " + e.getMessage());
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
	
	@GetMapping("/public/images/{fileName}")
	public ResponseEntity<Resource> getPublicImage(@PathVariable("fileName") String fileName) {
	    try {
	        // 변경된 경로: "/images/Main/{fileName}"
	        Path filePath = Paths.get("/home/ec2-user/public/images/" + fileName).normalize();

	        // 이미지 파일을 Resource로 읽어오기
	        Resource resource = new FileSystemResource(filePath);

	        // 파일이 존재하면 응답
	        if (resource.exists()) {
	            return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	                .contentType(MediaType.IMAGE_JPEG)  // 이미지 타입에 맞게 설정 (예: JPEG, PNG 등)
	                .body(resource);
	        } else {
	            // 파일이 존재하지 않으면 404 응답
	            return ResponseEntity.notFound().build();
	        }
	    } catch (Exception e) {
	        // 예외 발생 시 400 Bad Request 응답
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
	@PutMapping("/updateImageCard/{id}")
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
		        imageInfo.setImage_src("/home/ec2-user/uploads/" + fileName); // 파일 경로 저장
		    } catch (IOException e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 저장 실패: " + e.getMessage());
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
	
	//글에 이미지 올렸을 때 이미지파일 저장해는 API
	@PostMapping("/uploadDevlogImage")
	public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile file) {
	    String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	    Path destinationPath = Paths.get("/home/ec2-user/uploads/" + filename);
	    
	    try {
	    	// 디렉토리가 존재하지 않으면 생성
	        Files.createDirectories(destinationPath.getParent());
	        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
	    } catch (IOException e) {
	        e.printStackTrace(); // 오류 메시지를 콘솔에 출력
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "파일 저장 실패")); // 구체적인 오류 메시지 반환
	    }

	    Map<String, String> response = new HashMap<>();
	    response.put("filename", filename);
	    response.put("imageUrl", "/home/ec2-user/uploads/" + filename);

	    return ResponseEntity.ok(response);
	}
	
	//글에 이미지 올렸을 때 저장해둔 이미지파일 불러오는 API
	@GetMapping("/images/devlog/{filename:.+}")
	  public ResponseEntity<Resource> serveFile(@PathVariable("filename") String filename) {
		try {
		      Path file = Paths.get("/home/ec2-user/uploads/").resolve(filename); // 경로 수정
		      Resource resource = new UrlResource(file.toUri());

		      if (!resource.exists() || !resource.isReadable()) {
		          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 오류 처리
		      }
		      String encodedFileName = URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8);
		      return ResponseEntity.ok()
		            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encodedFileName + "\"")
		            .body(resource);
		} catch (MalformedURLException e) {
		      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 잘못된 URL 요청 처리
		} catch (IOException e) {
		      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 내부 서버 오류 처리
		}
	}
	
	//devlog리스트 불러오기
	@GetMapping("/devLoglist")
	 public List<DevLogModel> getAllDevLogsList() {
        return devLogService.findAllByOrderByIdDesc();
    }
	
	//devlog디테일 불러오기
	@GetMapping("devlogDetail/{id}")
	public ResponseEntity<DevLogModel> getDevLogById(@PathVariable("id") Long id) {
        DevLogModel devLogModel = devLogService.findById(id);
        if (devLogModel != null) {
            return ResponseEntity.ok(devLogModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	//devlog수정
	@PutMapping("/updateDevLogEdit/{id}")
	public ResponseEntity<?> updateDevLog(@PathVariable("id") Long id, @RequestBody DevLogDTO devLogDTO) {
	    // 데이터베이스에서 해당 id에 해당하는 개발 일지 찾기
		DevLogModel devLogModel = devLogService.findById(id);
	    
		devLogModel.setTitle(devLogDTO.getTitle());
		devLogModel.setContent(devLogDTO.getContent());
		devLogModel.setUpdate_user(devLogDTO.getUpdate_user());
		devLogModel.setUpdate_dt(devLogDTO.getUpdate_dt());
		devLogModel.setCoverImage(devLogDTO.getCoverImage());
		
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
	
	@GetMapping("/posts")
	public Page<?> getAllPosts(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
		Pageable pageable = PageRequest.of(page, size);
        return postsService.getAllPosts(pageable);
    }
}
