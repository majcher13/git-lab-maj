package main;

public class Vector2d {
    public int x,y;
    public Vector2d(int x, int y){
        this.x=x;
        this.y=y;
    }
    public String toString(){
        String result="("+x+","+y+")";
        return result;
    }
    public boolean precedes(Vector2d other){
        return (this.x<=other.x && this.y<=other.y);
    }
    public boolean follows(Vector2d other){
        return (this.x>=other.x && this.y >= other.y);
    }
    public Vector2d upperRight(Vector2d other) {
        Vector2d result = new Vector2d(this.x > other.x ? this.x : other.x, this.y > other.y ? this.y : other.y);
        return result;
    }
    public Vector2d lowerLeft(Vector2d other){
        Vector2d result = new Vector2d(this.x < other.x ? this.x : other.x, this.y < other.y ? this.y : other.y);
        return result;
    }
    public void add(Vector2d other){
        this.x=this.x+other.x;
        this.y=this.y+other.y;
        //Vector2d result=new Vector2d(this.x+other.x,this.y+other.y);
        //return result;
    }

    public void add(Vector2d other,Vector2d boundary){
        this.add(other);
        this.x=this.x%boundary.x;
        this.y=this.y%boundary.y;
    }

    public Vector2d sum(Vector2d other){
        Vector2d result=new Vector2d(this.x+other.x,this.y+other.y);
        return result;
    }

    public Vector2d sum(Vector2d other,Vector2d boundary){
        Vector2d result=new Vector2d(this.x+other.x,this.y+other.y);
        result.y=result.y%boundary.y;
        result.x=result.x%boundary.x;
        return result;
    }

    public Vector2d substract(Vector2d other){
        Vector2d result=new Vector2d(this.x-other.x,this.y-other.y);
        return result;
    }
    public boolean equals(Object other){
        if (this==other) return true;
        if(!(other instanceof Vector2d))return false;
        Vector2d that= (Vector2d)other;
        return(that.x==this.x && that.y==this.y);
    }
    public Vector2d opposite(){
        Vector2d result=new Vector2d(-this.x,-this.y);
        return result;
    }

    @Override
    public int hashCode() {
        int hash=13;
        hash+=this.x*31;
        hash+=this.y*17;
        return hash;
    }
}

