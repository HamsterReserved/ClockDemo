package space.hamsters.clockdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hamster on 16/7/13.
 *
 * Adapter for lists containing single lines of text (inverse display order)
 */
public class SingleLineListAdapter extends
        RecyclerView.Adapter<SingleLineListAdapter.SingleLineViewHolder> {

    private Context mContext;
    private ArrayList<String> mContent;

    public class SingleLineViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        SingleLineViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.single_line_item_text);
        }
    }

    SingleLineListAdapter(Context context) {
        mContext = context;
        mContent = new ArrayList<>(10);
    }

    @Override
    public SingleLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SingleLineViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.single_line_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SingleLineViewHolder holder, int position) {
        holder.textView.setText(mContent.get(getItemCount() - position - 1)); /* Inverse */
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    public void appendItem(String item) {
        mContent.add(item);
    }

    public void clear() {
        mContent.clear();
    }
}
