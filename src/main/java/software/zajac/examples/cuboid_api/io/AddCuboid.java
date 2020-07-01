package software.zajac.examples.cuboid_api.io;
import software.zajac.examples.cuboid_api.geom.Cuboid;
import software.zajac.utils.Json;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;


/**
 * DB operation. Adds a new Cuboid to Data Base.
 */
public class AddCuboid implements Runnable {

    private String errorMessage;

    private Cuboid c;

    public AddCuboid(Cuboid c){
        this.c = c;
    }

    /**
     * Exposed errorMessage in a thread-safe manner.
     * @return The error message or null
     */
    public synchronized String getErrorMessage(){
        return errorMessage;
    }

    @Override
    public void run() {
        this.errorMessage = null;

        // TODO: push geom to DB here; update errorMessage in case of unsuccessful call.

        // DB call mockup. Saving geom to the local file system
        RandomAccessFile stream = null;
        FileChannel channel = null;
        try {
            stream = new RandomAccessFile("db-mock.json", "rw");
            channel  = stream.getChannel();

                FileLock lock = null;
                // simple re-try with limit.
                int tryCout = 0;
                while (lock==null && tryCout < 10) try {
                    lock = channel.tryLock();
                } catch (OverlappingFileLockException e) {
                    channel.close();
                    tryCout++;
                    try {
                        Thread.sleep(100);
                    }catch (InterruptedException ex){
                        break;
                    }
                }

                if(lock==null) { throw new IOException("Can't access geom"); }

            long l = channel.size();
             // Note: line brakes added for easier readability
            if(l==0){
                channel.write(ByteBuffer.wrap(("[\n"+Json.toJson(c).toString()+"\n]").getBytes()));
            } else {
                channel.write(ByteBuffer.wrap((",\n"+Json.toJson(c).toString()+"\n]").getBytes()), l-2);
            }
            lock.release();

        } catch (IOException e) {
            this.errorMessage = e.toString();
        }finally {
           if(stream!=null) {  try { stream.close(); } catch (IOException e) { /*do nothing*/ }}
           if(channel!=null) { try {   channel.close();  } catch (IOException e) { /*do nothing*/ }}
        }
    }
}
