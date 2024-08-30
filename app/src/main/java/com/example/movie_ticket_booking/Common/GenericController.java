package com.example.movie_ticket_booking.Common;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Models.FilterType;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public abstract class GenericController<T extends Identifiable> {
    protected final FirebaseFirestore db;
    protected String collectionPath;
    protected final Class<T> type;

    public GenericController(String collection, Class<T> type) {
        this.db = FirebaseFirestore.getInstance();
        this.collectionPath = collection;
        this.type = type;
    }

    public void add(T o) {
        this.db.collection(this.collectionPath)
                .add(o)
                .addOnSuccessListener(documentReference -> o.setId(documentReference.getId()))
                .addOnFailureListener(e -> Log.e(collectionPath, "Error adding document", e));
    }

    public LiveData<T> get(String id) {
        MutableLiveData<T> liveData = new MutableLiveData<>();
        getRef(id)
                .get()
                .addOnSuccessListener(document -> {
                    T d = document.toObject(this.type);
                    d.setId(document.getId());
                    liveData.setValue(d);
                })
                .addOnFailureListener(e -> Log.d("qq", "error"));
        return liveData;
    }

    public LiveData<T> get(DocumentReference docRef) {
        MutableLiveData<T> liveData = new MutableLiveData<>();
        docRef
                .get()
                .addOnSuccessListener(document -> {
                    T d = document.toObject(this.type);
                    d.setId(document.getId());
                    liveData.setValue(d);
                })
                .addOnFailureListener(e -> Log.d("qq", "error"));
        return liveData;
    }

    public LiveData<List<T>> get(List<String> ids) {
        MutableLiveData<List<T>> liveData = new MutableLiveData<>();

        this.db.collection(this.collectionPath)
                .whereIn(FieldPath.documentId(), ids)
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

    public DocumentReference getRef(String id) {
        return this.db.collection(this.collectionPath).document(id);
    }

    public LiveData<List<T>> getAll() {
        return this.getAll(this.db.collection(this.collectionPath));
    }

    public LiveData<List<T>> getAll(CollectionReference colRef) {
        MutableLiveData<List<T>> liveData = new MutableLiveData<>();

        colRef
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
            if (list_criteria == null) return;

            list_criteria.forEach((k, v) -> {
                if (v == null) return;

                switch (_type) {
                    case EQUAL:
                        query[0] = query[0].whereEqualTo(k, v);
                        break;

                    case GREATER:
                        query[0] = query[0].whereGreaterThan(k, v);
                        break;

                    case GREATER_OR_EQUAL:
                        query[0] = query[0].whereGreaterThanOrEqualTo(k, v);
                        break;

                    case LESS:
                        query[0] = query[0].whereLessThan(k, v);
                        break;

                    case LESS_OR_EQUAL:
                        query[0] = query[0].whereLessThanOrEqualTo(k, v);
                        break;

                    case ARRAY_CONTAINS:
                        query[0] = query[0].whereArrayContains(k, v);
                        break;

                    case IN:
                        query[0] = query[0].whereIn(k, (List<? extends Object>) v);
                        break;

                    case STRING_CONTAINS:
                        break;
                }
            });
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
                                    String[] field_keys = f.split("\\.");

                                    Field currentField = m.getClass().getDeclaredField(field_keys[0]);
                                    currentField.setAccessible(true);
                                    Object currentObject = currentField.get(m);

                                    for (int i = 1; i < field_keys.length; i++) {
                                        if (currentObject == null) break;

                                        currentField = currentObject.getClass().getDeclaredField(field_keys[i]);
                                        currentField.setAccessible(true);
                                        currentObject = currentField.get(currentObject);
                                    }

                                    if (currentObject != null && currentObject instanceof String) {
                                        String objString = (String) currentObject;
                                        String searchStr = v.toString().toLowerCase();
                                        if (objString.toLowerCase().contains(searchStr))
                                            l.add(m);
                                    }
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
