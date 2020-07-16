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
package com.tang.common.bases;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.tang.common.logger.LogUtil;


public class BaseFragment extends Fragment {

    protected Toast longToast;
    protected Toast shortToast;
    protected ProgressDialog progressDialog;

    @Override
    public void onAttach(Context context) {
        LogUtil.d("fragment_lifecycle", getClass() + "   onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtil.d("fragment_lifecycle", getClass() + "   onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("fragment_lifecycle", getClass() + "   onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        LogUtil.d("fragment_lifecycle", getClass() + "   onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        LogUtil.d("fragment_lifecycle", getClass() + "   onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        LogUtil.d("fragment_lifecycle", getClass() + "   onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtil.d("fragment_lifecycle", getClass() + "   onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtil.d("fragment_lifecycle", getClass() + "   onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        LogUtil.d("fragment_lifecycle", getClass() + "   onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtil.d("fragment_lifecycle", getClass() + "   onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        LogUtil.d("fragment_lifecycle", getClass() + "   onDetach");
        super.onDetach();
    }

    /**
     * debug时的Toast,正式发布需要将开关置为false
     * 此处将此开关同Log的开关绑定在一起了
     *
     * @param msg
     */
    public void showDebugToast(final String msg) {
        if (LogUtil.isDebug()) return;
        if (!TextUtils.isEmpty(msg)) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (longToast == null) {
                        longToast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
                    } else {
                        longToast.setText(msg);
                    }
                    longToast.show();
                }

            });
        }
    }

    public void showLongToast(final String msg) {
        if (!TextUtils.isEmpty(msg)&&getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (longToast == null) {
                        longToast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
                    } else {
                        longToast.setText(msg);
                    }
                    longToast.show();
                }
            });
        }
    }

    public void showLongToast(final int resId) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (longToast == null) {
                        longToast = Toast.makeText(getActivity(), resId, Toast.LENGTH_LONG);
                    } else {
                        longToast.setText(resId);
                    }
                    longToast.show();
                }
            });
        }
    }

    public void showShortToast(final String msg) {
        if (!TextUtils.isEmpty(msg)&&getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (shortToast == null) {
                        shortToast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
                    } else {
                        shortToast.setText(msg);
                    }
                    shortToast.show();
                }
            });
        }
    }

    public void showShortToast(final int resId) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (shortToast == null) {
                        shortToast = Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT);
                    } else {
                        shortToast.setText(resId);
                    }
                    shortToast.show();
                }
            });
        }
    }

    public void showProgressDialog(final String message,
                                   final DialogInterface.OnCancelListener cancelListener,
                                   final boolean cancelable) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog != null) {
                        if (!progressDialog.isShowing()) {
                            showDialog(message, cancelListener, cancelable);
                        }
                    } else {
                        progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setCanceledOnTouchOutside(false);
                        showDialog(message, cancelListener, cancelable);
                    }
                }
            });
        }
    }

    private void showDialog(String message,
                            DialogInterface.OnCancelListener cancelListener,
                            boolean cancelable){
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
        progressDialog.setOnCancelListener(cancelListener);
        progressDialog.show();
    }

    public void cancelProgressDialog() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.cancel();
                    }
                }
            });
        }
    }
}
