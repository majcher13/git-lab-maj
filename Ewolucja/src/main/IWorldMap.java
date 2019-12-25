package main;

public interface IWorldMap {
    boolean place(Animal animal);
    void run();
    boolean isOccupied(Vector2d position);
    Object objectAt(Vector2d position);
}
