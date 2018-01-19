package edu.psu.unifiedapi.getdiningtimes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class getDiningTimes {

    public static void main(String[] args) throws IOException {
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
                lactaid += fairLife.get(j).text() + " ";
            }
            milk.add(lactaid);
            lactaid = "";
        }
        for(int i = 0; i < milk.size(); i++){
            System.out.println(milk.get(i));
        }
    }
}