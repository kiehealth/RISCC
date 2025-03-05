package com.chronelab.riscc.service.impl.general;

import com.chronelab.riscc.entity.general.AuthorityEntity;
import com.chronelab.riscc.entity.general.RoleEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.enums.Authority;
import com.chronelab.riscc.repo.general.AuthorityRepo;
import com.chronelab.riscc.repo.general.CountryRepo;
import com.chronelab.riscc.repo.general.RoleRepo;
import com.chronelab.riscc.repo.general.UserRepo;
import com.chronelab.riscc.service.general.SystemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class SystemServiceImpl implements SystemService {

    private static final Logger LOG = LogManager.getLogger();

    private final AuthorityRepo authorityRepo;
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final CountryRepo countryRepo;

    @Autowired
    public SystemServiceImpl(AuthorityRepo authorityRepo, RoleRepo roleRepo, UserRepo userRepo, CountryRepo countryRepo) {
        this.authorityRepo = authorityRepo;
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.countryRepo = countryRepo;
    }

    @Override
    public void saveDefaultRoleAuthority() {

        if (authorityRepo.count() == 0 && roleRepo.count() == 0) {

            LOG.info("----- Saving Default Roles and Authorities. -----");

            //Authority Authorities
            AuthorityEntity authorityAuthority = new AuthorityEntity().setTitle(Authority.AUTHORITY.toString());

            //Role Authorities
            AuthorityEntity roleAuthority = new AuthorityEntity().setTitle(Authority.ROLE.toString());
            AuthorityEntity roleReadAllAuthority = new AuthorityEntity().setTitle(Authority.ROLE_RA.toString());
            AuthorityEntity roleCreateAuthority = new AuthorityEntity().setTitle(Authority.ROLE_C.toString());
            AuthorityEntity roleUpdateAuthority = new AuthorityEntity().setTitle(Authority.ROLE_U.toString());
            AuthorityEntity roleDeleteAuthority = new AuthorityEntity().setTitle(Authority.ROLE_D.toString());

            //User Authorities
            AuthorityEntity userAuthority = new AuthorityEntity().setTitle(Authority.USER.toString());
            AuthorityEntity userReadAllAuthority = new AuthorityEntity().setTitle(Authority.USER_RA.toString());
            AuthorityEntity userCreateAuthority = new AuthorityEntity().setTitle(Authority.USER_C.toString());
            AuthorityEntity userUpdateAuthority = new AuthorityEntity().setTitle(Authority.USER_U.toString());
            AuthorityEntity userDeleteAuthority = new AuthorityEntity().setTitle(Authority.USER_D.toString());
            AuthorityEntity changePasswordAuthority = new AuthorityEntity().setTitle(Authority.CHANGE_PASSWORD.toString());

            //Country Authorities
            AuthorityEntity countryCreateAuthority = new AuthorityEntity().setTitle(Authority.COUNTRY_C.toString());
            AuthorityEntity countryUpdateAuthority = new AuthorityEntity().setTitle(Authority.COUNTRY_U.toString());
            AuthorityEntity countryDeleteAuthority = new AuthorityEntity().setTitle(Authority.COUNTRY_D.toString());

            //State Authorities
            AuthorityEntity stateCreateAuthority = new AuthorityEntity().setTitle(Authority.STATE_C.toString());
            AuthorityEntity stateUpdateAuthority = new AuthorityEntity().setTitle(Authority.STATE_U.toString());
            AuthorityEntity stateDeleteAuthority = new AuthorityEntity().setTitle(Authority.STATE_D.toString());

            //City Authorities
            AuthorityEntity cityCreateAuthority = new AuthorityEntity().setTitle(Authority.CITY_C.toString());
            AuthorityEntity cityUpdateAuthority = new AuthorityEntity().setTitle(Authority.CITY_U.toString());
            AuthorityEntity cityDeleteAuthority = new AuthorityEntity().setTitle(Authority.CITY_D.toString());

            /* Start Super Admin Authorities */

            List<AuthorityEntity> superAdminAuthorities = new ArrayList<>(
                    Arrays.asList(
                            //Role
                            roleAuthority, roleReadAllAuthority, roleCreateAuthority, roleUpdateAuthority, roleDeleteAuthority,
                            //Authority
                            authorityAuthority,
                            //User
                            userAuthority, userReadAllAuthority, userCreateAuthority, userUpdateAuthority, userDeleteAuthority, changePasswordAuthority,
                            //Country
                            countryCreateAuthority, countryUpdateAuthority, countryDeleteAuthority,
                            //State
                            stateCreateAuthority, stateUpdateAuthority, stateDeleteAuthority,
                            //City
                            cityCreateAuthority, cityUpdateAuthority, cityDeleteAuthority
                    )
            );

            RoleEntity superAdminRole = new RoleEntity().setTitle("Super Admin").setAuthorities(superAdminAuthorities);
            superAdminRole.setCreatedDate(LocalDateTime.now());
            superAdminRole.setLastModifiedDate(LocalDateTime.now());
            /* End Super Admin Role */

            List<RoleEntity> roles = new ArrayList<>();
            roles.add(superAdminRole);

            roleRepo.saveAll(roles);
        }
    }

    @Override
    public void saveMissingAuthority() {
        if (authorityRepo.count() > 0) {
            LOG.info("----- Checking and saving missing authorities. -----");
            List<Authority> authorities = Arrays.asList(Authority.values());
            List<Authority> existingAuthorities = authorityRepo.findAll().parallelStream()
                    .map(authorityEntity -> Authority.valueOf(authorityEntity.getTitle()))
                    .collect(Collectors.toList());
            List<AuthorityEntity> missingAuthorities = new ArrayList<>();
            authorities.forEach(authority -> {
                if (!existingAuthorities.contains(authority)) {
                    missingAuthorities.add(new AuthorityEntity().setTitle(authority.name()));
                }
            });
            RoleEntity roleEntity = roleRepo.findByTitle("Super Admin").get();
            roleEntity.getAuthorities().addAll(missingAuthorities);
        }
    }

    @Override
    public void saveDefaultUser() {
        if (userRepo.count() == 0) {

            LOG.info("----- Saving Default User. -----");

            RoleEntity roleEntity = roleRepo.findByTitle("Super Admin").get();
            UserEntity user = new UserEntity()
                    .setMobileNumber("0000000000")
                    .setEmailAddress("sa@yopmail.com")
                    .setPassword("$2a$04$ND04Es6dx5AtjOuNzmbvPOkrwMCrs2oF9Zj/focP5TG9KeFaZ8okq")
                    .setFirstName("Super")
                    .setLastName("Admin")
                    .setRole(roleEntity)
                    .setLastPasswordResetDate(LocalDateTime.now());
            userRepo.save(user);
        }
    }

    @Override
    public void saveDefaultAddress() {
        /*
        if (countryRepo.count() == 0) {
            LOG.info("----- Saving Default Address. -----");

            List<CountryEntity> countries = new ArrayList<>();

            //Nepal
            CountryEntity countryNepal = new CountryEntity().setName("Nepal").setCode("NP").setDialCode("977");
            countries.add(countryNepal);

            List<StateEntity> statesNepal = new ArrayList<>();
            StateEntity state2 = new StateEntity().setName("State 2").setCountry(countryNepal);

            statesNepal.add(state2);
            countryNepal.setStates(statesNepal);

            //State 2 Cities
            List<CityEntity> citiesState2 = new ArrayList<>(
                    Arrays.asList(
                            new CityEntity().setName("Janakpur").setState(state2),
                            new CityEntity().setName("Bardibas").setState(state2),
                            new CityEntity().setName("Jaleshwor").setState(state2),
                            new CityEntity().setName("Balawa").setState(state2)
                    )
            );

            state2.setCities(citiesState2);

            //India
            CountryEntity countryIndia = new CountryEntity().setName("India").setCode("IN").setDialCode("91");
            countries.add(countryIndia);

            countryRepo.saveAll(countries);
        }*/
    }
}
