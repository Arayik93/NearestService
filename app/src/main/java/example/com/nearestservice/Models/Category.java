package example.com.nearestservice.Models;

/**
 * Created by Narehh on 8/14/2016.
 */
public class Category {

    private String name;
    private String imagePath;

    public Category() {
    }

    public Category(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }
}
