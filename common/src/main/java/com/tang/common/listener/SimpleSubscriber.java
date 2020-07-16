/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tang.common.listener;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * 简化RxAndroid订阅器
 */
public abstract class SimpleSubscriber<T> {

//    protected Context context;
//
//    public SimpleSubscriber(Context context) {
//        this.context = context;
//    }
//
//    public abstract void onSuccess(T t);
//
//    public void onFailure(Throwable e) {
//        e.printStackTrace();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (!isNetworkAvailable(context)) {
//            Toast.makeText(context, R.string.net_invalid, Toast.LENGTH_SHORT).show();
//            onError(new NetConnException(context.getString(R.string.net_invalid)));
//        }
//    }
//
//    @Override
//    public void onCompleted() {
//
//    }
//
//    @Override
//    public void onError(Throwable e) {
//        if (e instanceof SocketTimeoutException) {
//            onTimeout((SocketTimeoutException) e);
//        } else if (e instanceof ConnectException) {
//            onNetConnInvalid((ConnectException) e);
//        } else if (e instanceof NetConnException) {
//            e = new ConnectException(e.getMessage());
//            onNetConnInvalid((ConnectException) e);
//        }
//        onFailure(e);
//    }
//
//    public void onNetConnInvalid(ConnectException c) {
//
//    }
//
//    public void onTimeout(SocketTimeoutException e) {
//
//    }
//
//    @Override
//    public void onNext(T t) {
//        onSuccess(t);
//    }
//
//    // 检查是否连接到网络
//    public boolean isNetworkAvailable(Context context) {
//        if (context != null) {
//            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo info = manager.getActiveNetworkInfo();
//            if (info != null)
//                return info.isAvailable();
//        }
//        return false;
//    }

}
