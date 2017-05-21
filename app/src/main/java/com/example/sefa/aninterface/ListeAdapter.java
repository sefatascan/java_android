package com.example.sefa.aninterface;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sefa on 16.05.2017.
 */

public class ListeAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<ArrayList>> _listDataChild;

    public ListeAdapter(Context context, List<String> listDataHeader,
                           HashMap<String, List<ArrayList>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater infalInflater = (LayoutInflater) this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ArrayList childArray = (ArrayList) getChild(groupPosition, childPosition);
        if(groupPosition == 4){
            if(childArray.size()==1){
                convertView = infalInflater.inflate(R.layout.list_acil_item, null);
                TextView txtListChild = (TextView) convertView.findViewById(R.id.textView_acil_item);
                txtListChild.setText((String)childArray.get(0));
            } else{
                convertView = infalInflater.inflate(R.layout.list_acil_mudahale, null);
                TextView textView2 = (TextView)convertView.findViewById(R.id.textView2);
                TextView textView3 = (TextView)convertView.findViewById(R.id.textView3);
                TextView textView4 = (TextView)convertView.findViewById(R.id.textView4);
                TextView textView5 = (TextView)convertView.findViewById(R.id.textView5);
                TextView textView6 = (TextView)convertView.findViewById(R.id.textView6);

                textView2.setText((String)childArray.get(0));
                textView3.setText((String)childArray.get(1));
                textView4.setText((String)childArray.get(2));
                textView5.setText((String)childArray.get(3));
                textView6.setText((String)childArray.get(4));
            }


        }
        else if(groupPosition == 3){
            if(childArray.size()==1){
                convertView = infalInflater.inflate(R.layout.list_acil_item, null);
                TextView txtListChild = (TextView) convertView.findViewById(R.id.textView_acil_item);
                txtListChild.setText((String)childArray.get(0));
            }else {
                convertView = infalInflater.inflate(R.layout.list_acil_iletisim, null);
                TextView textView7 = (TextView) convertView.findViewById(R.id.textView7);
                TextView textView8 = (TextView) convertView.findViewById(R.id.textView8);
                TextView textView9 = (TextView) convertView.findViewById(R.id.textView9);
                TextView textView10 = (TextView) convertView.findViewById(R.id.textView10);
                TextView textView11 = (TextView) convertView.findViewById(R.id.textView11);
                TextView textView12 = (TextView) convertView.findViewById(R.id.textView12);
                TextView textView13 = (TextView) convertView.findViewById(R.id.textView13);

                textView7.setText((String) childArray.get(0));
                textView8.setText((String) childArray.get(1));
                textView9.setText((String) childArray.get(2));
                textView10.setText((String) childArray.get(3));
                textView11.setText((String) childArray.get(4));
                textView12.setText((String) childArray.get(5));
                textView13.setText((String) childArray.get(6));
            }

        }else{

            convertView = infalInflater.inflate(R.layout.list_acil_item, null);
            TextView txtListChild = (TextView) convertView.findViewById(R.id.textView_acil_item);

            for (int i=0; i<childArray.size();i++)
            {
                txtListChild.setText((String)childArray.get(i));

            }
        }





        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_acil_baslik, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.textView_acil_baslik);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
