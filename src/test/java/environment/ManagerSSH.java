package environment;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ManagerSSH {




    public static void connectToSSh() {

        String user = "amedzhidov";
//        String password = "";
        String host = "amedzhidov@ibolit.amedzhidov.docdoc.pro";
        int port = 22;

        String seed = "cd ibolit_api/ && php artisan migrate:fresh --seed";


        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
//            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "yes");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");
            System.out.println("Crating SFTP Channel.");
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            System.out.println("SFTP Channel created.");


            InputStream out = null;
            out = sftpChannel.get(seed);

            BufferedReader br = new BufferedReader(new InputStreamReader(out));
            String line;
            while ((line = br.readLine()) != null)
                System.out.println(line);
            br.close();
        } catch (Exception e) {
            System.err.print(e);
        }
    }
}
