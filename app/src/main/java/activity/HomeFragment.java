package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.sobhy.myapplication.R;
import com.google.gson.Gson;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import adapter.DataManager;
import model.Budget;



public class HomeFragment extends android.support.v4.app.Fragment {
    private static String TAG = FragmentDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private DataManager adapter;
    private View containerView;
    private static String[] titles = null;
    public static final String URL =
            "http://192.168.1.30:45/IISHostingWebSite.svc/json/";

    public  Budget[] array;

    private FragmentDrawer.FragmentDrawerListener drawerListener;

    public HomeFragment() {

    }

    public void setDrawerListener(FragmentDrawer.FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels

        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);


    }
    public void bindRecycler() {

        new SimpleTask(getActivity(),recyclerView).execute(URL);

    }
    private class SimpleTask extends AsyncTask<String, Void, String> {


        RecyclerView recyclerView;
        Activity mContext;

        public  SimpleTask(Activity context,RecyclerView recycler) {
            this.recyclerView=recycler;
            this.mContext=context;
        }
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar


        }

        protected String doInBackground(String... urls) {
            String result = "";
            try {

                HttpGet httpGet = new HttpGet(urls[0]);
                HttpClient client = new DefaultHttpClient();

                HttpResponse response = client.execute(httpGet);

                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    InputStream inputStream = response.getEntity().getContent();
                    BufferedReader reader = new BufferedReader
                            (new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result += line;
                    }
                }

            } catch (ClientProtocolException e) {
                Log.e("error", String.valueOf(e.getMessage()));

            } catch (IOException e) {
                Log.e("error", String.valueOf(e.getMessage()));
            }
            return result;
        }

        protected void onPostExecute(String jsonString) {
            // Dismiss ProgressBar
            showData(jsonString);
        }
    }

    private void showData(String jsonString) {
        Gson gson = new Gson();

        List<Budget> newsItems = Arrays.asList(gson.fromJson(jsonString, Budget[].class));
             array = new Budget[newsItems.size()];
         newsItems.toArray(array); // fill the array
        adapter = new DataManager(array);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Intent intent = new Intent(getActivity(), DetailsActivity.class);
               intent.putExtra(DetailsActivity.ID, array[position].getId());
                startActivity(intent);
/*
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        // the context of the activity
                        getActivity(),

                        // For each shared element, add to this method a new Pair item,
                        // which contains the reference of the view we are transitioning *from*,
                        // and the value of the transitionName attribute
                        new Pair<View, String>(view.findViewById(R.id.CONTACT_circle),
                                getString(R.string.transition_name_circle)),
                        new Pair<View, String>(view.findViewById(R.id.CONTACT_name),
                                getString(R.string.transition_name_name)),
                        new Pair<View, String>(view.findViewById(R.id.CONTACT_phone),
                                getString(R.string.transition_name_phone))
                );
                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
*/
                /*
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
                */

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }

        ));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflating view layout
        final View layout = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.rv);
        bindRecycler();




      /*
        recyclerView.addOnItemTouchListener( // and the click is handled
                new RecyclerClickListener(getActivity(), new RecyclerClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // STUB:
                        // The click on the item must be handled
                    }
                }));
   */
        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }


     static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

         private GestureDetector gestureDetector;
         private ClickListener clickListener;

         public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
             this.clickListener = clickListener;
             gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                 @Override
                 public boolean onSingleTapUp(MotionEvent e) {
                     return true;
                 }

                 @Override
                 public void onLongPress(MotionEvent e) {
                     View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                     if (child != null && clickListener != null) {
                         clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                     }
                 }
             });
         }


         @Override
         public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

             View child = rv.findChildViewUnder(e.getX(), e.getY());
             if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                 clickListener.onClick(child, rv.getChildPosition(child));
             }
             return false;
         }

         @Override
         public void onTouchEvent(RecyclerView rv, MotionEvent e) {
         }

         @Override
         public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

         }


     }


}
