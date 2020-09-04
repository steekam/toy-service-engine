package sample.models;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.lang.reflect.Field;

import static java.util.Objects.isNull;

public class Toy implements Serializable {

    private final SimpleStringProperty code = new SimpleStringProperty();

    private final SimpleStringProperty name = new SimpleStringProperty();

    private final SimpleStringProperty description = new SimpleStringProperty();

    private final SimpleStringProperty price = new SimpleStringProperty();

    private final SimpleStringProperty dateOfManufacture = new SimpleStringProperty();

    private final SimpleStringProperty batchNumber = new SimpleStringProperty();

    private Manufacturer manufacturer = new Manufacturer();

    public Toy() {
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public SimpleStringProperty dateOfManufactureProperty() {
        return dateOfManufacture;
    }

    public SimpleStringProperty batchNumberProperty() {
        return batchNumber;
    }

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getPrice() {
        return price.get();
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getDateOfManufacture() {
        return dateOfManufacture.get();
    }

    public void setDateOfManufacture(String dateOfManufacture) {
        this.dateOfManufacture.set(dateOfManufacture);
    }

    public String getBatchNumber() {
        return batchNumber.get();
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber.set(batchNumber);
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void resetValues() {
        for(Field field : Toy.class.getDeclaredFields()) {
            field.setAccessible(true);

            try {
                if(field.getName().equals("manufacturer")) {
                    ((Manufacturer) field.get(this)).resetValues();
                    continue;
                }

                ((SimpleStringProperty) field.get(this)).setValue(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        String fieldsString = "";

        for (Field field : Toy.class.getDeclaredFields()) {

            field.setAccessible(true);
            try {
                Object value = null;

                if (field.get(this) instanceof SimpleStringProperty)
                    value = ((SimpleStringProperty) field.get(this)).get();


                if (field.get(this) instanceof Manufacturer) value = field.get(this).toString();

                if (!isNull(value)) {
                    fieldsString += String.format("%s='%s' ", field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return String.format("Toy { %s }", fieldsString);
    }
}
