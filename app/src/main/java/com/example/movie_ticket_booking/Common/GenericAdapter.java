package com.example.movie_ticket_booking.Common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class GenericAdapter<T> extends BaseAdapter {

    protected List<T> list;
    private int resLayout;

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public T getItem(int i) {
        return this.list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return inflater.inflate(this.resLayout, viewGroup, false);
    }

    public void setList(List<T> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void add(T o) {
        this.list.add(o);
        this.notifyDataSetChanged();
    }

    public void addAll(Collection<T> list) {
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.list.clear();
        this.notifyDataSetChanged();
    }
}
