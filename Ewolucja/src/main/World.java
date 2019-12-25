package main;



import static java.lang.System.out;
import java.lang.String;
public class World {

        public static void main (String[]args){
            try {
                int width=2;
                int height=2;
                int startEnergy=5;
                int moveEnergy=1;
                int plantEnergy=3;
                double jungleRatio=1;
                LoopedMap map=new LoopedMap(width,height,startEnergy,moveEnergy,plantEnergy,jungleRatio);
                for(int i=0;i<3;i++)map.place(new Animal(map,startEnergy));
                /*for(int i=0;i<50;i++) {
                    map.plantsGrowing();
                }*/
                out.println(map.toString());
                for(int i=0;i<5;i++) {
                    map.run();
                    out.println(map.toString());
                }
                //Animal first=new Animal(map,startEnergy);
                //Animal second=new Animal(map,startEnergy);
                //map.place(first,new Vector2d(1,1));
                //map.place(second,new Vector2d(3,3));
                //second.oriented=MoveDirection.SOUTHWEST;
                //first.oriented=MoveDirection.NORTHEAST;
                /*for(int i=0;i<3;i++){
                    map.place(new Animal(map,startEnergy));
                }
                out.println(map.toString());
                map.run();
                out.println(map.toString());
                map.run();
                map.run();
                map.run();
                out.println(map.toString());
                map.run();
                out.println(map.toString());*/

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
};


