/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020, 2021, 2022, 2023 shedaniel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.shedaniel.rei.impl.client.config.entries;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.rei.RoughlyEnoughItemsCore;
import me.shedaniel.rei.RoughlyEnoughItemsCoreClient;
import me.shedaniel.rei.api.common.plugins.PluginManager;
import me.shedaniel.rei.impl.client.gui.screen.ConfigReloadingScreen;
import me.shedaniel.rei.impl.client.search.argument.Argument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@ApiStatus.Internal
public class ReloadPluginsEntry extends AbstractConfigListEntry<Unit> {
    private int width;
    private AbstractWidget reloadPluginsButton = new Button(0, 0, 0, 20, Component.empty(), button -> {
        RoughlyEnoughItemsCore.PERFORMANCE_LOGGER.clear();
        RoughlyEnoughItemsCoreClient.reloadPlugins(null, null);
    }, Supplier::get) {
        @Override
        public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
            if (PluginManager.areAnyReloading()) {
                Screen screen = Minecraft.getInstance().screen;
                Minecraft.getInstance().setScreen(new ConfigReloadingScreen(Component.translatable("text.rei.config.is.reloading"), PluginManager::areAnyReloading, () -> Minecraft.getInstance().setScreen(screen), null));
            } else {
                super.render(matrices, mouseX, mouseY, delta);
            }
        }
    };
    private AbstractWidget reloadSearchButton = new Button(0, 0, 0, 20, Component.empty(), button -> {
        Argument.resetCache(true);
    }, Supplier::get) {};
    private List<AbstractWidget> children = ImmutableList.of(reloadPluginsButton, reloadSearchButton);
    
    public ReloadPluginsEntry(int width) {
        super(Component.empty(), false);
        this.width = width;
        reloadPluginsButton.setMessage(Component.translatable("text.rei.reload_config"));
        reloadSearchButton.setMessage(Component.translatable("text.rei.reload_search"));
    }
    
    @Override
    public Unit getValue() {
        return Unit.INSTANCE;
    }
    
    @Override
    public Optional<Unit> getDefaultValue() {
        return Optional.of(Unit.INSTANCE);
    }
    
    @Override
    public void save() {
    
    }
    
    @Override
    public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isSelected, delta);
        Window window = Minecraft.getInstance().getWindow();
        this.reloadPluginsButton.active = this.isEditable();
        this.reloadPluginsButton.setY(y);
        this.reloadPluginsButton.setWidth(width / 2 - 2);
        this.reloadPluginsButton.setX(x + entryWidth / 2 - width / 2);
        this.reloadPluginsButton.render(matrices, mouseX, mouseY, delta);
        this.reloadSearchButton.active = this.isEditable() && Argument.hasCache();
        this.reloadSearchButton.setY(y);
        this.reloadSearchButton.setWidth(width / 2 - 2);
        this.reloadSearchButton.setX(x + entryWidth / 2 + 2);
        this.reloadSearchButton.render(matrices, mouseX, mouseY, delta);
    }
    
    @Override
    public List<? extends GuiEventListener> children() {
        return children;
    }
    
    @Override
    public List<? extends NarratableEntry> narratables() {
        return children;
    }
}
