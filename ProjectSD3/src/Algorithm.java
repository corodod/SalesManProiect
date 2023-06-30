import java.util.Map;

public abstract class Algorithm {
    //    private double k1;
//    private double k2;
//    private double k3;
//    private double k4;
//    private double k5;
    public abstract Map<String, String> getParams();

    public abstract int[] getPath(double[][] matrix, Map<String, String> params);
}
