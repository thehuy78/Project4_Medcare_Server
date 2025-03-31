package projectsem4.medcare_server.domain.entity;

import java.util.Calendar;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "codeVerify")
public class CodeVerify {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "code", nullable = false)
  private Long code;

  @Column(name = "createDate")
  private Date createDate;

  @Column(name = "expDate")
  private Date expDate;

  @PrePersist
  protected void onCreate() {
    this.createDate = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(createDate);
    calendar.add(Calendar.MINUTE, 4); // Add 1 minute to createDate
    this.expDate = calendar.getTime();
  }

  @PreUpdate
  protected void onUpdate() {
    this.createDate = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(createDate);
    calendar.add(Calendar.MINUTE, 4); // Add 1 minute to createDate
    this.expDate = calendar.getTime();
  }

}
