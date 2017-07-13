package fieldmargin.rainforestroboto.holders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import fieldmargin.rainforestroboto.R;


public class MapCell extends android.support.v7.widget.AppCompatImageView {

    private ItemOnMap mItemInThisCell;

    public MapCell(Context context) {
        super(context);
    }

    public MapCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MapCell(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setItemInThisCell(@Nullable ItemOnMap itemInThisCell) {
        mItemInThisCell = itemInThisCell;
        if (mItemInThisCell != null) {
            switch (mItemInThisCell.getItemType()) {
                case Robot:
                    setImageResource(R.drawable.ic_robot);
                    break;
                case Crate:
                    setImageResource(R.drawable.ic_crate);
                    break;
                case ConveyorBeltFeeder:
                    setImageResource(R.drawable.ic_conveyor_belt);
                    break;
                default:
                    setImageDrawable(null);
            }
        } else {
            setImageDrawable(null);
        }
    }

    public ItemOnMap getItemInThisCell() {
        return mItemInThisCell;
    }
}
