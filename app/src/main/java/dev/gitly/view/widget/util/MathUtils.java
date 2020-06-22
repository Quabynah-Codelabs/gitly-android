package dev.gitly.view.widget.util;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;

/**
 * Borrowed from github.com/romannurik/muzei
 */
public class MathUtils {

    private MathUtils() {
    }

    public static float constrain(float min, float max, float v) {
        return Math.max(min, Math.min(max, v));
    }

    /**
     * Given the float value of an int (such as alpha),
     *
     * @param alpha
     * @return
     */
    public static int shiftedIntFloatToByteInt(@FloatRange(from = 0f, to = 1f) float alpha) {
        return (int) (255f * alpha);
    }

    public static float shiftedByteIntToIntFloat(@IntRange(from = 0, to = 255) int alpha) {
        return (float) (alpha / 255);
    }
}

