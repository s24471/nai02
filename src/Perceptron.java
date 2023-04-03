import java.util.ArrayList;
import java.util.Random;

public class Perceptron {
    public double a;
    public double b;
    public double[] w;
    public int ilosc;
    public double O;
    public String looking;
    public Perceptron(int ilosc, String looking) {
        this.ilosc = ilosc;
        w = new double[ilosc];
        Random random = new Random();
        for (int i = 0; i < w.length; i++) {
            w[i] = random.nextDouble() + 0.01;
        }
        this.looking=looking;
        O=1;
        a=0.00001;
        b=0.00001;
    }


    public boolean get(ArrayList<Double> x){
        double suma = 0;
        for (int i = 0; i < w.length; i++) {
            suma+=x.get(i)*w[i];
        }
        return suma >= O;
    }
    public void normalize(){
        double s =0;
        for (double v : w) {
            s += v * v;
        }
        s=Math.sqrt(s);
        for (int i = 0; i < w.length; i++) {
            w[i]/=s;
        }
    }
    public void train(ArrayList<Double> x, String name){
        if(get(x)!=(name.equals(looking))){
            double d = (name.equals(looking)?1:0);
            double y = (get(x)?1:0);
            for (int i = 0; i < w.length; i++) {
                w[i]+=(d-y)*a*x.get(i);
            }
            O = O - (d-y)*b;
            normalize();
        }
    }
}