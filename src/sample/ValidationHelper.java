package sample;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ValidationHelper {

    private final HashMap<String, ValidatorBase> ruleValidatorLookup = new HashMap<>(
            Map.ofEntries(
                    new AbstractMap.SimpleImmutableEntry<>("required", new RequiredFieldValidator("Required field.")),
                    new AbstractMap.SimpleImmutableEntry<>("number", new NumberValidator("Number value."))
            )
    );

    private final HashMap< ? super Node , List<String>> fieldRulesLookup;

    public ValidationHelper(HashMap<? super Node, List<String>> fieldRulesLookup) {
        this.fieldRulesLookup = fieldRulesLookup;

        this.addValidators();
    }

    private void addValidators() {
        fieldRulesLookup.forEach((field, rulesList) -> Objects.requireNonNull(getFieldValidators(field)).addAll(
                rulesList.stream()
                        .map(ruleValidatorLookup::get)
                        .toArray(ValidatorBase[]::new)
        ));
    }

    private ObservableList<ValidatorBase> getFieldValidators(Object field) {
        try {
            Method getValidators = field.getClass().getDeclaredMethod("getValidators");

            return (ObservableList<ValidatorBase>) getValidators.invoke(field);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean validateField(Object field) {
        try {
            Method validate = field.getClass().getDeclaredMethod("validate");

            return (boolean) validate.invoke(field);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void clearField(Object field) {
        switch (field.getClass().getSimpleName()) {
            case "JFXTextField" -> ((JFXTextField) field).clear();
            case "JFXComboBox" -> ((JFXComboBox<?>) field).getSelectionModel().clearSelection();
            default -> throw new IllegalStateException("Unexpected value: " + field.getClass().getName());
        }

    }

    /**
     * Returns true is all fields are valid
     *
     * @return boolean
     */
    public boolean validate() {

        return this.fieldRulesLookup.keySet()
                .stream().map(this::validateField)
                .reduce(true, (acc, element) -> acc & element );
    }

    public void clearAllFields() {
        this.fieldRulesLookup.keySet().forEach(this::clearField);
    }
}
