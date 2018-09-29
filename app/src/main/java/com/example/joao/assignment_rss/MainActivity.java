package com.example.joao.assignment_rss;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {


    private ArrayList<News> arrayOfNews;
    private NewsAdapter newsAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RssTask rssTask = new RssTask();
        rssTask.execute();

        // 2 fase


//        int i = 0;
//        for (String news : title)
//        {
//            i+=1;
//            News newsToInsert = new News(news);
//            arrayOfNews.add(newsToInsert);
//        }
//        newsAdapter = new NewsAdapter(this, R.layout.list_item, arrayOfNews);

//********************************************************
    }

    class News {
        private String title;
        private String subtitle;

        public News (String title, String subtitle){
            this.title = title.trim();
            this.subtitle = subtitle.trim();
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }
    }
//
//    private class NewsAdapter extends ArrayAdapter<News> {
//
//        private ArrayList<News> items;
//
//        public NewsAdapter(Context context, int textViewResourceId, ArrayList<News> items) {
//            super(context, textViewResourceId, items);
//            this.items = items;
//        }
//    }

    //********************************************************************
    //nested async task to download and parse RSS feed
    class RssTask extends AsyncTask<Void, Void, Void> {
        private SAXParser saxParser;


        //has UI thread access
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("JZ", "pre execute!");

        }

        //The "main" method of the thread
        //NO UI thread access
        @Override
        protected Void doInBackground(Void... voids) {

            //use factory to create parser
            try {
                saxParser = SAXParserFactory.newInstance().newSAXParser();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            //URL object for RSS or Atom feed location
            URL url = null;
            try {
                url = new URL("https://www.winnipegfreepress.com/rss/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            //open the HTTP connection and get the input stream
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            try {
                httpURLConnection = (HttpURLConnection)url.openConnection();
                inputStream = httpURLConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //create our custom handler
            NewsHandler newsHandler = new NewsHandler();

            //parse the RSS input stream using our custom handler!
            try {
                saxParser.parse(inputStream, newsHandler);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            /*try {
                Thread.sleep(5000); //5 second sleep
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/


            return null;
        }

        //has UI thread access
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Jody", "post execute!" );
            //ArrayList<News> test = arrayOfNews;

            listViewPopulation(arrayOfNews);

        }
    } //asynctask

    private class NewsAdapter extends ArrayAdapter<News>{

        private ArrayList<News> arrayfNews;

        public NewsAdapter(Context context, int textViewResourceId, ArrayList<News> arrayfNews){
            super(context, textViewResourceId, arrayfNews);
            this.arrayfNews = arrayfNews;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }
            News o = arrayfNews.get(position);
            if (o != null) {
                TextView tt = v.findViewById(R.id.toptext);
                TextView bt = v.findViewById(R.id.bottomtext);
                if (tt != null) {
                    tt.setText(o.getTitle());
                }
                if (bt != null) {
                    bt.setText(o.getSubtitle());
                }
            }
            return v;
        }
    }

    //class that defines how the SAXParser will parse
    class NewsHandler extends DefaultHandler {
        //ArrayList to hold titles of articles
        //private ArrayList<String> title;
        //Better to have an ArrayList of FeedItem or NewsItem (custom class objects)

        //boolean flags to keep track of which elements we are in
        private boolean inItem, inTitle, inDesc;

        //String to build with muliple calls to characters() for one element
        private String stringTitle, stringSubtitle;

//        public ArrayList<String> getTitle() {
//            return title;
//        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            Log.d("JZ", "startDocument");
            arrayOfNews = new ArrayList<News>(10);
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            Log.d("JZ", "endDocument - contents of title arraylist:");
            //makeNews();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            Log.d("JZ", "startElement: " + qName);
            if (qName.equals("item")) {
                inItem = true;
            } else if (inItem && qName.equals("title")) {
                inTitle = true;
                stringTitle = new String();
            } else if (inItem && qName.equals("description")){
                inDesc = true;

            }

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            Log.d("JZ", "endElement: " + qName);

            if (qName.equals("item")) {
                inItem = false;
            } else if (inItem && qName.equals("title")) {
                inTitle = false;
                //add the title to the title array
                //title.add(stringTitle);
            } else if (inItem && qName.equals("description")){
                inDesc= false;
                News newNews = new News(stringTitle,stringSubtitle);
                arrayOfNews.add(newNews);
                stringTitle = "";
                stringSubtitle = "";
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            //just for debugging...
            //String s = new String(ch, start, length);
            //Log.d("Jody", "characters: " + s);

            String s, ss = "";

            //check flags set by start/endElement()
            if (inTitle) {
                s = new String(ch, start, length);
                stringTitle += s;
                s = "";
                //Use a StringBuilder instead of just Strings
                //stringBuilder.append(ch, start, length);
            }
            else if (inDesc){
                ss = new String(ch, start, length);
                stringSubtitle += ss;
            }
        }
    }

    private void listViewPopulation(ArrayList<News> arrayfNews){
        newsAdapter = new NewsAdapter(this, R.layout.list_item,arrayfNews);

        listView = findViewById(R.id.my_list_view);
        listView.setAdapter(newsAdapter);
    }

}