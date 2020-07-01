package software.zajac.examples.cuboid_api.geom;

public class Bounds {
    public final int xMax;
    public final int xMin;
    public final int yMax;
    public final int yMin;
    public final int zMax;
    public final int zMin;

    public Bounds(int xMax, int xMin, int yMax, int yMin, int zMax, int zMin) {
        this.xMax = xMax;
        this.xMin = xMin;
        this.yMax = yMax;
        this.yMin = yMin;
        this.zMax = zMax;
        this.zMin = zMin;
    }
    public Bounds(int[] vertexData) {

        int i = 0;
        int l = vertexData.length;

        int xMax = vertexData[i];
        int xMin = vertexData[i++];
        int yMax = vertexData[i];
        int yMin = vertexData[i++];
        int zMax = vertexData[i];
        int zMin = vertexData[i++];

        while (i < l) {
            int x = vertexData[i++];
            int y = vertexData[i++];
            int z = vertexData[i++];

            if(x > xMax) xMax = x; else if(x < xMin) xMin = x;
            if(y > yMax) yMax = y; else if(y < yMin) yMin = x;
            if(z > zMax) zMax = z; else if(z < zMin) zMin = z;
        }

        this.xMax = xMax;
        this.xMin = xMin;
        this.yMax = yMax;
        this.yMin = yMin;
        this.zMax = zMax;
        this.zMin = zMin;
    }

    public boolean contains ( int x, int y, int z) {
        if (
            (x >= this.xMin && x <= this.xMax) &&
            (y >= this.yMin && y <= this.yMax) &&
            (z >= this.zMin && z <= this.zMax)
        ) return true;

        return false;
    }
// TODO: add exception for vertex data length not being the multitude of 3
    public boolean contains (int[] vertexData) {
        int i = 0;
        int l = vertexData.length;
        while (i < l) {
            if(contains(vertexData[i++], vertexData[i++], vertexData[i++])) return true;
        }
        return false;
    }
}
