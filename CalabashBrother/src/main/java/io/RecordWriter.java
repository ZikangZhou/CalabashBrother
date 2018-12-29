package io;

import model.Property;
import org.json.JSONObject;
import java.io.*;

public class RecordWriter {

    public void writeProperty(Property property) {
        JSONObject propertyObject = new JSONObject();
        JSONObject cellObject = new JSONObject();
        propertyObject.put("name", property.getName());
        cellObject.put("x", property.getCell().getCoordinate().getCoordinateX());
        cellObject.put("y", property.getCell().getCoordinate().getCoordinateY());
        propertyObject.put("cell", cellObject);
        try {
            PrintWriter writer =
                    new PrintWriter(new BufferedWriter(new FileWriter(new File("record.json"), true)));
            writer.write(propertyObject.toString());
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
       try {
           PrintWriter writer =
                   new PrintWriter(new BufferedWriter(new FileWriter(new File("record.json"))));
           writer.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }
}
