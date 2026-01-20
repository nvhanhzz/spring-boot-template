package sti.project.template.business.service;

import sti.project.template.base.service.BaseService;
import sti.project.template.business.dto.request.UserRequest;
import sti.project.template.business.dto.response.UserResponse;
import sti.project.template.business.entity.User;

public interface UserService extends BaseService<User, UserResponse, UserRequest> {
}