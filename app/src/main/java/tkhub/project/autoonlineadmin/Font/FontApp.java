package tkhub.project.autoonlineadmin.Font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Himanshu on 11/26/2015.
 */
public class FontApp extends TextView {
    public FontApp(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FontApp(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontApp(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Font/GOTHIC.TTF");
        setTypeface(tf);
    }

}
