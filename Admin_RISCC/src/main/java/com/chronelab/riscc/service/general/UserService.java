package com.chronelab.riscc.service.general;

import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.PasswordReqDto;
import com.chronelab.riscc.dto.request.general.UserReqDto;
import com.chronelab.riscc.dto.response.general.UserResDto;
import com.chronelab.riscc.repo.projection.UserProjection;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserResDto save(UserReqDto userReqDto) throws IOException;

    PaginationResDto<UserResDto> get(PaginationReqDto paginationReqDto);

    PaginationResDto<UserResDto> getByRole(PaginationReqDto paginationReqDto, Long roleId);

    PaginationResDto<UserProjection> getByRoleTitles(PaginationReqDto paginationReqDto, List<String> roleTitles);

    UserResDto get();

    PaginationResDto<UserProjection> getLimited(List<String> fields, PaginationReqDto paginationReqDto);

    UserResDto update(UserReqDto userReqDto) throws IOException;

    boolean changePassword(PasswordReqDto passwordReqDto);

    void delete(Long userId);

    void logout();
}
