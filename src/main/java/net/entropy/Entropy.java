package net.entropy;

import net.entropy.creativetab.EntropyCreativeTab;
import net.entropy.entity.EntropyEntities;
import net.entropy.items.EntropyItems;
import net.entropy.particles.EntropyParticles;
import net.entropy.sounds.EntropySounds;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Entropy implements ModInitializer {
	public static final String MOD_ID = "entropy";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		EntropyItems.initialize();
		EntropyEntities.initialize();
		EntropyParticles.initialize();
		EntropySounds.initialize();
		EntropyCreativeTab.initialize();

		LOGGER.info("\n\nFucking hell who caused all this *CHAOS*?! Haha, get the joke? No?... :( \n");
	}
}