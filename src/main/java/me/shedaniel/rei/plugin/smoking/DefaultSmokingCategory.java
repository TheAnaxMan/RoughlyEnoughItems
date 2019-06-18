/*
 * Roughly Enough Items by Danielshe.
 * Licensed under the MIT License.
 */

package me.shedaniel.rei.plugin.smoking;

import com.mojang.blaze3d.platform.GlStateManager;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.Renderer;
import me.shedaniel.rei.gui.renderers.RecipeRenderer;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import me.shedaniel.rei.gui.widget.SlotWidget;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.plugin.DefaultPlugin;
import net.minecraft.ChatFormat;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class DefaultSmokingCategory implements RecipeCategory<DefaultSmokingDisplay> {
    
    @Override
    public Identifier getIdentifier() {
        return DefaultPlugin.SMOKING;
    }
    
    @Override
    public Renderer getIcon() {
        return Renderer.fromItemStack(new ItemStack(Blocks.SMOKER));
    }
    
    @Override
    public String getCategoryName() {
        return I18n.translate("category.rei.smoking");
    }
    
    @Override
    public RecipeRenderer getSimpleRenderer(DefaultSmokingDisplay recipe) {
        return Renderer.fromRecipe(() -> Arrays.asList(recipe.getInput().get(0)), recipe::getOutput);
    }
    
    @Override
    public List<Widget> setupDisplay(Supplier<DefaultSmokingDisplay> recipeDisplaySupplier, Rectangle bounds) {
        Point startPoint = new Point((int) bounds.getCenterX() - 41, (int) bounds.getCenterY() - 27);
        List<Widget> widgets = new LinkedList<>(Arrays.asList(new RecipeBaseWidget(bounds) {
            @Override
            public void render(int mouseX, int mouseY, float delta) {
                super.render(mouseX, mouseY, delta);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                GuiLighting.disable();
                MinecraftClient.getInstance().getTextureManager().bindTexture(DefaultPlugin.getDisplayTexture());
                blit(startPoint.x, startPoint.y, 0, 54, 82, 54);
                int height = MathHelper.ceil((System.currentTimeMillis() / 250 % 14d) / 1f);
                blit(startPoint.x + 2, startPoint.y + 21 + (14 - height), 82, 77 + (14 - height), 14, height);
                int width = MathHelper.ceil((System.currentTimeMillis() / 250 % 24d) / 1f);
                blit(startPoint.x + 24, startPoint.y + 18, 82, 91, width, 17);
            }
        }));
        List<List<ItemStack>> input = recipeDisplaySupplier.get().getInput();
        widgets.add(new SlotWidget(startPoint.x + 1, startPoint.y + 1, input.get(0), true, true, true));
        widgets.add(new SlotWidget(startPoint.x + 1, startPoint.y + 37, recipeDisplaySupplier.get().getFuel(), true, true, true) {
            @Override
            protected List<String> getExtraToolTips(ItemStack stack) {
                return Collections.singletonList(ChatFormat.YELLOW.toString() + I18n.translate("category.rei.smelting.fuel"));
            }
        });
        widgets.add(new SlotWidget(startPoint.x + 61, startPoint.y + 19, recipeDisplaySupplier.get().getOutput(), false, true, true));
        return widgets;
    }
    
}