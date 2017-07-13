package fieldmargin.rainforestroboto.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fieldmargin.rainforestroboto.InputFilterMinMax;
import fieldmargin.rainforestroboto.R;
import fieldmargin.rainforestroboto.SizeUtils;
import fieldmargin.rainforestroboto.holders.ItemOnMap;
import fieldmargin.rainforestroboto.holders.MapItems;
import fieldmargin.rainforestroboto.holders.Position;

public class FormDialogFragment extends DialogFragment {

    @BindView(R.id.rv_input_crates)
    RecyclerView mRvInputCrates;
    @BindView(R.id.et_robot_x)
    EditText mEtRobotX;
    @BindView(R.id.et_robot_y)
    EditText mEtRobotY;
    @BindView(R.id.et_conveyor_x)
    EditText mEtConveyorX;
    @BindView(R.id.et_conveyor_y)
    EditText mEtConveyorY;
    @BindView(R.id.tv_scene_dimensions)
    TextView mTvSceneDimensions;

    private AdapterInputCrates mAdapterInputCrates;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnStartListener mOnStartListener;

    public static FormDialogFragment newInstance() {
        return new FormDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_form, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStartListener) {
            mOnStartListener = (OnStartListener) context;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    /**
     * init crates list and set inputFilter for user to be unable to introduce more than max
     */
    private void init() {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRvInputCrates.setLayoutManager(mLayoutManager);

        mAdapterInputCrates = new AdapterInputCrates();
        mRvInputCrates.setAdapter(mAdapterInputCrates);

        mTvSceneDimensions.setText("Scene dimension: X max = " + (SizeUtils.mMaxColumns - 1) + "; Y max = " + (SizeUtils.mMaxRows - 1));

        InputFilterMinMax inputFilterForX = new InputFilterMinMax("0", String.valueOf(SizeUtils.mMaxColumns - 1));
        InputFilterMinMax inputFilterForY = new InputFilterMinMax("0", String.valueOf(SizeUtils.mMaxRows - 1));

        mEtRobotX.setFilters(new InputFilter[]{inputFilterForX});
        mEtRobotY.setFilters(new InputFilter[]{inputFilterForY});

        mEtConveyorX.setFilters(new InputFilter[]{inputFilterForX});
        mEtConveyorY.setFilters(new InputFilter[]{inputFilterForY});
    }

    private boolean checkRobotPosition() {
        return !TextUtils.isEmpty(mEtRobotX.getText()) && !TextUtils.isEmpty(mEtRobotY.getText());
    }

    private boolean checkConveyorPosition() {
        return !TextUtils.isEmpty(mEtConveyorX.getText()) && !TextUtils.isEmpty(mEtConveyorY.getText());
    }

    @OnClick(R.id.btn_add_crate)
    void onAddCrateClick() {
        mAdapterInputCrates.addCrate();
    }

    /**
     * Build the MapItems with all items introduced by user
     */
    @OnClick(R.id.btn_start)
    void onStartClick() {
        MapItems mapItems = new MapItems();
        if (!checkRobotPosition()) {
            Toast.makeText(getContext(), "Check Robot positions", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mapItems.mRobot = getRobot();
        }

        if (!checkConveyorPosition()) {
            Toast.makeText(getContext(), "Check Conveyor positions", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mapItems.mConveyor = getConveyor();
        }

        mapItems.mCrates = getCrates();
        if (mapItems.mCrates == null) {
            Toast.makeText(getContext(), "Check Crates positions", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mOnStartListener != null) {
            mOnStartListener.onStart(mapItems);
            dismiss();
        }
    }

    private ItemOnMap getRobot() {
        return new ItemOnMap(new Position(Integer.parseInt(mEtRobotX.getText().toString()), Integer.parseInt(mEtRobotY.getText().toString())),
                0,
                ItemOnMap.ItemType.Robot);
    }

    private ItemOnMap getConveyor() {
        return new ItemOnMap(new Position(Integer.parseInt(mEtConveyorX.getText().toString()), Integer.parseInt(mEtConveyorY.getText().toString())),
                0,
                ItemOnMap.ItemType.ConveyorBeltFeeder);
    }

    private List<ItemOnMap> getCrates() {
        List<ItemOnMap> crates = null;
        for (int i = 0; i < mAdapterInputCrates.getItemCount(); i++) {
            View viewAtPosition = mLayoutManager.findViewByPosition(i);
            if (viewAtPosition != null) {
                ItemOnMap crate = new AdapterInputCrates.InputCrateViewHolder(viewAtPosition).getCrate();
                if (crate != null) {
                    if (crates == null) {
                        crates = new ArrayList<>();
                    }
                    crates.add(crate);
                }
            }
        }
        return crates;
    }

    public interface OnStartListener {
        void onStart(@NonNull MapItems mapItems);
    }

}
