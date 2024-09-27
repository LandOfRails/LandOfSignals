package net.landofrails.api.contentpacks.v2.flares;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJGroup;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.render.block.BlockRenderException;

import java.util.Map;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class Flare {

    /**
     * Required. Needs to be named after the element in the model.
     */
    private String id;

    /**
     * Optional. Will set the color of the flare. Default: OxFFFFFF (white)
     */
    private String color;

    /**
     * Required. Will set the rotation of the flare.
     */
    private Integer rotation;

    /**
     * Optional. Will set the rotation after translation. Default: 0
     */
    private Integer postRotation;

    /**
     * Optional. Will set the pitch of the flare. Default: 0
     */
    private Integer pitch;

    /**
     * Required. Will set the offset of the flare.
     */
    private Float offset;

    /**
     * Optional. Scaling will be multiplied by the given value. Default: 1
     */
    private Float scalingMultiplier;

    /**
     * Optional. Will always activate light flare. Default: false
     */
    private Boolean alwaysOn;

    /**
     * Required for: ContentPackSignal & ContentPackComplexSignal. States that will trigger this light flare.
     */
    private String[] states;

    /**
     * Required for: ContentPackComplexSignal. Group+States that will trigger this light flare.
     */
    private String groupId;

    /**
     * Required if: signal/sign/signalbox/asset contains more than one OBJ. Maps the flare to the specified obj
     */
    private String objPath;

    /**
     * Required if: signal/sign/signalbox/asset contains an OBJ that is used multiple times. Default: 0
     */

    private Integer objPathIndex;

    /**
     * Required if: Only parts of the OBJ are used. Needed to calculate accurate center of model
     */
    private String[] objGroups;

    /**
     * Optional. If left empty the default flare-image will be used.
     */
    private String texture;

    /**
     * Optional. Should the flare be scaled with distance? Default: true
     */
    private Boolean scaleWithDistance;

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

    public Flare(String id, String color, int rotation, int pitch, float offset, boolean alwaysOn, String groupId) {
        this.id = id;
        this.color = color;
        this.rotation = rotation;
        this.pitch = pitch;
        this.offset = offset;
        this.alwaysOn = alwaysOn;
        this.groupId = groupId;
    }

    public Flare(String id, String color, int rotation, int pitch, float offset, boolean alwaysOn) {
        this.id = id;
        this.color = color;
        this.rotation = rotation;
        this.pitch = pitch;
        this.offset = offset;
        this.alwaysOn = alwaysOn;
    }

    public Flare(Flare other) {
        this.id = other.getId();
        this.color = other.getColor();
        this.rotation = other.getRotation();
        this.postRotation = other.getPostRotation();
        this.pitch = other.getPitch();
        this.offset = other.getOffset();
        this.scalingMultiplier = other.getScalingMultiplier();
        this.alwaysOn = other.isAlwaysOn();
        this.states = other.getStates();
        this.texture = other.getTexture();
        this.objPath = other.getObjPath();
        this.objGroups = other.getObjGroups();
        this.objPathIndex = other.getObjPathIndex();
        this.scaleWithDistance = other.getScaleWithDistance();
        this.groupId = other.getGroupId();
        this.precalculatedData = null;
        validate();
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

    public Integer getPostRotation() {
        return postRotation;
    }

    public void setPostRotation(Integer postRotation) {
        this.postRotation = postRotation;
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

    public Float getScalingMultiplier() {
        return scalingMultiplier;
    }

    public void setScalingMultiplier(Float scalingMultiplier) {
        this.scalingMultiplier = scalingMultiplier;
    }

    public boolean isAlwaysOn() {
        return alwaysOn;
    }

    public void setAlwaysOn(boolean alwaysOn) {
        this.alwaysOn = alwaysOn;
    }

    public String[] getStates() {
        if(states == null)
            states = new String[0];
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getObjPath() {
        return objPath;
    }

    public void setObjPath(String objPath) {
        this.objPath = objPath;
    }

    public Integer getObjPathIndex() {
        return objPathIndex;
    }

    public void setObjPathIndex(Integer objPathIndex) {
        this.objPathIndex = objPathIndex;
    }

    public String[] getObjGroups() {
        if(objGroups == null)
            objGroups = new String[0];
        return objGroups;
    }

    public void setObjGroups(String[] objGroups) {
        this.objGroups = objGroups;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public boolean getScaleWithDistance() {
        if(scaleWithDistance == null)
            scaleWithDistance = true;
        return scaleWithDistance;
    }

    public void setScaleWithDistance(boolean scaleWithDistance) {
        this.scaleWithDistance = scaleWithDistance;
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
            throw new BlockRenderException(String.format(error, id, color));
        }

        return rgb;
    }

    public void validate(){

        // Default values
        if(color == null){
            color = "#FFFFFF";
        }

        if(postRotation == null){
            postRotation = 0;
        }

        if(pitch == null){
            pitch = 0;
        }

        if(scalingMultiplier == null){
            scalingMultiplier = 1f;
        }

        if(alwaysOn == null){
            alwaysOn = false;
        }

        if(objPathIndex == null){
            objPathIndex = 0;
        }

    }

    public void savePrecalculatedData(Map<String, OBJGroup> flareGroups, Vec3d scale, double lampScale, Vec3d preOffset, Vec3d postOffset, Vec3d rotation, Identifier flareTextureIdentifier) {
        if(this.precalculatedData != null)
            return;
        this.precalculatedData = new PrecalculatedData(flareGroups, scale, lampScale, preOffset, postOffset, rotation, flareTextureIdentifier);
    }

    public PrecalculatedData getPrecalculatedData() {
        return precalculatedData;
    }

    public static class PrecalculatedData {

        public final Map<String, OBJGroup> flareGroups;

        public final Vec3d scale;

        public final double lampScale;

        public final Vec3d rotation;

        public final Vec3d preOffset;

        public final Vec3d postOffset;

        public final Identifier flareTextureIdentifier;

        public PrecalculatedData(Map<String, OBJGroup> flareGroups, Vec3d scale, double lampScale, Vec3d preOffset, Vec3d postOffset, Vec3d rotation, Identifier flareTextureIdentifier){
            this.flareGroups = flareGroups;
            this.scale = scale;
            this.lampScale = lampScale;
            this.preOffset = preOffset;
            this.postOffset = postOffset;
            this.rotation = rotation;
            this.flareTextureIdentifier = flareTextureIdentifier;
        }

    }

}
