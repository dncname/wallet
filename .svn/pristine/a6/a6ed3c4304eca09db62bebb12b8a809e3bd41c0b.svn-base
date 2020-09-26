package com.zlw.base.ui.listentry;

import androidx.databinding.ViewDataBinding;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public final class ListEntryViewHolder<Entry extends EntryViewHolder> {
    public final Class<Entry> clazz;
    public final int resId;
    public final int type;

    public ListEntryViewHolder(Class<Entry> clazz, int resId) {
        this.clazz = clazz;
        this.resId = resId;
        this.type = 0;
    }
    public ListEntryViewHolder(Class<Entry> clazz, int resId, int type) {
        this.clazz = clazz;
        this.resId = resId;
        this.type = type;
    }
    public Entry newEntry(ViewDataBinding binding) {
        try {
            Constructor constructor = clazz.getConstructor(ViewDataBinding.class);
            return (Entry) constructor.newInstance(binding);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}