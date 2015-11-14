package adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;
import com.example.sobhy.myapplication.R;

import model.Budget;
import model.Contact;

/**
 * Created by SOBHY on 21/09/2015.
 */
public class DataManager extends RecyclerView.Adapter<DataManager.RecyclerViewHolder> {

    private Budget[] budgetArray;

    public DataManager(Budget[] budgetArrayList){

        this.budgetArray = budgetArrayList;
    }
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView mName, mPhone;
        View mCircle;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.CONTACT_name);
            mPhone = (TextView) itemView.findViewById(R.id.CONTACT_phone);
        //    mCircle = itemView.findViewById(R.id.CONTACT_circle);

        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_row, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int i) {
        // get the single element from the main array
        final Budget budget = budgetArray[i];
        // Set the values
        viewHolder.mName.setText(budget.get(Budget.Field.Budget_code));
        viewHolder.mPhone.setText(budget.get(Budget.Field.Budget_name));
        // Set the color of the shape
    //    GradientDrawable bgShape = (GradientDrawable) viewHolder.mCircle.getBackground();
    // bgShape.setColor(Color.parseColor(contact.get(Contact.Field.COLOR)));
    }

    @Override
    public int getItemCount() {
        return budgetArray.length;
    }


}