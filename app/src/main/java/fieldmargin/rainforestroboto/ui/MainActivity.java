package fieldmargin.rainforestroboto.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fieldmargin.rainforestroboto.R;
import fieldmargin.rainforestroboto.SizeUtils;
import fieldmargin.rainforestroboto.holders.ItemOnMap;
import fieldmargin.rainforestroboto.holders.MapCell;
import fieldmargin.rainforestroboto.holders.MapItems;
import fieldmargin.rainforestroboto.holders.Position;

public class MainActivity extends AppCompatActivity implements FormDialogFragment.OnStartListener {

    @BindView(R.id.gr_scene)
    GridLayout mGrScene; // the scene

    @BindView(R.id.tv_robot_status)
    TextView mTvRobotStatus;
    @BindView(R.id.tv_conveyor_status)
    TextView mTvConveyorStatus;
    @BindView(R.id.tv_crate_status)
    TextView mTvThisCrateStatus;

    /**
     * this variable holds the scene matrix with every cell ID
     */
    int[][] mSceneMatrix;
    /**
     * this variable holds all the items in the scene
     */
    private MapItems mMapItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mGrScene.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGrScene.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                startGame();
            }
        });
    }

    /**
     * Calculate scene dimensions, add cells to scene and open form input for every item position
     */
    private void startGame() {
        if (SizeUtils.mMaxColumns == 0 || SizeUtils.mMaxRows == 0) {
            SizeUtils.calculateDimensionOfScene(getResources().getDimensionPixelSize(R.dimen.cell_size),
                    mGrScene.getMeasuredWidth(),
                    mGrScene.getMeasuredHeight());
        }

        addCellsToScene(SizeUtils.mMaxRows, SizeUtils.mMaxColumns);

        FormDialogFragment.newInstance().show(getSupportFragmentManager(), "");
    }

    /**
     * Adds an MapCell to scene's GridLayout and saves the cell's id into mSceneMatrix
     */
    private void addCellsToScene(int maxRows, int maxCols) {
        mGrScene.removeAllViewsInLayout();
        int cellSize = getResources().getDimensionPixelSize(R.dimen.cell_size);
        int cellPadding = getResources().getDimensionPixelSize(R.dimen.cell_padding);
        int cellIndex = 0;
        mSceneMatrix = new int[maxRows][maxCols];
        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < maxCols; col++) {
                final MapCell mapCell = new MapCell(this);
                mapCell.setPadding(cellPadding, cellPadding, cellPadding, cellPadding);
                mapCell.setBackgroundResource(R.drawable.bg_black_border);

                mapCell.setId(cellIndex);
                mSceneMatrix[row][col] = cellIndex;
                cellIndex++;

                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.height = cellSize;
                param.width = cellSize;
                param.rowSpec = GridLayout.spec(row);
                param.columnSpec = GridLayout.spec(col);
                mapCell.setLayoutParams(param);
                mGrScene.addView(mapCell);
            }
        }
    }

    /**
     * @param cell to update status
     */
    private void updateStatusForRobotAndCell(@Nullable ItemOnMap cell) {
        if (cell != null) {
            switch (cell.getItemType()) {
                case Crate:
                    updateStatusForRobotAndCrate(cell);
                    break;
                case ConveyorBeltFeeder:
                    updateStatusForRobotAndConveyor();
                    break;
                default:
                    updateCrateStatus(null);
            }
        } else {
            updateCrateStatus(null);
        }
    }

    /**
     * @param crate to update Crate status
     */
    private void updateStatusForRobotAndCrate(@NonNull ItemOnMap crate) {
        updateRobotStatus();
        updateCrateStatus(crate);
    }

    private void updateStatusForRobotAndConveyor() {
        updateRobotStatus();
        updateConveyorStatus();
    }

    private void updateRobotStatus() {
        mTvRobotStatus.setText(String.format(getString(R.string.robot_quantity), mMapItems.mRobot.getQuantity()));
    }

    private void updateCrateStatus(@Nullable ItemOnMap crane) {
        if (crane == null) {
            mTvThisCrateStatus.setText("");
        } else {
            mTvThisCrateStatus.setText(String.format(getString(R.string.crate_quantity), crane.getQuantity()));
        }
    }

    private void updateConveyorStatus() {
        mTvConveyorStatus.setText(String.format(getString(R.string.conveyor_quantity), mMapItems.mConveyor.getQuantity()));
    }

    /**
     * This method moves the robot to it's next position and updates the status of robot
     * and crate, if the next cell contains a crate
     *
     * @param newPosition of the robot
     */
    private void updateRobotPosition(@NonNull Position newPosition) {
        MapCell oldCellForRobot = getMapCellAtPosition(mMapItems.mRobot.getPosition());
        if (oldCellForRobot.getItemInThisCell().getItemType() == ItemOnMap.ItemType.Robot) {
            oldCellForRobot.setItemInThisCell(null);
        } else {
            oldCellForRobot.setBackgroundResource(R.drawable.bg_black_border);
        }

        mMapItems.mRobot.setPosition(newPosition);
        MapCell nextCellForRobot = getMapCellAtPosition(mMapItems.mRobot.getPosition());

        if (nextCellForRobot.getItemInThisCell() == null) {
            nextCellForRobot.setItemInThisCell(mMapItems.mRobot);
        } else {
            nextCellForRobot.setBackgroundResource(R.drawable.bg_red_with_black_border);
        }

        updateStatusForRobotAndCell(nextCellForRobot.getItemInThisCell());
    }

    /**
     * @param position in scene
     * @return the MapCell at that position or null if position is outOfBounds
     */
    private MapCell getMapCellAtPosition(@NonNull Position position) {
        try {
            return ((MapCell) findViewById(mSceneMatrix[position.getYpos()][position.getXpos()]));
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private void onRobotDropsBagWhereNoConveyor() {
        onGameOver("Robot drops bag in wrong place");
    }

    private void onRobotPicksBagWhereNoCrate() {
        onGameOver("Robot picks bag from wrong place");
    }

    private void onGameOver(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startGame();
                    }
                })
                .setNeutralButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }

    @OnClick(R.id.btn_north)
    void onNorthClick() {
        Position newRobotPosition = new Position(mMapItems.mRobot.getPosition().getXpos(), mMapItems.mRobot.getPosition().getYpos() - 1);
        if (newRobotPosition.getYpos() < 0) {
            Toast.makeText(this, "Invalid move", Toast.LENGTH_SHORT).show();
        } else {
            updateRobotPosition(newRobotPosition);
        }
    }

    @OnClick(R.id.btn_east)
    void onEastClick() {
        Position newRobotPosition = new Position(mMapItems.mRobot.getPosition().getXpos() + 1, mMapItems.mRobot.getPosition().getYpos());
        if (newRobotPosition.getXpos() >= SizeUtils.mMaxColumns) {
            Toast.makeText(this, "Invalid move", Toast.LENGTH_SHORT).show();
        } else {
            updateRobotPosition(newRobotPosition);
        }
    }

    @OnClick(R.id.btn_south)
    void onSouthClick() {
        Position newRobotPosition = new Position(mMapItems.mRobot.getPosition().getXpos(), mMapItems.mRobot.getPosition().getYpos() + 1);
        if (newRobotPosition.getYpos() >= SizeUtils.mMaxRows) {
            Toast.makeText(this, "Invalid move", Toast.LENGTH_SHORT).show();
        } else {
            updateRobotPosition(newRobotPosition);
        }
    }

    @OnClick(R.id.btn_west)
    void onWestClick() {
        Position newRobotPosition = new Position(mMapItems.mRobot.getPosition().getXpos() - 1, mMapItems.mRobot.getPosition().getYpos());
        if (newRobotPosition.getXpos() < 0) {
            Toast.makeText(this, "Invalid move", Toast.LENGTH_SHORT).show();
        } else {
            updateRobotPosition(newRobotPosition);
        }
    }

    @OnClick(R.id.btn_pick)
    void onPickClick() {
        ItemOnMap itemAtRobotPosition = getMapCellAtPosition(mMapItems.mRobot.getPosition()).getItemInThisCell();
        if (itemAtRobotPosition.getItemType() == ItemOnMap.ItemType.Crate) {
            if (itemAtRobotPosition.getQuantity() == 0) {
                Toast.makeText(this, "Crate is empty", Toast.LENGTH_SHORT).show();
            } else {
                if (mMapItems.mRobot.getQuantity() == 0) {
                    mMapItems.mRobot.setQuantity(1);
                    itemAtRobotPosition.setQuantity(itemAtRobotPosition.getQuantity() - 1);
                    updateStatusForRobotAndCrate(itemAtRobotPosition);
                } else {
                    Toast.makeText(this, "Robot already has one bag", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            onRobotPicksBagWhereNoCrate();
        }
    }

    @OnClick(R.id.btn_drop)
    void onDropClick() {
        ItemOnMap itemAtRobotPosition = getMapCellAtPosition(mMapItems.mRobot.getPosition()).getItemInThisCell();
        if (mMapItems.mConveyor.getPosition().equals(itemAtRobotPosition.getPosition())) {
            if (mMapItems.mRobot.getQuantity() == 1) {
                mMapItems.mRobot.setQuantity(0);
                mMapItems.mConveyor.setQuantity(mMapItems.mConveyor.getQuantity() + 1);
                updateStatusForRobotAndConveyor();
            } else {
                Toast.makeText(this, "Robot has no bags", Toast.LENGTH_SHORT).show();
            }
        } else {
            onRobotDropsBagWhereNoConveyor();
        }
    }


    /**
     * Called from Form Fragment when user finishes to input every item position
     *
     * @param mapItems contains every item visible in the scene(robot, conveyor and all crates)
     */
    @Override
    public void onStart(@NonNull MapItems mapItems) {
        mMapItems = mapItems;

        MapCell robotMapCell = getMapCellAtPosition(mMapItems.mRobot.getPosition());
        if (robotMapCell == null) {
            Toast.makeText(this, "Robot position is out of scene", Toast.LENGTH_SHORT).show();
            return;
        }
        robotMapCell.setItemInThisCell(mMapItems.mRobot);

        MapCell conveyorMapCell = getMapCellAtPosition(mMapItems.mConveyor.getPosition());
        if (conveyorMapCell == null) {
            Toast.makeText(this, "Conveyor position is out of scene", Toast.LENGTH_SHORT).show();
            return;
        }
        conveyorMapCell.setItemInThisCell(mMapItems.mConveyor);
        updateStatusForRobotAndConveyor();

        if (mMapItems.mCrates != null) {
            for (ItemOnMap crate : mMapItems.mCrates) {
                MapCell crateCell = getMapCellAtPosition(crate.getPosition());
                if (crateCell != null) {
                    crateCell.setItemInThisCell(crate);
                }
            }
        } else {
            Toast.makeText(this, "No crates", Toast.LENGTH_SHORT).show();
        }
    }
}
