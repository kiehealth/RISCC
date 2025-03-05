package com.chronelab.riscc.config;

import com.chronelab.riscc.entity.general.RoleEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.repo.general.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public AuthenticatedUser loadUserByUsername(String username) {
        boolean isMobileNumber = false;
        UserEntity userEntity = userRepo.findByEmailAddressAndEmailAddressVerifiedTrue(username);
        if (userEntity == null) {
            userEntity = userRepo.findByMobileNumberAndMobileNumberVerifiedTrue(username);
            if (userEntity == null) {
                throw new UsernameNotFoundException("User not found.");
            } else {
                isMobileNumber = true;
            }
        }
        return buildUserForAuthentication(userEntity, isMobileNumber);
    }

    private AuthenticatedUser buildUserForAuthentication(UserEntity user, boolean isMobileNumber) {
        String username;
        if (isMobileNumber) {
            username = user.getMobileNumber();
        } else {
            username = user.getEmailAddress();
        }
        return new AuthenticatedUser(username, user.getPassword(), true,
                true, true, true, buildGrantedAuthorityForUser(user.getRole()),
                user.getId(), user.getRole().getTitle(), user.getLastPasswordResetDate());
    }

    private Collection<? extends GrantedAuthority> buildGrantedAuthorityForUser(RoleEntity role) {
        return role.getAuthorities().stream()
                .map(authorityEntity -> new SimpleGrantedAuthority(authorityEntity.getTitle()))
                .collect(Collectors.toList());
    }
}
