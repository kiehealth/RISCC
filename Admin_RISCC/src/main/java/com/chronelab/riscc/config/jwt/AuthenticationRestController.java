package com.chronelab.riscc.config.jwt;

import com.chronelab.riscc.config.AuthenticatedUser;
import com.chronelab.riscc.config.CustomUserDetailsService;
import com.chronelab.riscc.dto.LoginDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.request.general.UserReqDto;
import com.chronelab.riscc.dto.response.general.UserResDto;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.general.DeviceEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.general.UserRepo;
import com.chronelab.riscc.util.ConfigUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.ZoneId;

@RestController
@RequestMapping(value = "/api")
@Api(tags = "Authentication")
public class AuthenticationRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DtoUtil<UserEntity, UserReqDto, UserResDto> dtoUtil;

    @Transactional
    @PostMapping(value = "/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginDto authenticationDetail) throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationDetail.getUsername(),
                        authenticationDetail.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final AuthenticatedUser authenticatedUser = userDetailsService.loadUserByUsername(authenticationDetail.getUsername());
        String token = jwtTokenUtil.generateToken(authenticatedUser);

        //Save user device if from mobile device.
        UserEntity userEntity = userRepo.findById(authenticatedUser.getId()).get();

        if (authenticationDetail.getDeviceToken() != null && authenticationDetail.getDevicePlatform() != null) {
            if (userEntity.getDevice() != null) {
                userEntity.getDevice()
                        .setToken(authenticationDetail.getDeviceToken())
                        .setPlatform(authenticationDetail.getDevicePlatform());
                if (authenticationDetail.getIosNotificationMode() != null) {
                    userEntity.getDevice().setIosNotificationMode(authenticationDetail.getIosNotificationMode());
                }
                userRepo.save(userEntity);
            } else {
                DeviceEntity deviceEntity = new DeviceEntity()
                        .setToken(authenticationDetail.getDeviceToken())
                        .setPlatform(authenticationDetail.getDevicePlatform())
                        .setUser(userEntity);
                if (authenticationDetail.getIosNotificationMode() != null) {
                    deviceEntity.setIosNotificationMode(authenticationDetail.getIosNotificationMode());
                }
                userEntity.setDevice(deviceEntity);
                userRepo.save(userEntity);
            }
        }

        if (userEntity.getDevice() != null) {
            userEntity.getDevice().setLoggedOut(false);
        }

        // Return the token
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Token generated.")
                        .addData("token", token)
                        .addData("expireIn", ConfigUtil.INSTANCE.getExpiration())
                        .addData("user", dtoUtil.prepRes(userEntity))
        );
    }

    @GetMapping(value = "/auth/refresh")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(ConfigUtil.INSTANCE.getTokenHeader());

        if (!token.startsWith("Bearer ")) {
            throw new CustomException("ERR004");
        }
        token = token.substring(7);

        String username = jwtTokenUtil.getUsernameFromToken(token);
        AuthenticatedUser authenticatedUser = userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, Date.from(authenticatedUser.getLastPasswordResetDate().atZone(ZoneId.systemDefault()).toInstant()))) {

            String refreshedToken = jwtTokenUtil.refreshToken(token);

            return ResponseEntity.ok(
                    new ServiceResponse()
                            .setStatus(true)
                            .setMessage("Token refreshed.")
                            .addData("token", refreshedToken)
                            .addData("expireIn", ConfigUtil.INSTANCE.getExpiration())
                            .addData("user", dtoUtil.prepRes(userRepo.findById(authenticatedUser.getId()).get()))
            );

        }
        return ResponseEntity.badRequest().body(new ServiceResponse().setStatus(false).setMessage("Failed to refresh token."));
    }

}
