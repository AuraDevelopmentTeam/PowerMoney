package dev.aura.powermoney.network.packet.serverbound;

import dev.aura.powermoney.helper.AssertHelper;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.minecraft.util.math.BlockPos;
import org.junit.Test;

public class PacketChangeRequiresReceiverDataTest {
  @Test
  public void startDataTest() {
    AssertHelper.testPacket(
        PacketChangeRequiresReceiverData.startData(
            UUID.nameUUIDFromBytes("foobar".getBytes(StandardCharsets.UTF_8)),
            new BlockPos(-123, 123, 0)));
  }

  @Test
  public void stopDataTest() {
    AssertHelper.testPacket(PacketChangeRequiresReceiverData.stopData());
  }
}
