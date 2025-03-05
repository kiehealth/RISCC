package com.chronelab.riscc.service.general;

public interface SystemService {
    void saveDefaultRoleAuthority();

    void saveMissingAuthority();

    void saveDefaultUser();

    void saveDefaultAddress();
}
