package se.doktorkapten.levelparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class LevelData {
    private int width;
    private int height;
    private LinkedHashMap<String, ArrayList<Vertex>> positions;
    private LinkedHashMap<String, ArrayList<Polygon>> paths;

    public LevelData() {
        positions = new LinkedHashMap<String, ArrayList<Vertex>>();
        paths = new LinkedHashMap<String, ArrayList<Polygon>>();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public HashMap<String, ArrayList<Vertex>> getPositions() {
        return positions;
    }

    public HashMap<String, ArrayList<Polygon>> getPaths() {
        return paths;
    }

    public void addPaths(String name, ArrayList<Polygon> pathList) {
        paths.put(name, pathList);
    }

    public void addPositions(String name, ArrayList<Vertex> positionList) {
        positions.put(name, positionList);
    }

}
