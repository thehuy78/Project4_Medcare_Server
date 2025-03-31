package projectsem4.medcare_server.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projectsem4.medcare_server.domain.entity.News;
import projectsem4.medcare_server.interfaces.admin.INews.NewsRes;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
  @Query("SELECT new projectsem4.medcare_server.interfaces.admin.INews$NewsRes(" +
      "n.id, n.title, n.image, n.description, n.createDate,n.updateDate, n.status, " +
      "n.user.userDetail.avatar, n.user.userDetail.firstName,n.user.userDetail.lastName, n.user.id) " +
      "FROM News n WHERE " +
      "(:search IS NULL OR :search = '' OR LOWER(n.title) LIKE LOWER(CONCAT('%', :search, '%')))")
  Page<NewsRes> findByFilters(
      @Param("search") String search,
      Pageable pageable);
}
