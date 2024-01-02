package fi.dy.masa.malilib.util;

import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.chunk.Chunk;

public class ChunkUtils {
    public int getHighestSectionYOffset(Chunk chunk)
    {
        // Replaces getHighestNonEmptySectionYOffset()
        int yMaxMath = chunk.getHighestNonEmptySection();

        yMaxMath = yMaxMath == -1 ? chunk.getBottomY() : ChunkSectionPos.getBlockCoord(chunk.sectionIndexToCoord(yMaxMath));

        return yMaxMath;
    }
}
