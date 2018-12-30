package io;

import model.Coordinate;
import model.Creature;
import model.Model;
import model.Property;
import model.cell.Cell;
import org.json.JSONObject;

public class RecordReader {

   public Property readProperty(String json) {
       if (json == null || json.isEmpty())
            return null;
       JSONObject propertyObject = new JSONObject(json);
       String name = propertyObject.getString("name");
       JSONObject cellObject = propertyObject.getJSONObject("cell");
       int x = cellObject.getInt("x");
       int y = cellObject.getInt("y");
       Coordinate coordinate = new Coordinate(x, y);
       Cell cell = new Cell(null, coordinate);
       for (Creature creature : Model.getCreatures()) {
           if (creature.getName().equals(name)) {
               cell.setCreature(creature);
               break;
           }
       }
       return new Property(name, cell);
   }
}
