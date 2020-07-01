package software.zajac.examples.cuboid_api.io;

// Data Base Operations
public class GetCuboidById implements Runnable {

    private String errorMessage;
    private String result;
    private String id;

    public GetCuboidById(String id){
        this.id = id;
    }

    public synchronized String getErrorMessage(){
        return this.errorMessage;
    }
    public synchronized String getResult(){
        return this.result;
    }

    @Override
    public void run() {
        this.errorMessage = null;
        // TODO: Fetch geom from DB here; update errorMessage in case of unsuccessful call.

    }
}
