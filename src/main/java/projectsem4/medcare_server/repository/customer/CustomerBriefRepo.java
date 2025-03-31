package projectsem4.medcare_server.repository.customer;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectsem4.medcare_server.domain.entity.Brief;
import projectsem4.medcare_server.domain.entity.User;

public interface CustomerBriefRepo extends JpaRepository<Brief, Long> {

    @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerBriefRepo$ProfileEntityDTO(" +
            "h.id,h.name, h.phone, h.dob,h.gender,h.identifier,h.job,h.address, " +
            "CASE WHEN h.ward LIKE '%Phường%' THEN REPLACE(h.ward, 'Phường ', '') " +
            "ELSE h.ward END , " +
            "CASE WHEN h.province LIKE '%Thành phố%' THEN REPLACE(h.province, 'Thành phố ', '') " +
            "ELSE h.province END , " +
            "CASE WHEN h.district LIKE '%Quận%' THEN REPLACE(h.district, 'Quận ', '') " +
            "WHEN h.district LIKE '%Thành phố%' THEN REPLACE(h.district, 'Thành phố ', '') " +
            "ELSE h.district END) " +
            "FROM Brief h WHERE h.status = 'active' AND h.id =:id ")
    ProfileEntityDTO findByIdCustom(@Param("id") Long id);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class ProfileEntityDTO {
        Long id;
        String name;
        String phone;
        Date dob;
        String gender;
        String identifier;
        String job;
        String address;
        String ward;
        String province;
        String district;
    }

    @Query("SELECT new projectsem4.medcare_server.repository.customer.CustomerBriefRepo$ProfileEntityLiteDTO(" +
            "h.id, h.name, h.dob,h.gender,h.identifier) " +
            "FROM Brief h WHERE h.status = 'active' AND h.user =:user ")
    List<ProfileEntityLiteDTO> findByUserId(@Param("user") User user);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class ProfileEntityLiteDTO {
        Long id;
        String name;
        Date dob;
        String gender;
        String identifier;
    }

    List<Brief> findByUser(User user);
}
