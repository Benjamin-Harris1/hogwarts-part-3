package studentadmin.utils;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

// Smart måde at lave en generisk og dynamisk patcher, fundet her: https://medium.com/@devchopra999/handling-patch-requests-efficiently-in-springboot-3db06e4783e2

@Component
public class Patcher {
    public void patchObject(Object existingObject, Object patchObject) throws IllegalAccessException {
        Class<?> clazz = patchObject.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            // Gør private felter tilgængelige
            field.setAccessible(true); 
            Object value = field.get(patchObject);
            
            // Tjekker om værdien er null. Hvis ikke, opdateres det eksisterende objekt
            if (value != null) {
                // Find feltet i det eksisterende objekt med samme navn
                Field existingField = findField(existingObject.getClass(), field.getName());
                if (existingField != null) {
                    existingField.setAccessible(true);
                    existingField.set(existingObject, value);
                    existingField.setAccessible(false);

                }
            }
            // Gør felter private igen
            field.setAccessible(false); 
        }
    }

    private Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == null) {
                return null;
            } else {
                // Søg i superklasser, hvis feltet ikke findes i den aktuelle klasse
                return findField(superClass, fieldName);
            }
        }
    }
}
