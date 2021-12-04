package deadlinefighters.analyticsframework.plugin.data;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * created by @author Lingzhi Ma.
 * email: lingzhim@andrew.cmu.edu
 * andrewId: lingzhim
 */
public class Demo {
    public static void main(String[] args) throws IOException {
//        var url = new URL("https://financialmodelingprep.com/api/v3/quote-short/AAPL?apikey=YOUR_API_KEY");
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
//            for (String line; (line = reader.readLine()) != null;) {
//                System.out.println(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String command = "python /c start python path\to\script\script.py";
//        Process p = Runtime.getRuntime().exec(command + param );


//        String prg = "import sys \nprint(\"hello\")";
//        BufferedWriter out = new BufferedWriter(new FileWriter("src/main/java/deadlinefighters/analyticsframework/plugin/data/yahooapi.py"));
//        out.write(prg);
//        out.close();
//        Process p = Runtime.getRuntime().exec("python src/main/java/deadlinefighters/analyticsframework/plugin/data/yahooapi.py");
//        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        String ret = in.readLine();
//        System.out.println("value is : "+ret);
//        File file  = new File("src/main/java/deadlinefighters/analyticsframework/plugin/data/yahooapi.py");
//        if(file.delete()){
//            System.out.println("delted");
//        }else{
//            System.out.println("error");
//        }

//        yahooapi();
        marketstockapi();

    }

    private static void marketstockapi() throws IOException{
        String[] cmd = {
            "python",
            "src/main/java/deadlinefighters/analyticsframework/plugin/data/yahooapi.py",
            "goog",
        };
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        try {
            p.waitFor();
            String strCurrentLine;
            while ((strCurrentLine = in.readLine()) != null) {
                System.out.println(strCurrentLine);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void yahooapi() throws IOException {
        String[] cmd = {
            "python",
            "src/main/java/deadlinefighters/analyticsframework/plugin/data/yahooapi.py",
            "goog",
        };
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        try {
            p.waitFor();
            String strCurrentLine;
            while ((strCurrentLine = in.readLine()) != null) {
                System.out.println(strCurrentLine);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
