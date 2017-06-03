package com.example.sefa.aninterface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sefa on 3.06.2017.
 */

public class DuzListeAdapter extends BaseAdapter {
    private Context context;
    private List<ArrayList> listData;


    public DuzListeAdapter (Context context,List<ArrayList>  listHashMap){
        this.context=context;
        this.listData =listHashMap;

    }
    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ArrayList childArray = (ArrayList) getItem(i);
        if (view == null) {
            view = infalInflater.inflate(R.layout.list_ana_mudahale, null);
        }
        TextView textViewA= (TextView)view.findViewById(R.id.textViewA);
        TextView textViewB= (TextView)view.findViewById(R.id.textViewB);
        TextView textViewC= (TextView)view.findViewById(R.id.textViewC);
        TextView textViewD= (TextView)view.findViewById(R.id.textViewD);
        TextView textViewE= (TextView)view.findViewById(R.id.textViewE);
        TextView textViewF= (TextView)view.findViewById(R.id.textViewF);

        textViewA.setText((String)childArray.get(0));
        textViewB.setText((String)childArray.get(1));
        textViewC.setText((String)childArray.get(2));
        textViewD.setText((String)childArray.get(3));
        textViewE.setText((String)childArray.get(4));
        textViewF.setText((String)childArray.get(5));




        return view;
    }
}
