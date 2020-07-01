package software.zajac.examples.cuboid_api.io;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

// Data Base Operations
public class GetAll implements Runnable {

    private String errorMessage;
    private String result;

    // TODO: add pagination params: GetAll(int limit, int start)";
    public GetAll(){
    }

    public synchronized String getErrorMessage(){
        return this.errorMessage;
    }
    public synchronized String getResult(){  return this.result; }

    @Override
    public void run() {
        this.errorMessage = null;
        // TODO: Fetch geom from DB here; update errorMessage in case of unsuccessful call.

        // db call mockup. Reading geom from local file system.
        try {
            RandomAccessFile reader = new RandomAccessFile("db-mock.json", "r");
             FileChannel channel = reader.getChannel();
             ByteArrayOutputStream out = new ByteArrayOutputStream();

            int bufferSize = 1024;
            if (bufferSize > channel.size()) {
                bufferSize = (int) channel.size();
            }
            ByteBuffer buff = ByteBuffer.allocate(bufferSize);

            while (channel.read(buff) > 0) {
                out.write(buff.array(), 0, buff.position());
                buff.clear();
            }
            result = new String(out.toByteArray(), StandardCharsets.UTF_8).replace("\n", "");

        } catch (FileNotFoundException e) {
            result = "[]";
        } catch(IOException e){
            this.errorMessage = e.toString();
        }
    }
}
