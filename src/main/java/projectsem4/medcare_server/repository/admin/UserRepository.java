package projectsem4.medcare_server.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
import projectsem4.medcare_server.domain.entity.User;
import projectsem4.medcare_server.interfaces.admin.IUser.UserAdminRes;
import projectsem4.medcare_server.interfaces.admin.IUser.UserCustomerRes;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        User findByEmail(String email);

        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IUser$UserCustomerRes(" +
                        "d.id, d.email, d.userDetail.firstName, d.userDetail.lastName, d.userDetail.avatar, d.userDetail.role, d.userDetail.verify, "
                        +
                        "d.userDetail.balance, d.userDetail.createDate, d.userDetail.updateDate, d.userDetail.status) "
                        +
                        "FROM User d WHERE " +
                        "(:role IS NULL OR :role = '' OR LOWER(d.userDetail.role) LIKE LOWER(:role)) AND " +
                        "(:status IS NULL OR :status = '' OR LOWER(d.userDetail.status) LIKE LOWER(:status)) AND " +
                        "(:search IS NULL OR :search = '' OR LOWER(d.email) LIKE LOWER(CONCAT('%', :search, '%')))")
        Page<UserCustomerRes> findCustomerByFilters(
                        @Param("role") String role,
                        @Param("status") String status,
                        @Param("search") String search,
                        Pageable pageable);

        @Query("SELECT new projectsem4.medcare_server.interfaces.admin.IUser$UserAdminRes(" +
                        "d.id, d.email, d.userDetail.firstName, d.userDetail.lastName, d.userDetail.avatar, d.userDetail.role, d.userDetail.verify, "
                        +
                        "d.userDetail.createDate, d.userDetail.updateDate, d.userDetail.status,d.userDetail.hospital.name) "
                        +
                        "FROM User d WHERE " +
                        "(:role IS NULL OR :role = '' OR LOWER(d.userDetail.role) LIKE LOWER(:role)) AND " +
                        "(:status IS NULL OR :status = '' OR LOWER(d.userDetail.status) LIKE LOWER(:status)) AND " +
                        "(:search IS NULL OR :search = '' OR LOWER(d.email) LIKE LOWER(CONCAT('%', :search, '%')))")
        Page<UserAdminRes> findAdminByFilters(
                        @Param("role") String role,
                        @Param("status") String status,
                        @Param("search") String search,
                        Pageable pageable);

        @Query("SELECT u FROM User u WHERE" +
                        "(:role IS NULL OR :role = '' OR LOWER(u.userDetail.role) LIKE LOWER(:role))")
        List<User> GetUserByRole(@Param("role") String role);
}
