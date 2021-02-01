package peio.fouillot.projetandroid.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import peio.fouillot.projetandroid.R;

public class ResultAdapter extends ArrayAdapter<StringHolder> {
    private final Context context;
    private final ArrayList<StringHolder> data;
    private final int layoutResourceId;

    public ResultAdapter(Context context, int layoutResourceId, ArrayList<StringHolder> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ListViewHolder listViewHolder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            listViewHolder = new ListViewHolder();
            listViewHolder.textView1 = (TextView)row.findViewById(R.id.row_result_street);
            listViewHolder.textView2 = (TextView)row.findViewById(R.id.row_result_info);
            listViewHolder.textView3 = (TextView)row.findViewById(R.id.row_result_price);

            row.setTag(listViewHolder);
        }
        else
        {
            listViewHolder = (ListViewHolder)row.getTag();
        }

        StringHolder stringHolder = data.get(position);

        listViewHolder.textView1.setText(stringHolder.getResultList0());
        listViewHolder.textView2.setText(stringHolder.getResultList1());
        listViewHolder.textView3.setText(stringHolder.getResultList2());

        return row;
    }
}
