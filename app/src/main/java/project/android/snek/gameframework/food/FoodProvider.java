package project.android.snek.gameframework.food;

import java.util.List;

import project.android.snek.utils.Vector;

public interface FoodProvider {

    List<Food> foodWithinRadius(Vector center, double radius);

}

