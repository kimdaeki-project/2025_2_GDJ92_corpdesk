package com.goodee.corpdesk.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.goodee.corpdesk.file.dto.FileDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileManager {

    /**
     * 지정된 경로에 파일을 저장하고 파일 정보를 반환
     *
     * @param filePath 저장될 서버의 경로
     * @param fileData 저장할 파일 데이터
     * @return 저장된 파일 정보를 담은 FileDTO, 저장 실패 시 null
     */
    public FileDTO saveFile(String filePath, MultipartFile fileData) {
        
        if (fileData == null || fileData.isEmpty()) {
            log.warn("파일 데이터가 비어있습니다. 저장을 건너뜁니다.");
            return null;
        }

        try {
            // 1. 디렉토리 생성
            Path uploadPath = Paths.get(filePath);
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 2. 파일 정보 추출 및 저장용 파일명 생성
            String originalFilename = fileData.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String savedName = UUID.randomUUID().toString().replaceAll("-", "");
            String fileName = savedName + "." + extension;
            Path destinationPath = uploadPath.resolve(fileName);
            
            // 3. HDD에 저장
            Files.copy(fileData.getInputStream(), destinationPath);
            
            // 4. 반환할 정보를 FileDTO에 바인딩
            FileDTO fileDTO = new FileDTO();
            fileDTO.setExtension(extension);
            fileDTO.setOriName(getOriginalNameWithoutExtension(originalFilename));
            fileDTO.setSaveName(savedName);
            
            log.info("파일 저장 성공: {}", destinationPath);
            return fileDTO;

        } catch (IOException | IllegalStateException e) {
            log.error("파일 저장 실패: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 파일 이름에서 확장자를 추출하고 반환
     * @param fileName 파일명
     * @return 확장자
     */
    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.lastIndexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }
    
    /**
     * 파일 이름에서 확장자를 제외한 원본 파일명을 추출하고 반환
     * @param fileName 파일명
     * @return 확장자 없는 원본 파일명
     */
    private String getOriginalNameWithoutExtension(String fileName) {
        if (fileName != null && fileName.lastIndexOf(".") != -1) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        }
        return fileName; // 확장자가 없는 경우 전체 이름 반환
    }
    
    /**
     * 지정된 경로에 있는 파일을 삭제하고 삭제 여부를 반환
     * @param filePath 파일이 저장된 서버의 경로
     * @param fileDTO 삭제할 파일 정보를 담은 FileDTO
     * @return 삭제 성공 시 true, 실패 시 false
     */
    public boolean deleteFile(String filePath, FileDTO fileDTO) {
        if (fileDTO == null || fileDTO.getSaveName() == null || fileDTO.getExtension() == null) {
            log.warn("파일 삭제를 위한 정보가 불완전합니다. 삭제를 건너뜁니다.");
            return false;
        }

        try {
            String fileNameWithExtension = fileDTO.getSaveName() + "." + fileDTO.getExtension();
            Path fileToDeletePath = Paths.get(filePath, fileNameWithExtension);

            if (Files.exists(fileToDeletePath) && Files.isReadable(fileToDeletePath)) {
                Files.delete(fileToDeletePath);
                log.info("파일 삭제 성공: {}", fileToDeletePath);
                return true;
            } else {
                log.warn("삭제하려는 파일이 존재하지 않거나 접근할 수 없습니다: {}", fileToDeletePath);
                return false;
            }
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", e.getMessage(), e);
            return false;
        }
    }
}