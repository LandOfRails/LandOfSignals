package net.landofrails.api.contentpacks.v2.flares;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJGroup;

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
     * Required. Will set the rotation of the flare.
     */
    private Integer rotation;

    /**
     * Optional. Will set the pitch of the flare. Default: 0
     */
    private Integer pitch = 0;

    /**
     * Required. Will set the offset of the flare.
     */
    private Float offset;


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

    /**
     * Required if: signal/sign/signalbox/asset contains more than one OBJ. Maps the flare to the specified obj
     */
    private String objPath;

    /**
     * Required if: Only parts of the OBJ are used. Needed to calculate accurate center of model
     */
    private String[] objGroups;

    private PrecalculatedData precalculatedData;

    public Flare(String id, String color, int rotation, int pitch, float offset, boolean alwaysOn, String[] states) {
        this.id = id;
        this.color = color;
        this.rotation = rotation;
        this.pitch = pitch;
        this.offset = offset;
        this.alwaysOn = alwaysOn;
        this.states = states;
    }

    public Flare(String id, String color, int rotation, int pitch, float offset, boolean alwaysOn, Map<String, String> groupStates) {
        this.id = id;
        this.color = color;
        this.rotation = rotation;
        this.pitch = pitch;
        this.offset = offset;
        this.alwaysOn = alwaysOn;
        this.groupStates = groupStates;
    }

    public Flare(String id, String color, int rotation, int pitch, float offset, boolean alwaysOn) {
        this.id = id;
        this.color = color;
        this.rotation = rotation;
        this.pitch = pitch;
        this.offset = offset;
        this.alwaysOn = alwaysOn;
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

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    public Integer getPitch() {
        return pitch;
    }

    public void setPitch(Integer pitch) {
        this.pitch = pitch;
    }

    public Float getOffset() {
        return offset;
    }

    public void setOffset(Float offset) {
        this.offset = offset;
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

    public String getObjPath() {
        return objPath;
    }

    public void setObjPath(String objPath) {
        this.objPath = objPath;
    }

    public String[] getObjGroups() {
        if(objGroups == null)
            objGroups = new String[0];
        return objGroups;
    }

    public void setObjGroups(String[] objGroups) {
        this.objGroups = objGroups;
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

    public void savePrecalculatedData(Map<String, OBJGroup> flareGroups, Vec3d scale, double lampScale, Vec3d offset, Vec3d rotation) {
        if(this.precalculatedData != null)
            return;
        this.precalculatedData = new PrecalculatedData(flareGroups, scale, lampScale, offset, rotation);
    }

    public PrecalculatedData getPrecalculatedData() {
        return precalculatedData;
    }

    public class PrecalculatedData {

        public final Map<String, OBJGroup> flareGroups;

        public final Vec3d scale;

        public final double lampScale;

        public final Vec3d rotation;

        public final Vec3d offset;

        public PrecalculatedData(Map<String, OBJGroup> flareGroups, Vec3d scale, double lampScale, Vec3d offset, Vec3d rotation){
            this.flareGroups = flareGroups;
            this.scale = scale;
            this.lampScale = lampScale;
            this.offset = offset;
            this.rotation = rotation;
        }

    }

}
