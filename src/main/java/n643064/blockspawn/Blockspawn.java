package n643064.blockspawn;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.openjdk.nashorn.internal.ir.annotations.Ignore;

import static n643064.blockspawn.Config.CONFIG;

@Mod(Blockspawn.MODID)
public class Blockspawn
{
    public static final String MODID = "blockspawn";

    public Blockspawn()
    {
        Config.setup();
        MinecraftForge.EVENT_BUS.addListener(this::playerBreakBLock);
    }

    @SuppressWarnings("deprecation")
    public void playerBreakBLock(BlockEvent.BreakEvent event)
    {
        final BlockPos bp = event.getPos();
        final Level l = event.getPlayer().level();

        final Config.ConfigEntry[] entries = CONFIG.entries().get(BuiltInRegistries.BLOCK.getKey(event.getState().getBlock()).toString());
        for (Config.ConfigEntry e : entries)
        {
            double d = l.random.nextDouble();
            System.out.println(e.mob() + " " + d);
            if (d <= e.chance())
            {
                final Entity mob = BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(e.mob())).create(l);
                assert mob!=null;
                mob.setPos(bp.getX(), bp.getY(), bp.getZ());

                l.addFreshEntity(mob);
                break;
            }
        }
    }
}
