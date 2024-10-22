package com.vue3Test.demo.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vue3Test.demo.DTO.DevLogDTO;
import com.vue3Test.demo.model.DevLogModel;
import com.vue3Test.demo.repository.DevLogRepository;

import jakarta.transaction.Transactional;



@Service
public class DevLogService {
	@Autowired
	private DevLogRepository devLogRepository;
	
	public DevLogModel saveDevLog(DevLogDTO devLogDTO) {
		DevLogModel devLogModel = new DevLogModel();
		devLogModel.setTitle(devLogDTO.getTitle());
		devLogModel.setContent(devLogDTO.getContent());
		devLogModel.setCreate_dt(devLogDTO.getCreate_dt());
		devLogModel.setCreate_user(devLogDTO.getCreate_user());
		devLogModel.setUpdate_dt(devLogDTO.getUpdate_dt());
		devLogModel.setUpdate_user(devLogDTO.getUpdate_user());
		devLogModel.setCoverImage(devLogDTO.getCoverImage());
		
		return devLogRepository.save(devLogModel);
	}
	
	// DevLogList
    public List<DevLogModel> findAllDevLogList(){
    	return devLogRepository.findAll();
    }
    
    // DevLog id 가져오기
    public DevLogModel findById(Long id) {
        return devLogRepository.findById(id).orElse(null);
    }
    
    public DevLogModel saveUpdateData(DevLogModel devLog) {
        return devLogRepository.save(devLog);
    }
    
    @Transactional
    public void deleteById(Long id) {
    	devLogRepository.deleteById(id);
    }
}
