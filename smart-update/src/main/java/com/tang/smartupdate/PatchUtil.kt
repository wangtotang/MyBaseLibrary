package com.tang.smartupdate

/**
 * Created by tanghongtu on 2020/6/23.
 */
object PatchUtil {

    init {
        System.loadLibrary("smart-update")
    }
    /**
     * @param oldApkPath
     * @param newApkPath
     * @param patchPath
     * @return 0 成功,  1 失败
     */
    external fun patch(oldApkPath: String, newApkPath: String, patchPath: String): Int
}