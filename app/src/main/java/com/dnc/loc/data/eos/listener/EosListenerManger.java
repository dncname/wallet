package com.dnc.loc.data.eos.listener;

import java.util.ArrayList;
import java.util.List;

public class EosListenerManger implements EosAccountRefListener {
    public List<EosAccountRefListener> listeners;

    public void addEosAccountRefListeners(EosAccountRefListener listener) {
        if (listeners == null) listeners = new ArrayList<>();
        if (listeners.contains(listener)) {
            return;
        }
        listeners.add(listener);
    }


    public void removeEosAccountRefListeners(EosAccountRefListener listener) {
        if (listeners != null && listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    @Override
    public void blockFlushed() {
        if (listeners == null || listeners.size() == 0) return;
        for (EosAccountRefListener listener : listeners)
            if (listener != null)
                listener.blockFlushed();
    }

    @Override
    public void balanceFlushed() {
        if (listeners == null || listeners.size() == 0) return;
        for (EosAccountRefListener listener : listeners)
            if (listener != null)
                listener.balanceFlushed();
    }

    public void onDestroy() {
        listeners = null;
    }
}

