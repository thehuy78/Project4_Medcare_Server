package projectsem4.medcare_server.domain.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "offline")
public class Offline {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "day", nullable = false)
  private String day;

  @Column(name = "description")
  private String description;

  @Column(name = "createDate")
  private Date createDate;

  @Column(name = "updateDate")
  private Date updateDate;

  @Column(name = "status")
  private String status;

  @PrePersist
  protected void onCreate() {
    this.createDate = new Date();
    this.updateDate = new Date();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updateDate = new Date();
  }

  @ManyToOne
  @JoinColumn(name = "hospital_id")
  private Hospital hospital;

  @ManyToOne
  @JoinColumn(name = "doctor_id")
  private Doctor doctor;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
