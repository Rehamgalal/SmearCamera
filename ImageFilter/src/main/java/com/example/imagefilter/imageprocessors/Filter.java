package com.example.imagefilter.imageprocessors;


import android.graphics.Bitmap;
import android.util.Log;


import com.example.imagefilter.imageprocessors.subfilters.BrightnessSubFilter;
import com.example.imagefilter.imageprocessors.subfilters.ColorOverlaySubFilter;
import com.example.imagefilter.imageprocessors.subfilters.ContrastSubFilter;
import com.example.imagefilter.imageprocessors.subfilters.SaturationSubfilter;
import com.example.imagefilter.imageprocessors.subfilters.ToneCurveSubFilter;
import com.example.imagefilter.imageprocessors.subfilters.VignetteSubfilter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This Class represents a ImageFilter and includes many subfilters within, we add different subfilters to this class's
 * object and they are then processed in that particular order
 */
public class Filter {
    private List<SubFilter> subFilters = new ArrayList<>();
    private String name;

    public Filter(Filter filter) {
        this.subFilters = filter.subFilters;
    }

    public Filter() {
    }

    public Filter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds a Subfilter to the Main Filter
     *
     * @param subFilter Subfilter like contrast, brightness, tone Curve etc. subfilter
     * @see BrightnessSubFilter
     * @see ColorOverlaySubFilter
     * @see ContrastSubFilter
     * @see ToneCurveSubFilter
     * @see VignetteSubfilter
     * @see SaturationSubfilter
     */
    public void addSubFilter(SubFilter subFilter) {
        subFilters.add(subFilter);
    }

    /**
     * Clears all the subfilters from the Parent Filter
     */
    public void clearSubFilters() {
        subFilters.clear();
    }

    /**
     * Removes the subfilter containing Tag from the Parent Filter
     */
    public void removeSubFilterWithTag(String tag) {
        Iterator<SubFilter> iterator = subFilters.iterator();
        while (iterator.hasNext()) {
            SubFilter subFilter = iterator.next();
            if (subFilter.getTag().equals(tag)) {
                iterator.remove();
            }
        }
    }

    /**
     * Returns The filter containing Tag
     */
    public SubFilter getSubFilterByTag(String tag) {
        for (SubFilter subFilter : subFilters) {
            if (subFilter.getTag().equals(tag)) {
                return subFilter;
            }
        }
        return null;
    }

    /**
     * Give the output Bitmap by applying the defined filter
     *
     * @param inputImage Input Bitmap on which filter is to be applied
     * @return filtered Bitmap
     */
    public Bitmap processFilter(Bitmap inputImage) {
        Bitmap outputImage = inputImage;
        outputImage= outputImage.copy(Bitmap.Config.ARGB_8888, true);
        if (outputImage != null) {
            for (SubFilter subFilter : subFilters) {
                try {
                    outputImage = subFilter.process(outputImage);
                    Log.e("","first processFilter outputImage.getWidth()  "+outputImage.getWidth());
                    Log.e("","first processFilter outputImage.getHeight()  "+outputImage.getHeight());
                } catch (OutOfMemoryError oe) {
                    oe.printStackTrace();
                    System.gc();
                    try {
                        outputImage = subFilter.process(outputImage);
                        Log.e("","second processFilter outputImage.getWidth()  "+outputImage.getWidth());
                        Log.e("","second processFilter outputImage.getHeight()  "+outputImage.getHeight());
                    } catch (OutOfMemoryError ignored) {
                        ignored.printStackTrace();
                    }
                }
            }
        }

        return outputImage;
    }

}
