package cz.thewhiterabbit.electronicapp.model.similation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SpiceRunnerTest {
    String spiceResource = getClass().getResource("ngspice/bin/ngspice_con.exe").toExternalForm();
    //private String[] argumString = new String[]{"start", spiceResource, "-b", "ex01.cir", "-o", "test.txt"};
    private String[] argumString = new String[]{"cmd.exe", "/c", "start", spiceResource, "-b", "ex01.cir", "-o", "test_2.txt"};

    public int exec(){
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try{
            process = runtime.exec(argumString);
            System.out.print(loadStream(process.getInputStream()));
            System.err.print(loadStream(process.getErrorStream()));
        }catch (Exception e){
            System.out.println(e);
        }

        try {
            int exitCode = process.waitFor();
            return exitCode;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    static String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();
        while( (ptr = in.read()) != -1 ) {
            buffer.append((char)ptr);
        }
        return buffer.toString();
    }
}
