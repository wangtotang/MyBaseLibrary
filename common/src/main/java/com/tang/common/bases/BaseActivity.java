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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tang.common.logger.LogUtil;

public class BaseActivity extends AppCompatActivity {

    protected Toast longToast;
    protected Toast shortToast;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.d("activity_lifecycle", this.getClass() + "  onCreate");
        super.onCreate(savedInstanceState);
        processExtraData();
    }

    @Override
    public void recreate() {
        LogUtil.d("activity_lifecycle", this.getClass() + "  recreate");
        super.recreate();
    }

    @Override
    protected void onStart() {
        LogUtil.d("activity_lifecycle",this.getClass() + "  onStart");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        LogUtil.d("activity_lifecycle", this.getClass() + "  onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        LogUtil.d("activity_lifecycle", this.getClass() + "  onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtil.d("activity_lifecycle", this.getClass() + "  onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        LogUtil.d("activity_lifecycle", this.getClass() + "  onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtil.d("activity_lifecycle", this.getClass() + "  onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtil.d("activity_lifecycle", this.getClass() + "  onNewIntent");
        super.onNewIntent(intent);
        setIntent(intent);// 在处理extra之前一定要调用,如果没有调用此句，在getIntent时可能拿不到数据
        processExtraData();
    }

    /**
     * 当launchMode为singleTask时或其他情况时onCreate并不执行，
     * 因此最好覆盖此方法处理intent传值
     */
    protected  void processExtraData(){}

    /**
     * 设置全屏，需在setContentView之前调用
     */
    public void setFullScreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (longToast == null) {
                        longToast = Toast.makeText(getApplicationContext(), msg,
                                Toast.LENGTH_LONG);
                    } else {
                        longToast.setText(msg);
                    }
                    longToast.show();
                }

            });
        }
    }

    /**
     * 显示Toast，以长时间显示
     *
     * @param msg 显示的内容（字符串）
     */
    public void showLongToast(final String msg) {
        if (!TextUtils.isEmpty(msg)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (longToast == null) {
                        longToast = Toast.makeText(getApplicationContext(), msg,
                                Toast.LENGTH_LONG);
                    } else {
                        longToast.setText(msg);
                    }

                    longToast.show();

                }

            });
        }
    }

    /**
     * 显示Toast，以长时间显示
     *
     * @param resId 显示的内容id
     */
    public void showLongToast(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (longToast == null) {
                    longToast = Toast.makeText(
                            BaseActivity.this.getApplicationContext(), resId,
                            Toast.LENGTH_LONG);
                } else {
                    longToast.setText(resId);
                }
                longToast.show();
            }
        });
    }

    /**
     * 显示Toast，以短时间显示
     *
     * @param msg 显示的内容（字符串）
     */
    public void showShortToast(final String msg) {
        if (!TextUtils.isEmpty(msg)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (shortToast == null) {
                        shortToast = Toast.makeText(getApplicationContext(), msg,
                                Toast.LENGTH_SHORT);
                    } else {
                        shortToast.setText(msg);
                    }
                    shortToast.show();
                }

            });
        }
    }

    /**
     * 显示Toast，以短时间显示
     *
     * @param resId 显示的内容id
     */
    public void showShortToast(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (shortToast == null) {
                    shortToast = Toast.makeText(
                            BaseActivity.this.getApplicationContext(), resId,
                            Toast.LENGTH_SHORT);
                } else {
                    shortToast.setText(resId);
                }
                shortToast.show();
            }
        });
    }

    /**
     * 跳转到Activity
     * @param clas              Activity
     * @param isFinishCurrent   是否Finish当前Activity
     */
    public void gotoActivity(Class clas,boolean isFinishCurrent){
        Intent intent=new Intent(this,clas);
        startActivity(intent);
        if(isFinishCurrent){
            finish();
        }
    }
    /**
     * 跳转到Activity,保留当前Activity
     * @param clas              Activity
     */
    public void gotoActivity(Class clas){
        gotoActivity(clas,false);
    }
    
    public void showProgressDialog(final String message,
                                   final DialogInterface.OnCancelListener cancelListener,
                                   final boolean cancelable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog != null) {
                    if(!progressDialog.isShowing()){
                        showDialog(message,cancelListener,cancelable);
                    }
                }else{
                    progressDialog = new ProgressDialog(BaseActivity.this);
                    progressDialog.setCanceledOnTouchOutside(false);
                    showDialog(message,cancelListener,cancelable);
                }
            }
        });
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
            }
        });
    }

}
