package com.trishasofttech.bottomnavigationbar.ui.searchbetween;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.trishasofttech.bottomnavigationbar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchBetweenFragment extends Fragment {

    /*to attach the layout xml file with fragment*/
    Button btn_search;
    EditText et_startid, et_endid;
    RecyclerView recyclerView;
    /*to allocate the memory at runtime and store data using key value pair*/
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_searchbetween, container, false);
        btn_search = v.findViewById(R.id.btn_search);
        et_endid = v.findViewById(R.id.et_endid);
        et_startid = v.findViewById(R.id.et_startid);
        recyclerView = v.findViewById(R.id.recyclerview_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchRecord();
            }
        });
        return v;
    }

    private void searchRecord() {
        StringRequest sr = new StringRequest(1, "http://searchkero.com/calldir/searchbetween.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                et_endid.setText("");
                et_startid.setText("");
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jArray = jObj.getJSONArray("data");
                    for(int i = 0; i<jArray.length(); i++)
                    {
                        JSONObject jObj1 = jArray.getJSONObject(i);
                        //tv_record.setText(jObj1.getString("name")+ "\n"+ jObj1.getString("mobile"));
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("idkey", jObj1.getString("id"));
                        hashMap.put("namekey", jObj1.getString("name"));
                        hashMap.put("mobilekey", jObj1.getString("mobile"));
                        hashMap.put("datekey", jObj1.getString("date"));
                        arrayList.add(hashMap);
                    }
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    SearchAdapter searchAdapter = new SearchAdapter();
                    recyclerView.setAdapter(searchAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("startid", et_startid.getText().toString().trim());
                map.put("endid", et_endid.getText().toString().trim());
                return map;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        rq.add(sr);
    }

    private class SearchAdapter extends RecyclerView.Adapter<Searchholder> {
        @NonNull
        @Override
        public Searchholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
            Searchholder searchholder = new Searchholder(v);
            return searchholder;
        }

        @Override
        public void onBindViewHolder(@NonNull Searchholder holder, int position) {
            HashMap<String, String> hashMap = arrayList.get(position);
            holder.tvid.setText(hashMap.get("idkey"));
            holder.tvname.setText(hashMap.get("namekey"));
            holder.tvmobile.setText(hashMap.get("mobilekey"));
            holder.tvdate.setText(hashMap.get("datekey"));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private class Searchholder extends  RecyclerView.ViewHolder{
        TextView tvid, tvname,tvmobile,tvdate;
        public Searchholder(@NonNull View itemView) {
            super(itemView);
            tvid = itemView.findViewById(R.id.tvid);
            tvname = itemView.findViewById(R.id.tvname);
            tvmobile = itemView.findViewById(R.id.tvmobile);
            tvdate = itemView.findViewById(R.id.tvdate);
        }
    }
}
