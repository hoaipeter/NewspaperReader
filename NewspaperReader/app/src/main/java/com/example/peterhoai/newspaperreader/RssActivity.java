package com.example.peterhoai.newspaperreader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by Peter Hoai on 8/13/2016.
 */
public class RssActivity  extends AppCompatActivity {
    private ListView listView;
    private ArrayList listNews;
    private NewsAdapter adapter;
    private String[] rssURLList;
    private String[] rssTopic;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_activity);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<String> arrL = getIntent().getStringArrayListExtra("url");//getResources().getStringArray(R.array.rss_url_list);
        rssURLList = new String[arrL.size()];
        arrL.toArray(rssURLList);

        ArrayList<String> arrLT = getIntent().getStringArrayListExtra("topic");//getResources().getStringArray(R.array.rss_url_list);
        rssTopic = new String[arrLT.size()];
        arrLT.toArray(rssTopic);

        listView = (ListView) findViewById(R.id.list_view);
        listNews = new ArrayList();
        adapter = new NewsAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News news = adapter.getItem(i);
                Intent intent = new Intent(RssActivity.this, DetailActivity.class);
                intent.putExtra("link", news.getLink());
                startActivity(intent);
                Log.d("link", news.getLink());
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                //String rssSt = rssURLList[0];
                int index = id - R.id.nav_trangchu;
                if (index >= rssURLList.length || rssURLList[index].isEmpty() )
                {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    Toast.makeText(RssActivity.this, "No information in this article", Toast.LENGTH_SHORT).show();
                    return true;
                }
                showLoadingDialog("Loading data");
                new XMLReader().execute(rssURLList[index]);

                getSupportActionBar().setTitle(rssTopic[index]);

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        showLoadingDialog("Loading data");
        new XMLReader().execute(rssURLList[0]);
    }

    public void BackHome(View view) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        finish();
    }

    private class XMLReader extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //listNews = new ArrayList();
            listNews.clear();

            XMLDOMParser parser = new XMLDOMParser();
            Document doc = parser.getDocument(s);
            NodeList nodeList = doc.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                News news = new News();
                Element e = (Element)nodeList.item(i);

                NodeList title = e.getElementsByTagName("title");
                Element titleElement = (Element)title.item(0);
                news.setTitle(titleElement.getFirstChild().getNodeValue());

                NodeList link = e.getElementsByTagName("link");
                Element linkElement = (Element)link.item(0);
                news.setLink(linkElement.getFirstChild().getNodeValue());

                listNews.add(news);
            }

            adapter.clear();
            adapter.addAll(listNews);
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            result = getXmlFromUrl(strings[0]);
            return result;
        }
    }

    private String getXmlFromUrl(String string) {
        String result = "";

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(string);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private void showLoadingDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if( id == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
