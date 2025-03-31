package projectsem4.medcare_server.domain.entity;


import java.util.Date;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "booking")
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "bookingDate", nullable = false)
  private Date bookingDate;
  @Column(name = "bookingTime", nullable = false)
  private String bookingTime;
  @Column(name = "description")
  private String description;
  @Column(name = "revenue", nullable = false)
  private Double revenue;
  @Column(name = "profit", nullable = false)
  private Double profit;
  @Column(name = "createDate")
  private Date createDate;
  @Column(name = "updateDate")
  private Date updateDate;

  @Column(name = "qrCodeUrl")
  private String qrCodeUrl;

  @Column(name = "status")
  private String status;

  @PrePersist
  protected void onCreate() {
    this.createDate = new Date();
    this.updateDate = new Date();
     if (doctor != null) {
      int currentBookings = doctor.getBookings().stream()
          .filter(booking -> booking.getBookingDate().equals(bookingDate)) 
                                                                           
          .collect(Collectors.toList()).size();
      if (currentBookings >= doctor.getPatients()) {
        throw new IllegalArgumentException("Full ");
      }
    }
  }

  @PreUpdate
  protected void onUpdate() {
    this.updateDate = new Date();
  }

  @ManyToOne
  @JoinColumn(name = "hospital_id", nullable = false)
  private Hospital hospital;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private Type type;

  @ManyToOne
  @JoinColumn(name = "pack_id")
  @JsonBackReference
  private Pack pack;

  @ManyToOne
  @JoinColumn(name = "test_id")
  @JsonBackReference
  private Test test;

  @ManyToOne
  @JoinColumn(name = "vaccine_id")
  @JsonBackReference
  private Vaccine vaccine;

  @ManyToOne
  @JoinColumn(name = "doctor_id")
  @JsonBackReference
  private Doctor doctor;

  @ManyToOne
  @JoinColumn(name = "brief_id", nullable = false)
  private Brief brief;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
