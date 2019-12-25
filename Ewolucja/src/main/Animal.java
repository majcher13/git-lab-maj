package main;
import javafx.collections.transformation.SortedList;
import main.*;
import main.Vector2d;
import java.util.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.System.out;
public class Animal implements IMapElement, Comparable {
    protected int energy;
    public int id;
    public MoveDirection oriented; //changeIt!!!
    private Vector2d position;
    private ArrayList genome;
    private main.IWorldMap myMap;
    private ArrayList<IPositionChangeObserver>Observers;
    public Animal(IWorldMap map,int beginEnergy){
        this.myMap=map;
        this.Observers=new ArrayList<IPositionChangeObserver>();
        this.Observers.add((IPositionChangeObserver)map);
        this.genome=new ArrayList<Integer>();
        for(int i=0; i<8;i++){
            this.genome.add(i);
        }
        Random rand=new Random();
        for (int i=8;i<32;i++){
            this.genome.add(rand.nextInt(8));
        }
        Collections.sort(this.genome);
        this.oriented=MoveDirection.NORTH;
        this.setOrientation();
        this.energy=beginEnergy;
    }
    public void setPosition(Vector2d p){
        this.position=p;
    }

    public Animal(Animal dad,Animal mum,List<Vector2d>available,Vector2d boundary){
            this.energy=(dad.energy/4) + (mum.energy/4);
            dad.energy-=dad.energy/4;
            mum.energy-=mum.energy/4;
            Random rand=new Random();
            int direction=rand.nextInt(available.size()-1);
            this.position=available.get(direction);
            this.fixPosition(boundary);
            this.myMap=dad.myMap;
            this.Observers=new ArrayList<IPositionChangeObserver>();
            this.genome=new ArrayList<Integer>();
            this.addObserver((IPositionChangeObserver) this.myMap);
            int a=rand.nextInt(31);
            int b=a;
            while(b==a) b=rand.nextInt(31);
            int firstCut=Math.min(a,b);
            int secondCut=Math.max(a,b);
            this.genome.addAll(dad.genome.subList(0,firstCut));
            this.genome.addAll(mum.genome.subList(firstCut,secondCut));
            this.genome.addAll(dad.genome.subList(secondCut,31));
            for(int i=0; i<7;i++){
                if(!genome.contains(i)){
                    int place=rand.nextInt(31);
                    genome.add(place,i);
                }
            }
            Collections.sort(this.genome);
            this.oriented=MoveDirection.NORTH;
            this.setOrientation();
            this.imBorn();
    }

    public Animal(){oriented=MoveDirection.NORTH;
        position=new Vector2d(2,2);
        this.Observers=new ArrayList<IPositionChangeObserver>();
    }

    public Animal(IWorldMap map, Vector2d initialPosition){
        this.position=initialPosition;
        this.Observers=new ArrayList<IPositionChangeObserver>();
        this.myMap=map;
        this.oriented=MoveDirection.NORTH;
    };
    public String toString(){
        /*switch (this.oriented){
            case WEST:
                return "6";
            case NORTHEAST:
                return "1";
            case NORTHWEST:
                return "7";
            case SOUTHEAST:
                return "3";
            case SOUTHWEST:
                return "5";
            case NORTH:
                return "0";
            case EAST:
                return "2";
            case SOUTH:
                return "4";
        }
        return "ERROR";*/
        return  Integer.toString(((LoopedMap) this.myMap).animals.get(this.getPosition()).size());
    }
    public MoveDirection getOrientation(){
        return this.oriented;
    }
    public Vector2d getPosition(){
        return this.position;
    }

    public void setOrientation(){
        Random rand=new Random();
        int index=rand.nextInt(31);
        this.oriented=this.oriented.change((int)genome.get(index));
    }
    public void eat(int plantEnergy){
        this.energy+=plantEnergy;
        this.energyChanged();
    }

    public void move(int moveEnergy,Vector2d boundary){
        Vector2d old= new Vector2d(this.getPosition().x,this.getPosition().y);
        this.positionIsChanging(old);
        switch(this.oriented){
            case NORTH:
                this.position.add(MoveDirection.NORTH.toUnitVector());
                break;
            case SOUTH:
                this.position.add(MoveDirection.SOUTH.toUnitVector());
                break;
            case EAST:
                this.position.add(MoveDirection.EAST.toUnitVector());
                break;
            case WEST:
                this.position.add(MoveDirection.WEST.toUnitVector());
                break;
            case SOUTHWEST:
                this.position.add(MoveDirection.SOUTHWEST.toUnitVector());
                break;
            case SOUTHEAST:
                this.position.add(MoveDirection.SOUTHEAST.toUnitVector());
                break;
            case NORTHWEST:
                this.position.add(MoveDirection.NORTHWEST.toUnitVector());
                break;
            case NORTHEAST:
                this.position.add(MoveDirection.NORTHEAST.toUnitVector());
                break;
        }
        this.fixPosition(boundary);
        this.positionChanged();
        this.energy-=moveEnergy;
        //this.energyChanged();
    }

    private void addObserver(main.IPositionChangeObserver observer){
        this.Observers.add(observer);
    }

    void removeObserver(main.IPositionChangeObserver observer){
        this.Observers.remove(observer);
    }

    private void positionChanged(){
        for (main.IPositionChangeObserver observer : this.Observers){
            observer.positionChanged(this);
        }
    }

    private void imBorn(){
        for (main.IPositionChangeObserver observer : this.Observers){
            observer.newAnimal(this);
        }
    }

    private void positionIsChanging(Vector2d old){
        for (main.IPositionChangeObserver observer : this.Observers){
            observer.positionIsChanging(old,this);
        }
    }

    private void energyChanged(){
        for (main.IPositionChangeObserver observer : this.Observers) {
            observer.energyChanged(this);
        }
    }

    private void fixPosition(Vector2d boundary){
        this.position.x=Math.abs(this.position.x%(boundary.x+1));
        this.position.y=Math.abs(this.position.y%(boundary.y+1));
    }


    @Override
    public int compareTo(Object o) {
        Integer a= 1;
        if(this.id==((Animal)o).id)return a.compareTo(1);
        if(this.energy==((Animal)o).energy){
            return Integer.compare(0,1);
        };
        return Integer.compare(this.energy, ((Animal) o).energy);
    }
}

