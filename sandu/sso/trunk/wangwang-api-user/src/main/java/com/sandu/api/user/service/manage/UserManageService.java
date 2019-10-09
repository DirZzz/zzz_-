package com.sandu.api.user.service.manage;

import com.github.pagehelper.PageInfo;
import com.sandu.api.user.input.UserAdd;
import com.sandu.api.user.input.UserEdit;
import com.sandu.api.user.input.UserManageSearch;
import com.sandu.api.user.model.UserManageDTO;
import com.sandu.api.user.output.UserBatchExcelVO;

import java.util.List;

public interface UserManageService {

    PageInfo<UserManageDTO> getUserList(UserManageSearch search);

    UserBatchExcelVO addUser(UserAdd userAdd, Long userId);

    void editUser(UserEdit userEdit, Long userId);
}
