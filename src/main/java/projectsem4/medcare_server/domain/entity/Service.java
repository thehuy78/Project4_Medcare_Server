package projectsem4.medcare_server.domain.entity;

import java.util.Date;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "service")
public class Service {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

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
  @JoinColumn(name = "attributes_id", nullable = false)
  private Attributes attributes;
}
