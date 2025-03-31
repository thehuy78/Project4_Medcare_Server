package projectsem4.medcare_server.domain.entity;

import java.util.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "useremail", nullable = false)
    private String useremail;

    @Column(name = "service", nullable = false)
    private String service;

    @Column(name = "fee", nullable = false)
    private Double fee;

    @Column(name = "hospitalname", nullable = false)
    private String hospitalname;

    @Column(name = "patientname", nullable = false)
    private String patientname;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "bookingDate", nullable = false)
    private Date bookingDate;
    @Column(name = "bookingTime", nullable = false)
    private String bookingTime;

    @Column(name = "createDate")
    private Date createDate;

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

    @Column(name = "updateDate")
    private Date updateDate;

    @PrePersist
    protected void onCreate() {
        this.createDate = new Date();
        this.updateDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = new Date();
    }
}
