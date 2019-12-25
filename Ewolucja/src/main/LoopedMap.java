package main;

import java.util.*;

import static java.lang.System.out;

public class LoopedMap implements IWorldMap,IPositionChangeObserver {


    public Map<Vector2d,SortedSet<Animal>> animals;
    protected Map<Vector2d,Grass> field;
    private List<Animal> deadPool;
    private Vector2d upperRight;
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    private Vector2d jungleUpperRight;
    private Vector2d jungleLowerLeft;
    private int animalAmount;
    private FreePlaceObserver observer;
    private int maxId;


    public LoopedMap(int width,int height, int startEnergy, int moveEnergy, int plantEnergy,double jungleRatio){
        this.deadPool=new ArrayList<>();
        this.animals=new HashMap<>();
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                this.animals.put(new Vector2d(i,j),new TreeSet<Animal>());
            }
        }
        this.field=new HashMap<>();
        this.upperRight=new Vector2d(width-1,height-1);
        this.moveEnergy=moveEnergy;
        this.plantEnergy=plantEnergy;
        this.startEnergy=startEnergy;
        this.animalAmount=0;
        //this.place(new Animal(this,this.startEnergy));
        if (jungleRatio<0 || jungleRatio>1)
            throw new IllegalArgumentException("jungle ratio has to be between 0 and 1");
        int jungleWidth= (int) (jungleRatio*width);
        int jungleHeight= (int) (jungleRatio*height);
        this.jungleLowerLeft=new Vector2d((width-jungleWidth)/2,(height-jungleHeight)/2);
        this.jungleUpperRight= this.jungleLowerLeft.sum(new Vector2d(jungleWidth,jungleHeight));
        this.observer=new FreePlaceObserver(this.upperRight,this.jungleLowerLeft,this.jungleUpperRight);
        this.maxId=0;

    }

    @Override
    public boolean place(Animal animal) {
        animal.setPosition(this.observer.getRandomJungle());
        if(this.objectAt(animal.getPosition())instanceof Animal) return false;
        //this.animals.put(animal.getPosition(),new TreeSet<Animal>());
        this.animals.get(animal.getPosition()).add(animal);
        this.animalAmount++;
        this.observer.taken(animal.getPosition());
        animal.id=(++this.maxId);
        return true;
    }

    public boolean place(Animal animal,Vector2d place) {
        //animal.setPosition(place);
        if(this.objectAt(place)instanceof Animal) return false;
        this.animals.put(animal.getPosition(),new TreeSet<Animal>());
        this.animals.get(animal.getPosition()).add(animal);
        this.animalAmount++;
        this.observer.taken(animal.getPosition());
        return true;
    }

    @Override
    public void run(){
        out.println(this.animalAmount);
        funeral();
        //out.println("AfterFuneral:");
        //out.println(this.toString());
        moving();
        out.println("AfterMoving:");
        out.println(this.toString());
        eating();
        out.println("AfterEating:");
        out.println(this.toString());
        procreation();
        //out.println("AfterProcreation:");
        //out.println(this.toString());
        plantsGrowing();
        //out.println("AfterGrowing:");
        //out.println(this.toString());
    }



    void plantsGrowing() {
        int r=1;
        int junglePlants=0;
        int savannaPlants;
        Random rand=new Random();
        while (r==1){
            junglePlants++;
            r=rand.nextInt(1);
        }
        r=rand.nextInt(3);
        if(r==0)savannaPlants=1;
        else savannaPlants=0;
        Vector2d jungleSpan=jungleUpperRight.substract(jungleLowerLeft);
        while(junglePlants-->0){
            Vector2d placement=this.observer.getRandomJungle();
            if(placement==null)break;
            this.field.put(placement,new Grass(placement));
        }
        while(savannaPlants-->0){
            Vector2d placement=this.observer.getRandomSavanna();
            if(placement==null)break;
            this.field.put(placement,new Grass(placement));
        }

    }

    private void procreation() {
        //Collection<SortedSet<Animal>> everybody=  this.animals.values();
        //Iterator it=this.animals.entrySet().iterator();
        Set <Vector2d> keys=this.animals.keySet();
        for (Object o : keys.toArray()) {
            Vector2d key=(Vector2d)o;
            if(this.animals.get(key).size()>=2){
                //out.println("yyyyyyy");
                SortedSet<Animal>set=this.animals.get(key);
                //this.animals.get(key).remove(key);
                Animal dad= (Animal) set.last();
                set.remove(set.last());
                out.println("animalTaken:procreation");
                out.println(this.toString());
                Animal mum=(Animal)set.last();
                set.remove(set.last());
                out.println("animalTaken:procreation");
                out.println(this.toString());
                if(dad.energy>=startEnergy/2 && mum.energy>=startEnergy/2) {
                    this.animalAmount++;
                    //out.println("yyyyyyy");
                    List<Vector2d>available=new ArrayList<>();
                    MoveDirection dir=MoveDirection.NORTH;
                    do{
                        if(!isOccupied(dad.getPosition().sum(dir.toUnitVector(),this.upperRight))){
                            available.add(dad.getPosition().sum(dir.toUnitVector(),this.upperRight));
                        }
                        dir=dir.next();
                    }while(dir!=MoveDirection.NORTH);
                    if(available.isEmpty()){
                        do{
                            available.add(dad.getPosition().sum(dir.toUnitVector(),this.upperRight));
                            dir=dir.next();
                        }while(dir!=MoveDirection.NORTH);
                    }
                    new Animal(dad, mum,available,this.upperRight);
                }
                set.add(dad);
                out.println("newAnimal:procreation");
                out.println(this.toString());
                set.add(mum);
                out.println("newAnimal:procreation");
                out.println(this.toString());
                //this.animals.put(dad.getPosition(),set);
            }
        }
    }

    private void moving(){
        Collection<SortedSet<Animal>> a=this.animals.values();
        ArrayList<Animal> stworki=new ArrayList<>();
        for(SortedSet set : a){
            stworki.addAll(set);
        }
        for(Animal stworek:stworki){
            stworek.setOrientation();
            stworek.move(this.moveEnergy,this.upperRight);
            if(stworek.energy<=0){
                this.deadPool.add(stworek);
            }
        }
    }

    private void eating(){
        Collection<SortedSet<Animal>> a=this.animals.values();
        List<Animal>eaters=new LinkedList<>();
        for(SortedSet<Animal>set : a){
            if(set.size()>0&&this.field.containsKey(set.first().getPosition())) {
                Animal[] tmp = new Animal[set.size()];
                set.toArray(tmp);
                int maxEnergy = tmp[tmp.length - 1].energy;
                int i = tmp.length-1;
                while (i>=0 && tmp[i].energy == maxEnergy) {
                    eaters.add(tmp[i]);
                    i--;
                }
                for (Animal zwierzak : eaters) {
                    zwierzak.eat(this.plantEnergy / eaters.size());
                    if(zwierzak.energy>0)this.deadPool.remove(zwierzak);
                }
                this.field.remove(set.first().getPosition());
            }
        }
    }

    private void funeral(){
        for(Animal deadOne : this.deadPool){
            Vector2d position=deadOne.getPosition();
            SortedSet a=this.animals.get(position);
            this.animals.get(position).remove(deadOne);
            out.println("animalTaken:funeral");
            if((this.animals.get(position).size()==0)&&(!this.field.containsKey(position))){
                this.observer.free(position);
            }
            this.animalAmount--;
        }
        this.deadPool.clear();
    }

    @Override
    public Object objectAt(Vector2d position) {
        if(this.animals.get(position).size()>0)return animals.get(position).last();
        if(this.field.containsKey(position))return field.get(position);
        return null;
    }

    public void positionIsChanging(Vector2d position, Animal animal){
        this.animals.get(position).remove(animal);
        out.println("animalTaken:positionIsChanging");
        if(this.animals.get(position).size()==0 && !this.field.containsKey(position)){
            this.observer.free(position);
        }
        out.println(this.toString());
    }
    @Override
    public void positionChanged(Animal animal) {
        this.animals.get(animal.getPosition()).add(animal);
        out.println("newAnimal:positionChanged");
        out.println(this.toString());
        }

    @Override
    public void newAnimal(Animal animal){
        this.animals.get(animal.getPosition()).add(animal);
        this.observer.taken(animal.getPosition());
        out.println("newAnimal:newAnimal");
        animal.id=(++this.maxId);
    }

    @Override
    public void energyChanged(Animal animal) {
        this.animals.get(animal.getPosition()).remove(animal);
        out.println("animalTaken:energyChanged");
        out.println(this.toString());
        this.animals.get(animal.getPosition()).add(animal);
        out.println("newAnimal:energyChanged");
        out.println(this.toString());
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return ((this.animals.get(position).size()>0)||(this.field.containsKey(position)));
    }



    public String toString(){
        Vector2d v=this.upperRight;
        Vector2d w=new Vector2d(0,0);
        MapVisualizer a=new MapVisualizer(this);
        return a.draw(w,v);
    }
}