package projectsem4.medcare_server.domain.entity;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "hospital")
public class Hospital {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "code", nullable = false, unique = true)
  private String code;

  @Column(name = "name", nullable = false)
  private String name;
  @Column(name = "logo", nullable = false)
  private String logo;
  @Column(name = "image", columnDefinition = "LONGTEXT")
  private String image;
  @Column(name = "district", nullable = false)
  private String district;

  @Column(name = "province", nullable = false)
  private String province;

  @Column(name = "openTime", nullable = false)
  private String openTime;
  @Column(name = "closeTime", nullable = false)
  private String closeTime;

  @Column(name = "workDay", nullable = false)
  private String workDay;
  @Column(name = "address", nullable = false)
  private String address;
  @Column(name = "map")
  private String map;

  @Column(name = "description", columnDefinition = "LONGTEXT")
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
  @JoinColumn(name = "type_id", nullable = false)
  private Type type;

  // @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval =
  // true)
  // private List<Booking> bookings = new ArrayList<>();

  // @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval =
  // true)
  // private List<Department> departments = new ArrayList<>();

}
