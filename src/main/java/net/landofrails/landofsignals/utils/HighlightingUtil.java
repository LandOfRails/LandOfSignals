package net.landofrails.landofsignals.utils;

import cam72cam.mod.model.common.ModelLoader;
import cam72cam.mod.model.common.mesh.Model;
import cam72cam.mod.render.common.ModelConfig;
import cam72cam.mod.render.common.ModelRenderer;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class HighlightingUtil {

    private static Model model;
    private static final Random RANDOM = new Random();

    private static LocalDateTime colortime = LocalDateTime.now();
    private static HighlightingColors color = HighlightingColors.getRandomColor();

    private HighlightingUtil(){
    }

    @SuppressWarnings("java:S1141")
    public static void renderHighlighting(RenderState state) {
        try {

            Model model = getModel();
            state.translate(0.5, 0, 0.5);
            ModelConfig cfg = new ModelConfig().variant(getColor());
            try (ModelRenderer.Binding bound = ModelRenderer.getRendererFor(model).bind(cfg, state)) {

                bound.enqueueOpaque();

            } catch (Exception e) {
                // Not good, not bad enough to fail
            }

        } catch (Exception e) {
            // Not good, not bad enough to fail
        }

    }

    private static String getColor(){
        LocalDateTime now = LocalDateTime.now();
        if(colortime.plusSeconds(1).isBefore(now)){
            HighlightingColors newColor;
            do{
                newColor = HighlightingColors.getRandomColor();
            }while(newColor.equals(color));
            color = newColor;
            colortime = LocalDateTime.now();
        }
        return color.getFolder();
    }

    private static Model getModel() throws Exception {
        if(model == null){
            model = ModelLoader.load(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/connection-box/connection-box.obj"), HighlightingColors.getAllColorsAsStrings());
        }
        return model;
    }

    private enum HighlightingColors {

        WHITE(""),
        AMBER("amber"),
        BLACK("black"),
        BLUE_LIGHT("blue_light"),
        GRAY("gray"),
        GRAY_DARK("gray_dark"),
        GREEN_DARK("green_dark"),
        GREEN_LIGHT("green_light"),
        ORANGE_LIGHT("orange_light"),
        RED("red"),
        YELLOW_GREEN("yellow_green");

        private final String folder;

        HighlightingColors(String folder){
            this.folder = folder;
        }

        private String getFolder(){
            return this.folder;
        }

        public static HighlightingColors getRandomColor(){

            int randomNumber = RANDOM.nextInt(HighlightingColors.values().length);
            return HighlightingColors.values()[randomNumber];
        }

        public static List<String> getAllColorsAsStrings(){
            return Arrays.stream(HighlightingColors.values())
                    .map(HighlightingColors::getFolder)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

    }

}
