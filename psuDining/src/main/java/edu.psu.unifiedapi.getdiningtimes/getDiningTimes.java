package edu.psu.unifiedapi.getdiningtimes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class getDiningTimes {

    public static void main(String[] args) throws IOException {
        //main campus dining
        String url = "https://behrendcampusliving.psu.edu/campus-dining";
        Connection.Response res = Jsoup.connect(url)
                .followRedirects(true)
                .method(Connection.Method.POST)
                .execute();

        Document whole = Jsoup.connect(url).get();
        Elements skim = whole.getElementsByClass("field__item even");
        Elements fatFree = skim.get(1).getElementsByTag("tbody");
        ArrayList<String> milk = new ArrayList<>();

        String lactaid = "";

        for (int i = 0; i < fatFree.size(); i++) {
            Elements fairLife = fatFree.get(i).children();

            for(int j = 0; j < fairLife.size(); j++){
                if(j == 0) {
                    lactaid += fairLife.get(j).parent().parent().parent().getElementsByTag("img").get(i).attr("title") + " ";

                    if(lactaid.contains("&#039;")){
                        lactaid = lactaid.replace("&#039;", "'");
                    }

                }
                lactaid += fairLife.get(j).text() + " ";
            }
            lactaid = lactaid.replaceAll(" - ", " to ");
            lactaid = lactaid.replaceAll("\\.","");
            //lactaid = lactaid.replaceAll(": ", " ");
            //lactaid = lactaid.replaceAll("\\s*", "");
            milk.add(lactaid);
            lactaid = "";
        }

        //clipper
        url = "https://behrendcampusliving.psu.edu/behrend-clipper";
        res = Jsoup.connect(url)
                .followRedirects(true)
                .method(Connection.Method.POST)
                .execute();

        whole = Jsoup.connect(url).get();
        skim = whole.getElementsByTag("tbody");
        fatFree = skim.get(0).children();

        lactaid += "Behrend Clipper \n";

        for(int i = 0; i < fatFree.size(); i++){
            lactaid += fatFree.get(i).text() + "\n";
        }

        milk.add(lactaid);

        for(int i = 0; i < milk.size(); i++){
            System.out.println(milk.get(i));
        }
    }
}