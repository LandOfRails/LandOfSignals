package net.landofrails.landofsignals.utils;

import org.lwjgl.opengl.ARBMultitexture;

import java.util.EmptyStackException;
import java.util.Stack;

public class RenderUtil {

    public static float lastBrightnessX;
    public static float lastBrightnessY;

    /**
     * Lightmap stack used to remember existing lightmap settings when doing lightmap hacks.
     */
    private static final Stack<LightmapState> lightmapStack = new Stack<>();


    /**
     * Sets Minecraft's lightmap settings accordingly so rendered objects appear as if they are in daylight (similar to disabling lighting). This method can be used when
     * {@code GL11.glDisable(GL11.GL_LIGHTING)} does not produce a sufficiently 'bright' result.
     */
    public static void lightmapBright() {
        final int maxBright = (15 << 20) | (15 << 4);
        lightmapBright(maxBright % 65536, maxBright / 65536);
    }

    /**
     * Overrides the current lightmap texture coordinates to the specified values.<br>
     * Use this to specify brightness more precisely.
     *
     * @see #lightmapBright()
     */
    public static void lightmapBright(final float u, final float v) {
        ARBMultitexture.glMultiTexCoord2fARB(33985, u, v);
    }

    /**
     * Pushes the current lightmap settings so they can be retrieved later.
     */
    public static void lightmapPush() {
        lightmapStack.push(new LightmapState(lastBrightnessX, lastBrightnessY));
    }

    /**
     * Pops the saved lightmap settings from when they were pushed with {@link RenderUtil#lightmapPush()}.<br>
     * Applies the popped settings back to the lightmap.
     *
     * @throws EmptyStackException If the lightmap stack is empty.
     */
    public static void lightmapPop() {
        final LightmapState top = lightmapStack.pop();
        ARBMultitexture.glMultiTexCoord2fARB(33985, top.x, top.y);
    }

    /**
     * Lightmap Stack Object
     *
     * @author Jaffa
     */
    private static class LightmapState {
        public float x, y;


        public LightmapState(final float x, final float y) {
            this.x = x;
            this.y = y;
        }
    }


}
