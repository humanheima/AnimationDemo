package com.hm.animationdemo.widget;

import android.content.Context;
import android.widget.ImageView;

// 提供一个用于属性动画操作的图片类。imageLevel不属于ImageView的属性，因此只能自己定义此属性。
// 并通过属性动画进行修改。

/**
 * Created by p_dmweidu on 2023/10/25
 * Desc:
 */
public class LevelImageView extends ImageView {

    public LevelImageView(Context context) {
        super(context);
    }

    private int imageLevel = 0;

    public void setImageLevel(int level) {
        if (this.imageLevel == level) {
            return;
        }
        super.setImageLevel(level);
        this.imageLevel = level;
    }

    public int getImageLevel() {
        return imageLevel;
    }

    // 下一level接口。
    public void nextLevel() {
        setImageLevel(imageLevel++ % maxLevel);
    }

    private int maxLevel = 10;

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }


}