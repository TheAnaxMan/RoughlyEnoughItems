/*
 * Roughly Enough Items by Danielshe.
 * Licensed under the MIT License.
 */

package me.shedaniel.rei;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.rei.api.ConfigManager;
import net.minecraft.client.gui.screen.Screen;

import java.util.Optional;
import java.util.function.Supplier;

public class REIModMenuEntryPoint implements ModMenuApi {
    
    @Override
    public String getModId() {
        return "roughlyenoughitems";
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public Optional<Supplier<Screen>> getConfigScreen(Screen screen) {
        return Optional.of(() -> getScreen(screen));
    }
    
    public Screen getScreen(Screen parent) {
        return ConfigManager.getInstance().getConfigScreen(parent);
    }
    
}
