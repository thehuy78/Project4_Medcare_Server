package projectsem4.medcare_server.domain.entity;

import java.util.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "eventVaccine")
public class EventVaccine {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "code", nullable = false, unique = true)
  private String code;

  @Column(name = "name", nullable = false)
  private String name;

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
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

}
