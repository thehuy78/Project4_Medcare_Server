package projectsem4.medcare_server.domain.entity;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "doctor")
public class Doctor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "code", nullable = false, unique = true)
  private String code;
  @Column(name = "name", nullable = false)
  private String name;
  @Column(name = "workDay", nullable = false)
  private String workDay;
  @Column(name = "timeWork", nullable = false)
  private String timeWork;
  @Column(name = "level", nullable = false)
  private String level;
  @Column(name = "gender", nullable = false)
  private String gender;
  @Column(name = "fee", nullable = false)
  private Double fee;
  @Column(name = "room", nullable = false)
  private Integer room;
  @Column(name = "patients", nullable = false)
  private Integer patients;
  @Column(name = "avatar")
  private String avatar;
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
  @JoinColumn(name = "hospital_id", nullable = false)
  private Hospital hospital;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "department_id", nullable = false)
  private Department department;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @JsonManagedReference
  @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Booking> bookings = new ArrayList<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Offline> Offlines = new ArrayList<>();

}
