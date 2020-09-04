package sample.models;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.lang.reflect.Field;

import static java.util.Objects.isNull;

public class Manufacturer implements Serializable {

    private SimpleStringProperty companyName;

    private SimpleStringProperty streetAddress;

    private SimpleStringProperty zipCode;

    private SimpleStringProperty country;

    public Manufacturer() {
        this.companyName = new SimpleStringProperty();
        this.streetAddress = new SimpleStringProperty();
        this.zipCode = new SimpleStringProperty();
        this.country = new SimpleStringProperty();
    }

    public Manufacturer(String companyName, String streetAddress, String zipCode, String country) {
        this.companyName = new SimpleStringProperty(companyName);
        this.streetAddress = new SimpleStringProperty(streetAddress);
        this.zipCode = new SimpleStringProperty(zipCode);
        this.country = new SimpleStringProperty(country);
    }

    public SimpleStringProperty companyNameProperty() {
        return companyName;
    }

    public SimpleStringProperty streetAddressProperty() {
        return streetAddress;
    }

    public SimpleStringProperty zipCodeProperty() {
        return zipCode;
    }

    public SimpleStringProperty countryProperty() {
        return country;
    }

    public String getCompanyName() {
        return companyName.get();
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public String getStreetAddress() {
        return streetAddress.get();
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress.set(streetAddress);
    }

    public String getZipCode() {
        return zipCode.get();
    }

    public void setZipCode(String zipCode) {
        this.zipCode.set(zipCode);
    }

    public String getCountry() {
        return country.get();
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public void resetValues() {
        for(Field field : Manufacturer.class.getDeclaredFields()) {
            field.setAccessible(true);

            try {
                ((SimpleStringProperty) field.get(this)).setValue(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        String fieldsString = "";

        for (Field field : Manufacturer.class.getDeclaredFields()) {

            field.setAccessible(true);
            try {
                Object value = ((SimpleStringProperty) field.get(this)).get();
                if (!isNull(value)) {
                    fieldsString += String.format("%s='%s' ", field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return String.format("Manufacturer { %s }", fieldsString);
    }
}
