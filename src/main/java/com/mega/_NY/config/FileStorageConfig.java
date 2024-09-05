//package com.mega._NY.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.File;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//@Configuration
//public class FileStorageConfig {
//
//    @Value("${app.file.upload-dir}")
//    private String uploadDir;
//
//    @Bean
//    CommandLineRunner createUploadDirectoryIfItDoesNotExist() {
//        return args -> {
//            Path uploadPath = Paths.get(uploadDir);
//            if (!Files.exists(uploadPath)) {
//                try {
//                    Files.createDirectories(uploadPath);
//                    System.out.println("Upload directory created: " + uploadPath);
//                } catch (Exception e) {
//                    throw new RuntimeException("Could not create upload directory!", e);
//                }
//            }
//
//            // 디렉토리 권한 설정 (선택적)
//            File uploadDirFile = uploadPath.toFile();
//            uploadDirFile.setReadable(true, false);
//            uploadDirFile.setWritable(true, false);
//            uploadDirFile.setExecutable(true, false);
//        };
//    }
//}