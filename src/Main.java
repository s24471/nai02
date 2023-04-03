import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static ArrayList<Value> arr;
    public static ArrayList<Value> test;
    public static String lookingFor = "Iris-setosa" ;
    static int ilosc =-1;
    public static ArrayList<Double> maxs;
    public static ArrayList<Double> mins;
    public static void main(String[] args) {


        Scanner scanner=new Scanner(System.in);
        try {
            maxs = new ArrayList<>();
            mins = new ArrayList<>();
            arr = new ArrayList<>();
            Scanner s=new Scanner(new File("iris_training.txt"));
            System.out.println("Czy wlaczyc normalizacje danych do przedzialu 0-1? (1-tak, 0-nie)");
            int opt= scanner.nextInt();
            while(s.hasNext()){
                Value value = new Value(s.nextLine().split("\t"));
                arr.add(value);
                if(opt==1) {
                    if (maxs.size() == 0) {
                        maxs.addAll(value.arr);
                        mins.addAll(value.arr);
                    } else {
                        for (int i = 0; i < maxs.size(); i++) {
                            double curr = value.arr.get(i);
                            if (curr > maxs.get(i)) {
                                maxs.set(i, curr);
                            }
                            if (curr < mins.get(i)) {
                                mins.set(i, curr);
                            }
                        }
                    }
                }
            }
            s.close();
            if(opt==1) {
                for (Value value : arr) {
                    for (int i = 0; i < value.arr.size(); i++) {
                        value.arr.set(i, (value.arr.get(i) - mins.get(i)) / (maxs.get(i) - mins.get(i)));
                    }
                }
            }

            test = new ArrayList<>();
            Scanner s2 = new Scanner(new File("iris_test.txt"));
            while(s2.hasNext()) {
                Value value = new Value(s2.nextLine().split("\t"));
                test.add(value);
                if (opt == 1) {
                    for (int i = 0; i < value.arr.size(); i++) {
                        value.arr.set(i, (value.arr.get(i) - mins.get(i)) / (maxs.get(i) - mins.get(i)));
                    }
                }
            }
            s2.close();
            Perceptron perceptron = new Perceptron(ilosc-1, lookingFor);
            for(double w: perceptron.w){
                System.out.print(w + " ");
            }
            System.out.println();
            System.out.println("O: "+ perceptron.O);
            System.out.println("===");
            while(true){
                for (Value v : arr) {
                    perceptron.train(v.arr, v.name);
                }
                int good = accurate(perceptron, arr);
                for(double w: perceptron.w){
                    System.out.print(w + " ");
                }
                System.out.println();
                System.out.println(perceptron.O);
                System.out.println("===");
                if(good==arr.size())break;
            }
            manualInput(perceptron, opt);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static int accurate(Perceptron perceptron, ArrayList<Value> arr) {
        int good =0;
        for(Value v: arr){
            if(perceptron.get(v.arr)==((v.name.equals(lookingFor)))){
                good++;
            }
        }
        System.out.println("dobrze: " + good + "/" + arr.size());
        System.out.println((double)good/(arr.size())*100 +"%");
        return good;
    }

    public static void manualInput(Perceptron perceptron, int opt) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Wprowadź wektor atrybutów (oddzielone spacjami) lub wpisz 'exit', aby zakończyć:");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            input = input.replaceAll(",", ".");
            String[] inputArray = input.split(" ");
            if (inputArray.length != ilosc-1) {
                System.out.println("Wprowadzono niepoprawną liczbę atrybutów. Spróbuj ponownie.");
                continue;
            }
            ArrayList<Double> attributes = new ArrayList<>();
            try {
                for (String attribute : inputArray) {
                    attributes.add(Double.parseDouble(attribute));
                }
            } catch (NumberFormatException e) {
                System.out.println("Wprowadzono niepoprawne wartości atrybutów. Spróbuj ponownie.");
                continue;
            }
            if(opt==1) {
                for (int i = 0; i < attributes.size(); i++) {
                    attributes.set(i, (attributes.get(i) - mins.get(i)) / (maxs.get(i) - mins.get(i)));
                }
            }
            boolean result = perceptron.get(attributes);
            System.out.println("Wynik klasyfikacji: " + (result ? lookingFor : "Inna klasa"));
        }
    }


}
class Value{
    ArrayList<Double> arr;
    String name;
    public Value(String[] s) {
        arr = new ArrayList<>();
        if(Main.ilosc == -1) Main.ilosc = s.length;

        if(Main.ilosc == s.length){
            name = s[s.length-1].strip();
        }else{
            name = "#null#";
        }
        for (int i = 0; i < s.length-1; i++) {
            arr.add(Double.parseDouble(s[i].replace(',', '.').strip()));
        }
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for(Double d: arr){
            ans.append(d.toString()).append("\t");
        }

        return ans.toString();
    }

}