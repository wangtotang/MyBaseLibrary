/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tang.common.util;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

public class UrlInterceptor implements Interceptor {

    private boolean intercepted = true;
    private String host;
    private String port;

    public UrlInterceptor setHost(String host) {
        this.host = host;
        return this;
    }

    public UrlInterceptor setPort(String port) {
        this.port = port;
        return this;
    }

    /**
     * 设置是否拦截，默认拦截，
     * 如果某一次请求需要取消拦截，在完成请求后重新设置为true
     * @param intercepted
     */
    public void setIntercepted(boolean intercepted){
        this.intercepted = intercepted;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if(intercepted) {
            HttpUrl newUrl = request.url();
            if (!TextUtils.isEmpty(host)) {
                newUrl = newUrl.newBuilder()
                        .host(host)
                        .build();
            }
            if (!TextUtils.isEmpty(port)) {
                newUrl = newUrl.newBuilder()
                        .port(Integer.parseInt(port))
                        .build();
            }
            request = request.newBuilder()
                    .url(newUrl)
                    .build();
        }
        return chain.proceed(request);
    }
}
