package at.fhjoanneum.airkoality.db.room;


import androidx.room.TypeConverter;

import java.util.ArrayList;

import at.fhjoanneum.airkoality.model.Measurement;

public class Converters {

    @TypeConverter
    public static ArrayList<Measurement> fromStringToMeasurements(String value) {
        ArrayList<Measurement> measurementsList = new ArrayList<>();
        String[] measurements = value.split("\\|");

        for (String s : measurements) {
            String[] parts = s.split(";");
            measurementsList.add(new Measurement(parts[0], Double.valueOf(parts[1]), parts[2]));
        }

        return measurementsList;
    }

    @TypeConverter
    public static String fromMeasurementsToString(ArrayList<Measurement> measurements) {

        StringBuilder builder = new StringBuilder();
        if (measurements != null) {
            for (int i = 0; i < measurements.size(); i++) {
                builder.append(measurements.get(i).getParameter())
                        .append(";")
                        .append(measurements.get(i).getValue())
                        .append(";")
                        .append(measurements.get(i).getUnit());

                if (i != measurements.size() - 1) {
                    builder.append("|");
                }
            }
        }
        return builder.toString();
    }
}
