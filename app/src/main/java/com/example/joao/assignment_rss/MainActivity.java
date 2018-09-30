package com.example.joao.assignment_rss;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.rss_menu, menu);

        return true;
    }

    class News {
        private String title;
        private String subtitle;
        private String html;

        public News (String title, String subtitle, String html){
            this.title = title.trim();
            this.subtitle = subtitle.trim();
            this.html = html;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }
        public String getHtml() {return html;}
    }

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

//            URL url1, url2 = null;
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            URL[] urlArray;

            try {

                urlArray = new URL[2];
                urlArray[0] = new URL("https://www.winnipegfreepress.com/rss/");
                urlArray[1] = new URL("https://www.cbc.ca/cmlink/rss-topstories");
                arrayOfNews = new ArrayList<News>(10);



                for (URL url : urlArray) {

                    saxParser = SAXParserFactory.newInstance().newSAXParser();
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    inputStream = httpURLConnection.getInputStream();
                    NewsHandler newsHandler = new NewsHandler();
                    saxParser.parse(inputStream, newsHandler);
                }


            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
             catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        //has UI thread access
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

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
               // TextView bt = v.findViewById(R.id.bottomtext);
                if (tt != null) {
                    tt.setText(o.getTitle());
                }
//                if (bt != null) {
//                    bt.setText(o.getSubtitle());
//                }
            }
            return v;
        }
    }

    //class that defines how the SAXParser will parse
    class NewsHandler extends DefaultHandler {

        private boolean inItem, inTitle, inDesc, inLink;
        private String stringTitle, stringSubtitle, stringHtml;

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            Log.d("JZ", "startDocument");
           // arrayOfNews = new ArrayList<News>(10);
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            Log.d("JZ", "endDocument - contents of title arraylist:");
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            Log.d("JZ", "startElement: " + qName);
            if (qName.equals("item")) {
                inItem = true;
            } else if (inItem && qName.equals("title")) {
                inTitle = true;
                stringTitle = "";
            } else if (inItem && qName.equals("description")) {
                inDesc = true;
                stringSubtitle= "";
            } else if (inItem && qName.equals("link")) {
                inLink = true;
                stringHtml= "";
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
            } else if (inItem && qName.equals("link")){
                inLink= false;
            }else if (inItem && qName.equals("description")){
                inDesc= false;
                News newNews = new News(stringTitle,stringSubtitle,stringHtml);
                arrayOfNews.add(newNews);
                stringTitle = "";
                stringSubtitle = "";
                stringHtml="";
            }

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);

            String s, ss, sl = "";

            if (inTitle) {
                s = new String(ch, start, length);
                stringTitle += s;
                s = "";
            }
            else if (inLink){
                sl = new String(ch, start, length);
                stringHtml += sl;
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MainActivity.this, DisplayNews.class);
                intent.putExtra("title", arrayOfNews.get(i).getTitle() );
                intent.putExtra("subtitle", arrayOfNews.get(i).getSubtitle());
                intent.putExtra("link",arrayOfNews.get(i).getHtml());
                startActivity(intent);
            }
        });
    }

}