package n643064.blockspawn;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import static n643064.blockspawn.Config.CONFIG;


@Mod(Blockspawn.MODID)
public class Blockspawn
{
    public static final String MODID = "blockspawn";

    public static final byte[][] CUBE_OFFSET_LUT = new byte[][]
            {
                    new byte[] {-1, -1, -1},
                    new byte[] {-1, -1, 0},
                    new byte[] {-1, -1, 1},
                    new byte[] {0, -1, -1},
                    new byte[] {0, -1, 1},
                    new byte[] {1, -1, -1},
                    new byte[] {1, -1, 0},
                    new byte[] {1, -1, 1},

                    new byte[] {-1, 0, -1},
                    new byte[] {-1, 0, 0},
                    new byte[] {-1, 0, 1},
                    new byte[] {0, 0, -1},
                    new byte[] {0, 0, 0},
                    new byte[] {0, 0, 1},
                    new byte[] {1, 0, -1},
                    new byte[] {1, 0, 0},
                    new byte[] {1, 0, 1},

                    new byte[] {-1, 1, -1},
                    new byte[] {-1, 1, 0},
                    new byte[] {-1, 1, 1},
                    new byte[] {0, 1, -1},
                    new byte[] {0, 1, 0},
                    new byte[] {0, 1, 1},
                    new byte[] {1, 1, -1},
                    new byte[] {1, 1, 0},
                    new byte[] {1, 1, 1},
            };

    public Blockspawn()
    {
        Config.setup();
        MinecraftForge.EVENT_BUS.addListener(this::playerBreakBLock);
    }

    @SuppressWarnings("deprecation")
    public void playerBreakBLock(BlockEvent.BreakEvent event)
    {
        final String s = BuiltInRegistries.BLOCK.getKey(event.getState().getBlock()).toString();
        if (!CONFIG.containsKey(s)) return;

        final BlockPos bp = event.getPos();
        final Level l = event.getPlayer().level();
        final Config.ConfigEntry[] entries = CONFIG.get(s);

        for (Config.ConfigEntry e : entries)
        {
            final double d = l.random.nextDouble();
            if (d <= e.chance())
            {
                final Entity mob = BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(e.mob())).create(l);
                assert mob!=null;
                mob.setPos(bp.getX() + 0.5, bp.getY() + 0.1, bp.getZ() + 0.5);

                if (e.adjustTerrain())
                {
                    for (byte[] offset : CUBE_OFFSET_LUT)
                        l.destroyBlock(bp.offset(offset[0], offset[1], offset[2]), true);
                }
                if (e.targetPlayer() && mob instanceof Mob mob2)
                {
                    mob2.setTarget(event.getPlayer());
                    mob2.setAggressive(true);
                }
                l.addFreshEntity(mob);
                break;
            }
        }
    }
}
