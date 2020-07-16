package com.tang.util.device;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * shell工具
 */
public class ShellUtils {

    public static final String COMMAND_SU       = "su";
    public static final String COMMAND_SH       = "sh";
    public static final String COMMAND_EXIT     = "exit\n";
    public static final String COMMAND_LINE_END = "\n";

    private ShellUtils() {
        throw new AssertionError();
    }

    /**
     * 检查是否有root权限
     * 
     * @return
     */
    public static boolean checkRootPermission() {
        return execCommand("echo root", true, false).result == 0;
    }

    /**
     * 执行shell命令
     * @param command  单条命令
     * @param isRoot  是否需要root执行
     * @return
     */
    public static CommandResult execCommand(String command, boolean isRoot) {
        return execCommand(new String[] {command}, isRoot, true);
    }

    /**
     * 执行shell命令
     * @param commands  多条命令
     * @param isRoot    是否需要root执行
     * @return
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot) {
        return execCommand(commands == null ? null : commands.toArray(new String[] {}), isRoot, true);
    }

    /**
     * 执行shell命令
     * @param commands  多条命令
     * @param isRoot    是否需要root执行
     * @return
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot) {
        return execCommand(commands, isRoot, true);
    }

    /**
     * 执行shell命令
     * @param command           单条命令
     * @param isRoot            是否需要root执行
     * @param isNeedResultMsg   是否需要结果信息
     * @return
     */
    public static CommandResult execCommand(String command, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(new String[] {command}, isRoot, isNeedResultMsg);
    }

    /**
     * 执行shell命令
     * @param commands           命令
     * @param isRoot            是否需要root执行
     * @param isNeedResultMsg   是否需要结果信息
     * @return
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(commands == null ? null : commands.toArray(new String[] {}), isRoot, isNeedResultMsg);
    }


    /**
     * 执行shell命令
     * @param commands           命令
     * @param isRoot            是否需要root执行
     * @param isNeedResultMsg   是否需要结果信息
     * @return
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }

                // do not use os.writeBytes(command), avoid chinese charset error
                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();
            // get command result
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
                : errorMsg.toString());
    }

    /**
     * 命令执行结果
     */
    public static class CommandResult {

        /** result of command **/
        public int result;
        /** success message of command result **/
        public String successMsg;
        /** error message of command result **/
        public String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }
}