package net.landofrails.landofsignals.models;

import org.lwjgl.opengl.GL11;

import java.util.EmptyStackException;
import java.util.Stack;

public class ColorRGBA implements Cloneable {
    /**
     * Default black color.
     */
    public static final ColorRGBA BLACK = new ColorRGBA(0.0F, 0.0F, 0.0F);
    /**
     * Default blue color.
     */
    public static final ColorRGBA BLUE = new ColorRGBA(0.0F, 0.0F, 1.0F);
    /**
     * Default green color.
     */
    public static final ColorRGBA GREEN = new ColorRGBA(0.0F, 1.0F, 0.0F);
    /**
     * Default red color.
     */
    public static final ColorRGBA RED = new ColorRGBA(1.0F, 0.0F, 0.0F);
    /**
     * Default white color.
     */
    public static final ColorRGBA WHITE = new ColorRGBA(1.0F, 1.0F, 1.0F);
    /**
     * Default purple color. Often used to visualise model or texture errors.
     */
    public static final ColorRGBA PURPLE = new ColorRGBA(0.5F, 0.0F, 0.5F);

    /**
     * Multiplier for red color component. Applied to all calls to {@link #getR()}.
     */
    private static float mult_r = 1.0F;
    /**
     * Multiplier for green color component. Applied to all calls to {@link #getG()}.
     */
    private static float mult_g = 1.0F;
    /**
     * Multiplier for blue color component. Applied to all calls to {@link #getB()}.
     */
    private static float mult_b = 1.0F;
    /**
     * Multiplier for alpha color component. Applied to all calls to {@link #getA()}.
     */
    private static float mult_a = 1.0F;
    /**
     * A {@link java.util.Stack Stack} to keep track of which colour multipliers have been applied to {@link ColorRGBA}.
     */
    private static final Stack<ColorRGBA> multiplierStack = new Stack<ColorRGBA>();

    /**
     * Red color component, ranging from {@code 0.0F} to {@code 1.0F}.
     */
    private float r;
    /**
     * Green color component, ranging from {@code 0.0F} to {@code 1.0F}.
     */
    private float g;
    /**
     * Blue color component, ranging from {@code 0.0F} to {@code 1.0F}.
     */
    private float b;
    /**
     * Alpha channel component, ranging from {@code 0.0F} to {@code 1.0F}.
     */
    private float a;


