package projectsem4.medcare_server.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

  // @PostConstruct
  // public void initialize() {
  // try {
  // FileInputStream serviceAccount = new
  // FileInputStream("src/main/resources/firebase-adminsdk.json");

  // FirebaseOptions options = FirebaseOptions.builder()
  // .setCredentials(GoogleCredentials.fromStream(serviceAccount))
  // .setStorageBucket("medcare-9db1e.appspot.com")
  // .setDatabaseUrl("https://medcare-9db1e-default-rtdb.firebaseio.com/")
  // .build();

  // if (FirebaseApp.getApps().isEmpty()) {
  // FirebaseApp.initializeApp(options);
  // }
  // } catch (IOException e) {
  // e.printStackTrace();
  // }
  // }

  @PostConstruct
  public void initialize() {
    try {
      FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-adminsdk.json");

      FirebaseOptions options = FirebaseOptions.builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .setStorageBucket("medcare-9db1e.appspot.com")
          .setDatabaseUrl("https://medcare-9db1e-default-rtdb.firebaseio.com/")
          .build();

      if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options);
        System.out.println("Firebase initialized successfully.");
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println("Error initializing Firebase: " + e.getMessage());
    }
  }

}
