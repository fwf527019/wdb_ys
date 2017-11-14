package rxh.hb.model;

import org.xutils.common.Callback;

/**
 * Created by Administrator on 2016/4/11.
 * 网络请求返回的类型
 */
public interface Response {

    void onSuccess(String result);

    void onError(Throwable ex);

}
