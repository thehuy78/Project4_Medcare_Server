package projectsem4.medcare_server.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projectsem4.medcare_server.domain.dto.CustomResult;
import projectsem4.medcare_server.interfaces.admin.INews;
import projectsem4.medcare_server.interfaces.admin.INews.NewsDto;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/news")
public class NewsController {

  @Autowired
  INews _iNews;

  @PostMapping("getAll")
  public CustomResult GetAll(@RequestBody Map<String, Object> request) {
    String search = (String) request.getOrDefault("search", "");
    Integer page = (request.get("page") instanceof Integer) ? (Integer) request.get("page") : 0;
    Integer size = (request.get("size") instanceof Integer) ? (Integer) request.get("size") : 10;
    return _iNews.GetAll(search, page, size);
  }

  @GetMapping("get/{id}")
  public CustomResult getById(@PathVariable Long id) {
    return _iNews.FindById(id);
  }

  @GetMapping("changeStatus/{id}")
  public CustomResult ChangeStatus(@PathVariable Long id) {
    return _iNews.ChangeStatus(id);
  }

  @PostMapping("create")
  public CustomResult Create(@ModelAttribute NewsDto dto) {
    return _iNews.Create(dto);
  }

  @PostMapping("update")
  public CustomResult Update(@ModelAttribute NewsDto dto) {
    return _iNews.Update(dto);
  }

}
