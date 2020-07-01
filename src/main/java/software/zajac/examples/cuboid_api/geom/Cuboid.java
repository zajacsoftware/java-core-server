package software.zajac.examples.cuboid_api.geom;

public class Cuboid {
    public final String id;
    public final int[] vertices;
    public final int[] edges;
    public final int[] faces;
    public Cuboid(String id, int[]vertices, int[]edges, int[]faces) {
        this.id = id;
        this.vertices = vertices;
        this.edges = edges;
        this.faces = faces;
    }
}
