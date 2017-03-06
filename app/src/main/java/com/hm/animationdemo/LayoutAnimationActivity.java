package com.hm.animationdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.hm.animationdemo.adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LayoutAnimationActivity extends AppCompatActivity {

    @BindView(R.id.list_view)
    ListView listView;

    private List<String> dataList;
    private ListViewAdapter adapter;

    public static void launch(Context context) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_animation);
        ButterKnife.bind(this);
        dataList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            dataList.add("string" + i);
        }
        adapter = new ListViewAdapter(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
    }

}