    /**
     * Instantiate a default white color.
     */
    public ColorRGBA() {
        this(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Instantiate a color with the given hexadecimal value.
     */
    public ColorRGBA(int hex) {
        this(encodeHEXtoRGBA_R(hex), encodeHEXtoRGBA_G(hex), encodeHEXtoRGBA_B(hex), encodeHEXtoRGBA_A(hex));
    }

    /**
     * Instantiate a color with the given hexadecimal String.
     */
    public ColorRGBA(String hex) {
        this();
        set(hex);
    }

    /**
     * Instantiate a color of given RGB components with default alpha channel value.
     */
    public ColorRGBA(float r, float g, float b) {
        this(r, g, b, 1.0F);
    }

    /**
     * Instantiate a color of given RGBA components.
     */
    public ColorRGBA(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }


    /**
     * Client-side method to apply this color.
     */
    public void apply() {
        GL11.glColor4f(getR(), getG(), getB(), getA());
    }

    @Override
    public ColorRGBA clone() {
        return new ColorRGBA(r, g, b, a);
    }

    /**
     * Returns a new color with 10% brightness of the original color.
     */
    public ColorRGBA dark() {
        return multiply(0.1F);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ColorRGBA) {
            return toHEX() == ((ColorRGBA) obj).toHEX();
        }
        return false;
    }

    public float getA() {
        return a * mult_a;
    }

    public float getB() {
        return b * mult_b;
    }

    public float getG() {
        return g * mult_g;
    }

    public float getR() {
        return r * mult_r;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(r);
        result = prime * result + Float.floatToIntBits(g);
        result = prime * result + Float.floatToIntBits(b);
        result = prime * result + Float.floatToIntBits(a);
        return result;
    }

    /**
     * Returns a new color containing this color's RGB channels multiplied by the given value.<br>
     * Preserves alpha channel value.
     */
    public ColorRGBA multiply(float val) {
        return new ColorRGBA(r * val, g * val, b * val, a);
    }

    /**
     * Returns a new color containing this color's RGB channels offset by the given value, wrapping around {@code 1.0F}.<br>
     * Preserves alpha channel value.
     */
    public ColorRGBA offset(float val) {
        //@formatter:off
        return new ColorRGBA((r + val) % 1.0F,
                (g + val) % 1.0F,
                (b + val) % 1.0F,
                a);
        //@formatter:on
    }

    /**
     * Returns a variant of this color which appears to 'pulse' or 'flash'.
     */
    public ColorRGBA pulse() {
        float amount = -0.25F + (float) (Math.abs(Math.sin(System.currentTimeMillis() / 250.0D)) * 1.5F);
        if (amount < 0.0F) {
            amount = 0.0F;
        }

        return multiply(amount);
    }

    /**
     * Apply the given hexadecimal number as the new color.
     *
     * @param hex - The new color value, as hexadecimal number.
     */
    public void set(int hex) {
        r = encodeHEXtoRGBA_R(hex);
        g = encodeHEXtoRGBA_G(hex);
        b = encodeHEXtoRGBA_B(hex);
        a = encodeHEXtoRGBA_A(hex);
    }

    /**
     * Parse and apply the given String as the new color value.<br>
     * The given String will be interpreted as hexadecimal String of format {@code AARRGGBB}.
     *
     * @param s - The new value. May be prefixed with either {@code #}, {@code 0x} or {@code 0X}.
     * @see Integer#parseInt(String, int)
     */
    public void set(String s) {
        s = s.toLowerCase();
        if (s.length() > 0) {
            if (s.startsWith("#")) {
                s = s.substring(1);
            } else if (s.startsWith("0x")) {
                s = s.substring(2);
            }

            try {
                if (s.length() > 6) {
                    set((Integer.parseInt(s.substring(0, 6), 16) << 8) | Integer.parseInt(s.substring(6), 16));
                } else {
                    set((Integer.parseInt(s, 16) << 8) | 0xFF);
                }
            } catch (NumberFormatException ex) {
                /* Silent catch. */
            }
        }
    }

    /**
     * Set a new alpha channel value for this color.
     *
     * @return This ColorRGB instance.
     */
    public ColorRGBA setAlpha(float a) {
        this.a = a;
        return this;
    }

    /**
     * Set the given RGB values as new color, not affecting the alpha channel.
     */
    public void setColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Set given RGBA values as new color.
     */
    public void setColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Encode this color into 32-Bit HEX color space, with the alpha channel in front.<br>
     * This method can be used to convert this color into Minecraft's ARGB color scheme.
     */
    public int toAHEX() {
        return encodeRGBtoHEX(a, r, g, b);
    }

    /**
     * Encode this color into 32-Bit HEX color space.
     */
    public int toHEX() {
        return encodeRGBtoHEX(r, g, b, a);
    }

    @Override
    public String toString() {
        String colStr = Integer.toHexString(toHEX()).toUpperCase();
        if (colStr.length() < 8) {
            colStr = "00000000".substring(colStr.length()) + colStr;
        }
        return "#" + colStr;
    }

    /**
     * Encodes the given HEX color space into alpha RGBA color component.
     */
    public static float encodeHEXtoRGBA_A(int hex) {
        return (hex & 0xFF) / 255.0F;
    }

    /**
     * Encodes the given HEX color space into blue RGBA color component.
     */
    public static float encodeHEXtoRGBA_B(int hex) {
        return (hex >> 8 & 0xFF) / 255.0F;
    }

    /**
     * Encodes the given HEX color space into green RGBA color component.
     */
    public static float encodeHEXtoRGBA_G(int hex) {
        return (hex >> 16 & 0xFF) / 255.0F;
    }

    /**
     * Encodes the given HEX color space into red RGBA color component.
     */
    public static float encodeHEXtoRGBA_R(int hex) {
        return (hex >> 24 & 0xFF) / 255.0F;
    }

    /**
     * Encodes the given red, green and blue color components into HEX color space.
     */
    public static int encodeRGBtoHEX(float r, float g, float b, float a) {
        return (((int) (r * 255F + 0.5F) & 0xFF) << 24) | (((int) (g * 255F + 0.5F) & 0xFF) << 16) | (((int) (b * 255F + 0.5F) & 0xFF) << 8) | (((int) (a * 255F + 0.5F) & 0xFF));
    }

    /**
     * Pops the top-most entry on the {@link #multiplierStack color multiplier stack}.<br>
     * Raises an exception if the stack was empty.
     *
     * @throws EmptyStackException If the popped stack was empty.
     */
    public static void multiplierPop() {
        ColorRGBA mult = multiplierStack.pop();
        mult_r = mult.r;
        mult_g = mult.g;
        mult_b = mult.b;
        mult_a = mult.a;
    }

    /**
     * Multiplies the given colors with all subsequent calls to the respective color getters.<br>
     * <br>
     * <b>Remember to undo multiplier actions using {@link #multiplierPop()}.</b>
     *
     * @param r - Color multiplier for red colors.
     * @param g - Color multiplier for green colors.
     * @param b - Color multiplier for blue colors.
     * @param a - Color multiplier for alpha channel values.
     */
    public static void multiplierPush(float r, float g, float b, float a) {
        multiplierStack.push(new ColorRGBA(mult_r, mult_g, mult_b, mult_a));
        mult_r = r;
        mult_g = g;
        mult_b = b;
        mult_a = a;
    }
}
