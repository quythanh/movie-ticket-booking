package com.example.movie_ticket_booking.Controllers;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Identifiable;
import com.example.movie_ticket_booking.Models.FilterType;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GenericController<T extends Identifiable> {
    protected final FirebaseFirestore db;
    protected final String collectionPath;
    protected final Class<T> type;

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

    public LiveData<T> getLiveData(String id) {
        MutableLiveData<T> liveData = new MutableLiveData<>();
        TryGet(id).get().addOnSuccessListener(document -> liveData.setValue(document.toObject(this.type)));
        return liveData;
    }

    public DocumentReference TryGet(String id){
        return this.db.collection(this.collectionPath).document(id);
    }

    public LiveData<List<T>> getAll() {
        MutableLiveData<List<T>> liveData = new MutableLiveData<>();

        this.db.collection(this.collectionPath)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<T> l = new ArrayList<>();
                    queryDocumentSnapshots.forEach(doc -> {
                        T m = doc.toObject(this.type);
                        m.setId(doc.getId());
                        l.add(m);
                    });
                    liveData.setValue(l);
                })
                .addOnFailureListener(e -> {
                    Log.e(collectionPath, "Error fetching document", e);
                    liveData.setValue(Collections.emptyList());
                });

        return liveData;
    }

    public LiveData<List<T>> filter(Map<FilterType, Map<String, Object>> filters) {
        MutableLiveData<List<T>> liveData = new MutableLiveData<>();
        final Query[] query = { this.db.collection(this.collectionPath) };

        filters.forEach((_type, list_criteria) -> {
            if (list_criteria == null)
                return;

            switch (_type) {
                case EQUAL:
                    list_criteria.forEach((k, v) -> {
                        if (v != null)
                            query[0] = query[0].whereEqualTo(k, v);
                    });
                    break;

                case GREATER:
                    list_criteria.forEach((k, v) -> {
                        if (v != null)
                            query[0] = query[0].whereGreaterThan(k, v);
                    });
                    break;

                case GREATER_OR_EQUAL:
                    list_criteria.forEach((k, v) -> {
                        if (v != null)
                            query[0] = query[0].whereGreaterThanOrEqualTo(k, v);
                    });
                    break;

                case LESS:
                    list_criteria.forEach((k, v) -> {
                        if (v != null)
                            query[0] = query[0].whereLessThan(k, v);
                    });
                    break;

                case LESS_OR_EQUAL:
                    list_criteria.forEach((k, v) -> {
                        if (v != null)
                            query[0] = query[0].whereLessThanOrEqualTo(k, v);
                    });
                    break;

                case STRING_CONTAINS:
                    break;
            }
        });

        query[0].get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<T> l = new ArrayList<>();
                    queryDocumentSnapshots.forEach(doc -> {
                        T m = doc.toObject(this.type);
                        m.setId(doc.getId());

                        final int[] c = {0};

                        Map<String, Object> list_criteria = filters.get(FilterType.STRING_CONTAINS);
                        if (list_criteria == null)
                            l.add(m);
                        else {
                            list_criteria.forEach((f, v) -> {
                                if (v == null) {
                                    c[0]++;
                                    return;
                                }

                                try {
                                    Field objField = m.getClass().getDeclaredField(f);
                                    objField.setAccessible(true);

                                    String objString = (String) objField.get(m);
                                    assert objString != null;

                                    String searchStr = v.toString().toLowerCase();
                                    if (objString.contains(searchStr))
                                        l.add(m);
                                } catch (Exception e) {
                                    Log.e("Generic", e.toString());
                                }
                            });
                        }

                        if (c[0] == list_criteria.keySet().size())
                            l.add(m);
                    });
                    liveData.setValue(l);
                })
                .addOnFailureListener(e -> {
                    Log.e(collectionPath, "Error fetching document", e);
                    liveData.setValue(Collections.emptyList());
                });

        return liveData;
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

        // PAY ATTENTION!!!!!!
        // DO NOT TRY TO MERGE THIS TO THE ABOVE CODE
        // CAUSE IT WILL FAIL WITHOUT NO FCKING REASON
        Class<?> superCls = cls.getSuperclass();
        if (superCls != null) {
            Field[] baseFields = superCls.getDeclaredFields();
            for (Field field : baseFields) {
                field.setAccessible(true);
                String propertyName = field.getName();
                Object value = field.get(o);
                if (value != null && !propertyName.equals("id")) {
                    data.put(propertyName, value);
                }
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
