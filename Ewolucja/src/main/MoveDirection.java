package main;

public enum MoveDirection {
    NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;

    public Vector2d toUnitVector() {
        switch (this) {
            case NORTH:
                return new Vector2d(0, 1);
            case SOUTH:
                return new Vector2d(0, -1);
            case WEST:
                return new Vector2d(-1, 0);
            case EAST:
                return new Vector2d(1, 0);
            case NORTHEAST:
                return new Vector2d(1,1);
            case NORTHWEST:
                return new Vector2d(-1,1);
            case SOUTHEAST:
                return new Vector2d(1,-1);
            case SOUTHWEST:
                return new Vector2d(-1,-1);
        }
        return new Vector2d(0, 0);
    }

    public MoveDirection next(){
        switch (this){
            case NORTH:
                return NORTHEAST;
            case SOUTH:
                return SOUTHWEST;
            case WEST:
                return NORTHWEST;
            case EAST:
                return SOUTHEAST;
            case NORTHEAST:
                return EAST;
            case NORTHWEST:
                return NORTH;
            case SOUTHEAST:
                return SOUTH;
            case SOUTHWEST:
                return WEST;
        }
        return NORTH;
    }
    public MoveDirection change(int spin){
        MoveDirection cur=this;
        for(;spin>0;spin--){
            cur=cur.next();
        }
        return cur;
    }
}