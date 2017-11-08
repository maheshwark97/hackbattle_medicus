package byteofpi.com.medicus;

/**
 * Created by root on 4/3/17.
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
public class JSoup {

        public static String main()throws IOException
        {

            try {
                // fetch the document over HTTP
                Document doc = Jsoup.connect("http://quakes.globalincidentmap.com/").get();
                String htmlString = doc.toString();

                String earthquake="var earthquakes";

                int index= (htmlString.indexOf(earthquake));
                int first_int=index;
                while(htmlString.charAt(first_int) != '['){
                    first_int=first_int+1;
                }
                int second_int= first_int+1;
                while(htmlString.charAt(second_int)!=']'){
                    second_int=second_int+1;
                }
                return htmlString.substring(first_int,second_int+1);
                //System.out.println(htmlString.indexOf(earthquake));
                //String earth = (htmlString.substring(index,index+400) );
                //int index2=(earth.indexOf('{'));
                //System.out.println(index2);
                //System.out.println(earth.substring(index2+1));
                //return earth.substring(index2);




            } catch (IOException e) {
                e.printStackTrace();


            }
            return null;
        }
    }
