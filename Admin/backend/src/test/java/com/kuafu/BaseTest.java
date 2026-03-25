package com.kuafu;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;

@Slf4j
public class BaseTest {

    private String pythonCmd = "D:softwareanaconda3envscharts;D:softwareanaconda3envschartsLibrarymingw-w64bin;D:softwareanaconda3envschartsLibraryusrbin;D:softwareanaconda3envschartsLibrarybin;D:softwareanaconda3envschartsScripts;D:softwareanaconda3envschartsbin;D:softwareanaconda3condabin;";

    @Test
    public void test1() {
        String[] commands;
        //commands = new String[]{"/bin/sh", "-c", "echo $PATH"};
        commands = new String[]{"cmd", "/C", "python demo.py"};
        ProcessBuilder processBuilder = new ProcessBuilder();
        Map<String, String> env = processBuilder.environment();
        String path = pythonCmd + env.get("Path");
        env.put("Path", path);
        File workDir = new File("python");
        if (!workDir.exists()) {
            workDir.mkdirs();
        }
        processBuilder.directory(workDir);


        processBuilder.command(commands);
        try {
            // Start the process
            Process process = processBuilder.start();

            // Read output
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Process exited with code " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test2() {
        Long a1 = 156L;
        Long a2 = 156L;

        log.info("{}", a1.equals(a2));
        log.info("{}", a1 == a2);
    }

}






