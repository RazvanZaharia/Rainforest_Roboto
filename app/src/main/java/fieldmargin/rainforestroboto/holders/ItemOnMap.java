package fieldmargin.rainforestroboto.holders;


public class ItemOnMap {
    private Position mPosition;
    private int mQuantity;
    private ItemType mItemType;

    public ItemOnMap(Position position, int quantity, ItemType itemType) {
        mPosition = position;
        mQuantity = quantity;
        mItemType = itemType;
    }

    public Position getPosition() {
        return mPosition;
    }

    public void setPosition(Position position) {
        mPosition = position;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public ItemType getItemType() {
        return mItemType;
    }

    public void setItemType(ItemType itemType) {
        mItemType = itemType;
    }

    public enum ItemType {
        Robot, Crate, ConveyorBeltFeeder
    }
}
