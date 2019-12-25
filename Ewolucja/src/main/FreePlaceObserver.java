package main;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class FreePlaceObserver {
    protected LinkedList<Vector2d> freeJunglePlaces;
    protected LinkedList<Vector2d> freeSavannaPlaces;
    private Vector2d JungleLowerLeft;
    private Vector2d JungleUpperRight;
    public FreePlaceObserver(Vector2d mapUpperRight,Vector2d JungleLowerLeft,Vector2d JungleUpperRight){
        freeJunglePlaces=new LinkedList<>();
        freeSavannaPlaces=new LinkedList<>();
        for(int x=0; x<=mapUpperRight.x; x++){
            for(int y=0; y<=mapUpperRight.y;y++){
                Vector2d v= new Vector2d(x,y);
                if(v.follows(JungleLowerLeft)&& v.precedes(JungleUpperRight)){
                    this.freeJunglePlaces.add(v);
                }
                else{
                    this.freeSavannaPlaces.add(v);
                }
            }
        }
        this.JungleLowerLeft=JungleLowerLeft;
        this.JungleUpperRight=JungleUpperRight;
    }

   protected void free(Vector2d place){
        if(this.freeSavannaPlaces.contains(place) || this.freeJunglePlaces.contains(place))return;
        if(place.follows(JungleLowerLeft) && place.precedes(JungleUpperRight)){
           this.freeJunglePlaces.add(place);
       }
       else{
           this.freeSavannaPlaces.add(place);
       }
   }

   protected void taken(Vector2d place){
       if(place.follows(JungleLowerLeft)&& place.precedes(JungleUpperRight)){
           this.freeJunglePlaces.remove(place);
       }
       else{
           this.freeSavannaPlaces.remove(place);
       }
   }
   protected Vector2d getRandomJungle(){
        Random rand=new Random();
        if(this.freeJunglePlaces.size()==0)return null;
        return this.freeJunglePlaces.get(rand.nextInt(this.freeJunglePlaces.size()));
   }
   protected Vector2d getRandomSavanna(){
       Random rand=new Random();
       if(this.freeSavannaPlaces.size()==0)return null;
       return this.freeSavannaPlaces.get(rand.nextInt(this.freeSavannaPlaces.size()));
   }
}
