package fieldmargin.rainforestroboto.holders;


public class Position {

    private int mXpos;
    private int mYpos;

    public Position(int xpos, int ypos) {
        mXpos = xpos;
        mYpos = ypos;
    }

    public int getXpos() {
        return mXpos;
    }

    public void setXpos(int xpos) {
        mXpos = xpos;
    }

    public int getYpos() {
        return mYpos;
    }

    public void setYpos(int ypos) {
        mYpos = ypos;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Position
                && mXpos == ((Position) obj).mXpos
                && mYpos == ((Position) obj).mYpos;
    }
}
