package com.chronelab.riscc.repo.projection;

public interface UserProjection {
    Long getId();

    String getFirstName();

    String getLastName();

    String getEmailAddress();

    String getMobileNumber();
}
