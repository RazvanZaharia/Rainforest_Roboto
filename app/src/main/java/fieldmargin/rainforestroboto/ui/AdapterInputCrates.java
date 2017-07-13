package fieldmargin.rainforestroboto.ui;


import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import fieldmargin.rainforestroboto.InputFilterMinMax;
import fieldmargin.rainforestroboto.R;
import fieldmargin.rainforestroboto.SizeUtils;
import fieldmargin.rainforestroboto.holders.ItemOnMap;
import fieldmargin.rainforestroboto.holders.Position;

public class AdapterInputCrates extends RecyclerView.Adapter<AdapterInputCrates.InputCrateViewHolder> {

    int size = 1;

    @Override
    public InputCrateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InputCrateViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_crate_position_input, parent, false));
    }

    @Override
    public void onBindViewHolder(InputCrateViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public void addCrate() {
        size++;
        notifyItemInserted(size - 1);
    }

    public static class InputCrateViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.et_crate_x)
        EditText mEtCrateX;
        @BindView(R.id.et_crate_y)
        EditText mEtCrateY;
        @BindView(R.id.et_crate_quantity)
        EditText mEtCrateQuantity;

        public InputCrateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind() {
            InputFilterMinMax inputFilterForX = new InputFilterMinMax("0", String.valueOf(SizeUtils.mMaxColumns - 1));
            InputFilterMinMax inputFilterForY = new InputFilterMinMax("0", String.valueOf(SizeUtils.mMaxRows - 1));

            mEtCrateX.setFilters(new InputFilter[]{inputFilterForX});
            mEtCrateY.setFilters(new InputFilter[]{inputFilterForY});
        }

        public ItemOnMap getCrate() {
            return !checkCratePosition() ? null :
                    new ItemOnMap(
                            new Position(Integer.parseInt(mEtCrateX.getText().toString()), Integer.parseInt(mEtCrateY.getText().toString())),
                            Integer.parseInt(mEtCrateQuantity.getText().toString()),
                            ItemOnMap.ItemType.Crate);
        }

        private boolean checkCratePosition() {
            return !TextUtils.isEmpty(mEtCrateX.getText()) && !TextUtils.isEmpty(mEtCrateY.getText());
        }

    }

}
