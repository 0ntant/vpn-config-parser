package app.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static app.config.ProcessStatusConfig.*;
import static app.config.XrayConfig.XRAY_BIN_DIR;

@Slf4j
public class XrayUtil
{
    static String xrayBinFile = XRAY_BIN_DIR;
    static  Process process;

    public static int run(String config)
    {
        ProcessBuilder builder = new ProcessBuilder(
                xrayBinFile, "run", "-config", config);
        builder.redirectErrorStream(true);
        try
        {
            process = builder.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            long timeoutMs = 10_000;
            long startTime = System.currentTimeMillis();

            String message;
            while (true)
            {
                if (System.currentTimeMillis() - startTime > timeoutMs) {
                    process.destroy();
                    return RUN_TIMEOUT;
                }

                if (reader.ready()) {
                    message = reader.readLine();
                    if (message == null)
                    {
                        break;
                    }

                    if (message.toLowerCase().contains("started")) {
                        return RUN_SUCCESS;
                    }
                }
                else
                {
                    Thread.sleep(100);
                }

                try
                {
                    int exitCode = process.exitValue();
                    log.error("ExitCode: {}", exitCode);
                    return RUN_ERROR;
                }
                catch (IllegalThreadStateException e)
                {

                }
            }

            return RUN_ERROR;
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return RUN_ERROR;
        }
    }

    public static boolean isAlive()
    {
        return process.isAlive();
    }

    public static void stop()
    {
        if (process != null && process.isAlive())
        {
            ProcessHandle handle = process.toHandle();
            handle.destroyForcibly();

            try
            {
                process.waitFor();
            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }
}
