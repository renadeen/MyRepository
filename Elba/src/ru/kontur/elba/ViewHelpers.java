package ru.kontur.elba;

import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ViewHelpers {
    @SuppressWarnings({"unchecked"})
    public static <T> T[] enumerateButtons(ViewGroup container, Class<T> viewType) {
        int childrenCount = container.getChildCount();
        ArrayList<T> result = new ArrayList<T>();
        for (int i = 0; i < childrenCount; i++) {
            View child = container.getChildAt(i);
            if (viewType.isInstance(child))
                result.add((T) child);
        }
        return result.toArray((T[]) Array.newInstance(viewType, result.size()));
    }
}
