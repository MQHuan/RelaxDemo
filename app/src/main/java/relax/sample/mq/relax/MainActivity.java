package relax.sample.mq.relax;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity
        extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private ListView mListView;

    private List<ContactBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listview);

        String url = Constants.URL+"contact";

        Log.d(TAG, url);
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET,
                                                  url,
                                                  new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                // TODO: 连接成功
                                                                Log.d(TAG, "OK");
                                                                parseJson(response);
                                                            }
                                                        },
                                                  new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error)
                                                            {
                                                                // TODO: 连接失败
                                                                Log.d(TAG, "failed");
                                                                error.printStackTrace();
                                                            }
                                                        });

        queue.add(request);

    }

    private void parseJson(String json) {
        Gson gson = new Gson();
        mDatas = gson.fromJson(json, new TypeToken<List<ContactBean>>(){}.getType());

        mListView.setAdapter(new ImageAdapter());
    }

    private class ImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mDatas != null){
                return mDatas.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mDatas != null){
                return mDatas.get(position);
            }

            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_view, null);

                holder = new ViewHolder();

                convertView.setTag(holder);

                holder.mIvIcon = (ImageView) convertView.findViewById(R.id.item_iv_icon);
                holder.mTvName = (TextView) convertView.findViewById(R.id.item_iv_name);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            ContactBean bean = mDatas.get(position);
            holder.mTvName.setText(bean.name);

            String url = Constants.URL+"image?name="+bean.icon;
            Log.d(TAG, url);
            Picasso.with(MainActivity.this)
                    .load(url)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.error)
                    .into(holder.mIvIcon);


            return convertView;
        }
    }

    private class ViewHolder{
        private ImageView mIvIcon;
        private TextView  mTvName;
    }
}
