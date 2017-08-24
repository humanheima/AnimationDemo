package com.hm.animationdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;

import com.hm.animationdemo.R;
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

    public static void launch(Activity context) {
        Intent starter = new Intent(context, LayoutAnimationActivity.class);
        context.startActivity(starter);
        context.overridePendingTransition(R.anim.enter_ainm, R.anim.exit_ainm);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_animation);
        ButterKnife.bind(this);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_animation);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setDelay(0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        listView.setLayoutAnimation(controller);
        dataList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            dataList.add("string" + i);
        }
        adapter = new ListViewAdapter(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
    }

}
