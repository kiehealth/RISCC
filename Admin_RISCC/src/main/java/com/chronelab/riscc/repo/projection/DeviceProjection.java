package com.chronelab.riscc.repo.projection;

import com.chronelab.riscc.enums.IOSNotificationMode;

public interface DeviceProjection {
    String getToken();

    IOSNotificationMode getIosNotificationMode();

    Integer getBadgeCount();
}
