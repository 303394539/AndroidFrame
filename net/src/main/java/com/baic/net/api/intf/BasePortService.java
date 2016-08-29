package com.baic.net.api.intf;

import com.baic.net.api.ApiConnect;
import com.baic.net.api.annotation.PortServiceAnnotation;
import com.baic.net.api.annotation.PortAnnotation;

/**
 * Created by baic on 16/4/20.
 */

@PortServiceAnnotation
public interface BasePortService {

    @PortAnnotation(action = "/global/App/getAppid")
    ApiConnect getAppid(Callback callback);
}
