package projectsem4.medcare_server.domain.entity;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "brief")
public class Brief {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "gender", nullable = false)
  private String gender;
  @Column(name = "province", nullable = false)
  private String province;
  @Column(name = "district", nullable = false)
  private String district;
  @Column(name = "ward", nullable = false)
  private String ward;
  @Column(name = "address", nullable = false)
  private String address;
  @Column(name = "dob", nullable = false)
  private Date dob;
  @Column(name = "phone", nullable = false)
  private String phone;

  @Column(name = "identifier")
  private String identifier;

  @Column(name = "job")
  private String job;

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
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
