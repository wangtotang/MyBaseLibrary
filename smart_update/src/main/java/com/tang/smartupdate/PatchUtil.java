package com.tang.smartupdate;

/**
 * Created by tanghongtu on 2020/6/23.
 */
public class PatchUtil {

//    static {
//        System.loadLibrary("SmartUpdate");
//    }

    /**
     * @param oldApkPath
     * @param newApkPath
     * @param patchPath
     * @return 0 成功,  1 失败
     */
    public static native int patch(String oldApkPath, String newApkPath, String patchPath);

}
