package me.braekpo1nt.commands.activities.mlg.configurers;

import me.braekpo1nt.commands.activities.abstracts.configurers.ChunkConfigurer;
import me.braekpo1nt.commands.activities.mlg.MLGActivity;
import me.braekpo1nt.manhunttraining.Main;

public class MLGChunkConfigurer extends ChunkConfigurer {
    
    public MLGChunkConfigurer(Main plugin) {
        super(plugin);
    }

    @Override
    protected String getConfigString() {
        return MLGActivity.CHUNK;
    }
}
