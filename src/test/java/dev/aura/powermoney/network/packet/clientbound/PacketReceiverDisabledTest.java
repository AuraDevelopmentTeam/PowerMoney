package dev.aura.powermoney.network.packet.clientbound;

import dev.aura.powermoney.helper.AssertHelper;
import org.junit.Test;

public class PacketReceiverDisabledTest {
  @Test
  public void encodingDecodingTest() {
    AssertHelper.testPacket(new PacketReceiverDisabled());
  }
}
