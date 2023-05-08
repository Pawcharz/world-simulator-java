import World.World;

public class Main {
    public static void main(String[] args) {

        World world = World.GetInstance();

        world.Simulate();
    }
}