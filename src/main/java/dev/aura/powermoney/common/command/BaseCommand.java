package dev.aura.powermoney.common.command;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.common.config.PowerMoneyConfigWrapper;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

public class BaseCommand extends CommandBase {
  private static final String BASE_PERMISSION = PowerMoney.ID + ".command";
  private static final String RELOAD_PERMISSION = BASE_PERMISSION + ".reload";

  public BaseCommand() {
    PermissionAPI.registerNode(RELOAD_PERMISSION, DefaultPermissionLevel.OP, "Allows to reload " +PowerMoney.NAME);
  }

  @Override
  public String getName() {
    return PowerMoney.ID;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return getName() + " reload";
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return !(sender instanceof EntityPlayer)
        || PermissionAPI.hasPermission((EntityPlayer) sender, RELOAD_PERMISSION);
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    if ((args.length == 0)
        || ((args.length == 1) && ("reload".startsWith(Pattern.quote(args[0]))))) {
      return Collections.singletonList("reload");
    } else {
      return Collections.emptyList();
    }
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if ((args.length != 1) || ("reload".equals(args[0]))) {
      throw new WrongUsageException(getUsage(sender));
    }

    PowerMoneyConfigWrapper.loadConfig();
  }
}
