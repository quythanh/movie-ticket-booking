package com.example.movie_ticket_booking.Controllers;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GenericController<T> {
    protected final FirebaseFirestore db;
    protected final String collectionPath;
    private final Class<T> type;

    public GenericController(String collection, Class<T> type) {
        this.db = FirebaseFirestore.getInstance();
        this.collectionPath = collection;
        this.type = type;
    }

    public void add(T o) {
        this.db.collection(this.collectionPath)
                .add(o)
                .addOnSuccessListener(documentReference -> Log.d(collectionPath, "Add successfully ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.e(collectionPath, "Error adding document", e));
    }

    public T get(String id) {
        final List<T> o = new ArrayList<>();

        DocumentReference docRef = this.db.collection(this.collectionPath).document(id);
        docRef.get().addOnSuccessListener(document -> o.add(document.toObject(this.type)));

        return o.get(0);
    }

    public List<T> getAll() {
        final List<T> l = new ArrayList<>();

        this.db.collection(this.collectionPath)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> queryDocumentSnapshots.forEach(doc -> l.add(doc.toObject(this.type))));

        return l;
    }

    private static Map<String, Object> _createUpdateData(Object o) throws IllegalAccessException {
        Map<String, Object> data = new HashMap<>();

        Class<?> cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String propertyName = field.getName();
            Object value = field.get(o);
            if (value != null && !propertyName.equals("id")) {
                data.put(propertyName, value);
            }
        }

        return data;
    }

    public void update(String id, T o) throws IllegalAccessException {
        if (o == null || id.isEmpty()) {
            Log.e(this.collectionPath, "ID is not valid!");
            throw new IllegalArgumentException("ID is not valid!");
        }

        Map<String, Object> data = _createUpdateData(o);

        DocumentReference docRef = this.db.collection(this.collectionPath).document(id);
        docRef.update(data)
                .addOnSuccessListener(aVoid -> Log.d(this.collectionPath, "Update successfully!"))
                .addOnFailureListener(e -> Log.w(this.collectionPath, "Error updating document!", e));
    }

    public void delete(String id) {
        this.db.collection(this.collectionPath)
                .document(id)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(this.collectionPath, "Delete successfully!"))
                .addOnFailureListener(e -> Log.e(this.collectionPath, "Error deleting document!", e));
    }
}
