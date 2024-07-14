package ManagementSystem.fpt.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "region")
    private String region;

    @Column(name = "ward")
    private String ward;

    @Column(name = "branch")
    private String branch;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "company")
    private String company;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "supplier_group")
    private String supplierGroup;

    @Column(name = "note")
    private String note;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "createdDate", updatable = false)
    private Date createdDate;
}
