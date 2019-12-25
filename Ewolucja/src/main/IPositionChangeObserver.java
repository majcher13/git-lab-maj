package main;

public interface IPositionChangeObserver {
    void positionChanged(Animal animal);
    void newAnimal(Animal animal);
    void energyChanged(Animal animal);
    void positionIsChanging(Vector2d old,Animal animal);
}
