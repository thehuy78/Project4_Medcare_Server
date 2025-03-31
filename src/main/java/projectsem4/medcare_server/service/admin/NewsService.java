package projectsem4.medcare_server.service.admin;

import java.io.IOException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.domain.entity.Hospital;
import projectsem4.medcare_server.domain.entity.News;
import projectsem4.medcare_server.interfaces.admin.INews;
import projectsem4.medcare_server.interfaces.admin.INotification.NotificationRes;
import projectsem4.medcare_server.repository.admin.NewsRepository;
import projectsem4.medcare_server.repository.admin.UserRepository;
import projectsem4.medcare_server.service.FirebaseStorageService;

@Service
public class NewsService implements INews {

  @Autowired
  NewsRepository _newsRepo;

  @Autowired
  FirebaseStorageService firebaseStorageService;

  @Autowired
  UserRepository userRepo;

  @Override
  public CustomResult GetAll(String search, Integer page, Integer size) {
    try {
      int pages = page != null ? page : 0;
      int sizes = size != null ? size : 10;
      Page<NewsRes> news = _newsRepo.findByFilters(
          search,
          PageRequest.of(pages, sizes));
      return new CustomResult(200, "Get Success", news);
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult FindById(Long id) {
    try {
      var rs = _newsRepo.findById(id);
      if (rs.isPresent()) {
        News n = rs.get();
        NewsRes news = new NewsRes();
        BeanUtils.copyProperties(n, news);
        news.setAvatarUser(n.getUser().getUserDetail().getAvatar());
        news.setFirstName(n.getUser().getUserDetail().getFirstName());
        news.setLastName(n.getUser().getUserDetail().getLastName());
        news.setIdUser(n.getUser().getId());

        return new CustomResult(200, "Get Success", news);
      } else {
        return new CustomResult(300, "Blogs is not exist", null);
      }
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

  @Override
  public CustomResult Create(NewsDto dto) {
    try {
      var isUser = userRepo.findById(dto.getIdUser());
      if (!isUser.isPresent()) {
        return new CustomResult(400, "User is not Exist", null);
      }
      News n = new News();
      n.setTitle(dto.getTitle());
      n.setDescription(dto.getDescription());
      n.setStatus("active");
      if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
        n.setImage(firebaseStorageService.uploadFile(dto.getImageFile()));
      } else {
        n.setImage("");
      }
      n.setUser(isUser.get());
      _newsRepo.save(n);

      return new CustomResult(200, "Create Success", null);

    } catch (Exception e) {
      return new CustomResult(400, "Create Failed", e.getMessage());
    }
  }

  @Override
  public CustomResult Update(NewsDto dto) {
    try {
      var isUser = userRepo.findById(dto.getIdUser());
      if (!isUser.isPresent()) {
        return new CustomResult(400, "User is not Exist", null);
      }
      var isNews = _newsRepo.findById(dto.getId());
      if (!isNews.isPresent()) {
        return new CustomResult(400, "Blogs is not Exist", null);
      }
      News n = isNews.get();
      n.setTitle(dto.getTitle());
      n.setDescription(dto.getDescription());
      if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
        n.setImage(firebaseStorageService.uploadFile(dto.getImageFile()));
      }
      n.setUser(isUser.get());
      _newsRepo.save(n);

      return new CustomResult(200, "Update Success", null);

    } catch (Exception e) {
      return new CustomResult(400, "Update Failed", e.getMessage());
    }
  }

  @Override
  public CustomResult ChangeStatus(Long id) {
    try {
      var rs = _newsRepo.findById(id);
      if (rs.isPresent()) {
        News n = rs.get();
        if (n.getStatus().equalsIgnoreCase("active")) {
          n.setStatus("deactive");
        } else {
          n.setStatus("active");
        }
        _newsRepo.save(n);
        return new CustomResult(200, "Change Status Success", null);
      } else {
        return new CustomResult(300, "Blogs is not exist", null);
      }
    } catch (Exception e) {
      return new CustomResult(400, e.getMessage(), null);
    }
  }

}
