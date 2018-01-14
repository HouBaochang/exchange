package nuc.onlineeducation.exchange.controller.backend;

import nuc.onlineeducation.exchange.common.ServerResponse;
import nuc.onlineeducation.exchange.model.Project;
import nuc.onlineeducation.exchange.service.IProjectService;
import nuc.onlineeducation.exchange.util.DateTimeUtil;
import nuc.onlineeducation.exchange.vo.ProjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ji YongGuang.
 * @date 11:27 2018/1/14.
 */
@RestController
@RequestMapping(value = "/manage/projects")
public class ProjectManageController {

    @Autowired
    private IProjectService iProjectService;

    /**
     * 删除某个课题
     *
     * @param projectId 课题id
     * @return
     */
    @DeleteMapping("/{id}")
    public ServerResponse removeProject(@PathVariable("id") Integer projectId) {
        return iProjectService.removeProjectById(projectId);
    }

    /**
     * 更改课题信息
     * 1. 用户名 2 . 信息来源  涉及到用户信息不能改
     *
     * @param projectVO projectVO
     * @return
     */
    @PutMapping("/change")
    public ServerResponse updateProject(ProjectVO projectVO) {
        // VO -> 用户名 用户头像 一句话介绍 不能改
        Project project = new Project();
        project.setId(projectVO.getId());
        project.setTitle(projectVO.getTitle());
        project.setDetail(projectVO.getDetail());
        project.setOrigin(projectVO.getOrigin());
        project.setUserId(projectVO.getUserId());
        project.setCreateTime(DateTimeUtil.strToDate(projectVO.getCreateTime()));
        project.setUpdateTime(DateTimeUtil.strToDate(projectVO.getUpdateTime()));
        return iProjectService.updateProject(project);
    }
}
