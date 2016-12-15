package tkhub.project.autoonlineadmin.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tkhub.project.autoonlineadmin.Font.TextViewFontAwesome;
import tkhub.project.autoonlineadmin.R;


/**
 * Created by Himanshu on 7/22/2016.
 */
public class NavigationDrawer extends BaseAdapter {

    Context mContext;
    ArrayList<NavigationDrawerItem> mNavItems;

    private int selectedItem = -1;
    public NavigationDrawer(Context context, ArrayList<NavigationDrawerItem> navItems) {
        mContext = context;
        mNavItems = navItems;
    }
    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_navigation, null);
        } else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.textViewNavigationName);
        TextViewFontAwesome iconView = (TextViewFontAwesome) view.findViewById(R.id.imageViewnavigationIcon);

        titleView.setText(mNavItems.get(position).mTitle);
        iconView.setText(mNavItems.get(position).mImage);

        highlightItem(position,convertView);
        return view;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void highlightItem(int position, View result) {
        if(position == selectedItem) {
            result.setBackgroundColor(mContext.getResources().getColor(R.color.appLightBlue));
        } else {

        }
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }
}
