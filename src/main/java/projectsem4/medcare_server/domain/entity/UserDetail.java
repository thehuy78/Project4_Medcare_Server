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
@Table(name = "userDetail")
public class UserDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "firstName")
  private String firstName;
  @Column(name = "lastName")
  private String lastName;
  @Column(name = "avatar")
  private String avatar;
  @Column(name = "role", nullable = false)
  private String role;
  @Column(name = "verify")
  private String verify;
  @Column(name = "balance")
  private Double balance;
  @Column(name = "phone")
  private String phone;
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
}