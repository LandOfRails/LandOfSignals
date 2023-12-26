package net.landofrails.api.contentpacks.v2.flares;

import java.util.Map;
import java.util.function.Predicate;

public class Flare {

    /**
     * Required. Needs to be named after the element in the model.
     */
    private String id;

    /**
     * Optional. Will set the color of the flare. Default: OxFFFFFF (white)
     */
    private String color = "#FFFFFF";

    /**
     * Optional. Will always activate light flare. Default: false
     */
    private boolean alwaysOn = false;

    /**
     * Required for: ContentPackSignal. States that will trigger this light flare.
     */
    private String[] states;

    /**
     * Required for: ContentPackComplexSignal. Groups+States that will trigger this light flare.
     */
    private Map<String, String> groupStates;

    public Flare(String id, String color, boolean alwaysOn, String[] states) {
        this.id = id;
        this.color = color;
        this.alwaysOn = alwaysOn;
        this.states = states;
    }

    public Flare(String id, String color, boolean alwaysOn, Map<String, String> groupStates) {
        this.id = id;
        this.color = color;
        this.alwaysOn = alwaysOn;
        this.groupStates = groupStates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isAlwaysOn() {
        return alwaysOn;
    }

    public void setAlwaysOn(boolean alwaysOn) {
        this.alwaysOn = alwaysOn;
    }

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }

    public Map<String, String> getGroupStates() {
        return groupStates;
    }

    public void setGroupStates(Map<String, String> groupStates) {
        this.groupStates = groupStates;
    }

    public float[] getRenderColor(){
        float[] rgb = new float[3];

        // 33,66,99 or 80;160;240 or 010:020:030 or 0-050-230
        Predicate<String> isRGB = rawColor -> rawColor.matches("((\\d{1,3}[,;:-]){2}\\d{1,3})");
        // #123DEF or 0xFFAA00
        Predicate<String> isHEX = rawColor -> rawColor.matches("(#|0x)[A-Z\\d]{6}");

        if(isRGB.test(color)){
            String[] rawColors = color.split("[,;:-]");
            rgb[0] = Integer.parseInt(rawColors[0]) / 255f;
            rgb[1] = Integer.parseInt(rawColors[1]) / 255f;
            rgb[2] = Integer.parseInt(rawColors[2]) / 255f;
        }else if(isHEX.test(color)){
            String rawColors = color.replaceAll("(#|0x)", "");
            rgb[0] = Integer.valueOf( rawColors.substring( 0, 2 ), 16 ) / 255f;
            rgb[1] = Integer.valueOf( rawColors.substring( 2, 4 ), 16 ) / 255f;
            rgb[2] = Integer.valueOf( rawColors.substring( 4, 6 ), 16 ) / 255f;
        }else{
            String error = "Issues with flare \"%s\": \"%s\" is not rgb or hex!";
            throw new RuntimeException(String.format(error, id, color));
        }

        return rgb;
    }

}
