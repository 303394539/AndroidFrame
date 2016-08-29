package com.baic.androidframe.net;

import com.baic.net.api.ApiConnect;
import com.baic.net.api.annotation.PortAnnotation;
import com.baic.net.api.intf.BasePortService;
import com.baic.net.api.intf.Callback;
import com.baic.net.api.model.PagingRequestParam;

/**
 * Created by baic on 16/4/21.
 */
public interface PortService extends BasePortService {

    @PortAnnotation(action = "/global/App/getAppid")
    ApiConnect getAppid(Callback callback);

    @PortAnnotation(action = "/activity/movie/MovieAction/postrank", openid = "oMg0RuH7wfP7ZWCr_kHfA6RYXAtQ", passport = "8efeba551a1c6df13af606666f57c413")
    ApiConnect postrank(PagingRequestParam requestParam, Callback callback, Class<?> itemClass);

    @PortAnnotation(action = "/global/App/queryAppCity")
    ApiConnect queryAppCity(Callback callback);
}
