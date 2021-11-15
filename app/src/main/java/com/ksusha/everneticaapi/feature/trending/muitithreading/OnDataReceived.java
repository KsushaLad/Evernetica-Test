package com.ksusha.everneticaapi.feature.trending.muitithreading;

import com.ksusha.everneticaapi.feature.model.ImageModel;

import java.util.List;

public interface OnDataReceived {
    void onReceived(List<ImageModel> list);
}