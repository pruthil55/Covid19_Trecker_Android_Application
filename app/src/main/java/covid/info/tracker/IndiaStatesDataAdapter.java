package covid.info.tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class IndiaStatesDataAdapter extends ArrayAdapter<IndiaStatesData> {

    private Context context;
    private List<IndiaStatesData> stateModelsList;

    public IndiaStatesDataAdapter(Context context, List<IndiaStatesData> stateModelsList) {

        super(context, R.layout.layout_item_list,stateModelsList);

        this.context = context;
        this.stateModelsList = stateModelsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_list,null,true);
        TextView tv_state = view.findViewById(R.id.tv_state);
        TextView tv_confirmed = view.findViewById(R.id.tv_confirmed);
        TextView tv_active = view.findViewById(R.id.tv_active);
        TextView tv_deceased = view.findViewById(R.id.tv_deceased);
        TextView tv_recovered = view.findViewById(R.id.tv_recovered);

        tv_state.setText(stateModelsList.get(position).getTv_state());
        tv_confirmed.setText(stateModelsList.get(position).getTv_confirmed());
        tv_active.setText(stateModelsList.get(position).getTv_active());
        tv_deceased.setText(stateModelsList.get(position).getTv_deceased());
        tv_recovered.setText(stateModelsList.get(position).getTv_recovered());

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up_animation);
        view.startAnimation(animation);

        return view;

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
