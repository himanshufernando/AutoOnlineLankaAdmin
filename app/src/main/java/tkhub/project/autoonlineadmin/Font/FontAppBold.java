package tkhub.project.autoonlineadmin.Font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Himanshu on 11/26/2015.
 */
public class FontAppBold extends TextView {
    public FontAppBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FontAppBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontAppBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Font/GOTHICB.TTF");
        setTypeface(tf);
    }

}
