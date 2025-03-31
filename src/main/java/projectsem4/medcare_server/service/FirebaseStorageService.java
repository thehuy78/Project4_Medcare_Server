package projectsem4.medcare_server.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageService {

  public String uploadFile(MultipartFile file) throws IOException {

    String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
    Bucket bucket = StorageClient.getInstance().bucket();
    Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());
    return fileName;
  }
}
