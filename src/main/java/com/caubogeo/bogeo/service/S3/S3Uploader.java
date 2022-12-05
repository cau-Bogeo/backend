package com.caubogeo.bogeo.service.S3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Autowired
    public S3Uploader(AmazonS3Client amazonS3Client){
        this.amazonS3Client = amazonS3Client;
    }

    @Value("${cloud.aws.s3.bucket}")
    public String bucket; //S3 버킷 이름

    public String upload(MultipartFile mFile) throws IOException {
        File uploadFile = convert(mFile) // 파일을 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        return upload(uploadFile);
    }

    private String upload(File uploadFile){
        String fileName = "images/" + UUID.randomUUID() + changeFileNameToTimeSet(uploadFile.getName()); //S3에 저장되는 파일 이름은 업로드 시간을 이용한다.
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile mFile) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + mFile.getOriginalFilename());
        log.info("file : {}", convertFile.toString());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try(FileOutputStream fos = new FileOutputStream(convertFile)){
                fos.write(mFile.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        PutObjectRequest a = new PutObjectRequest(bucket, fileName, uploadFile).
                withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(a);
        String putS3FileUrl = amazonS3Client.getUrl(bucket, fileName).toString();
        log.info("putS3() called /  fileUrl : "+ putS3FileUrl  );
        return putS3FileUrl;
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()){
            log.info("Local File delete success");
            return;
        }
        log.info("Local File delete fail");
    }

    // S3에 해당이름의 파일 삭제
    public void deleteS3(String fileName){
        log.info("deleteS3() called / fileName: {"+ fileName +"} will be deleted");
        try{
            String key = "images/" + fileName;  // 경로에는 파일이름 앞에 static/images/ 경로가 들어간다 (duos s3 디렉토리 구조 참고)

            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key)); // DELETE

        }catch (AmazonServiceException e){
            // 일치하는 fileName을 S3에서 찾지 못하는 경우
            log.error("delete S3() AmazonService Exception" + "\n [S3] fail to delete file :" + fileName + "\n"+ e.getMessage() );
        }
    }

    // url 로 부터 fileName 뽑아오기
    public String getFileName(String url) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        return fileName;
    }

    // FileName을 현재시각으로 바꿔준다. 확장자(png, jpeg ...)는 유지한다.
    public String changeFileNameToTimeSet(String fileName){
        int index = fileName.lastIndexOf(".");
        String extension = fileName.substring(index+1); //확장자 //ex) png

        Date now = new Date();
        Locale currentLocale = new Locale("KOREAN","KOREA");
        String pattern = "yyyyMMddHHmmss";
        SimpleDateFormat format = new SimpleDateFormat(pattern,currentLocale);
        String result = format.format(now)+"."+extension;
        log.info("changedFileNameToTimeSet :" + fileName + " -> " + result);
        return result;

    }
}
