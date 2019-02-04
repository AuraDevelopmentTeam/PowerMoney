package dev.aura.powermoney.network.packet.clientbound;

import dev.aura.powermoney.common.payment.SpongeMoneyInterface;
import dev.aura.powermoney.helper.AssertHelper;
import java.math.BigDecimal;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("dev.aura.powermoney.common.payment.SpongeMoneyInterface")
@PrepareForTest(SpongeMoneyInterface.class)
public class PacketSendReceiverDataTest {
  @BeforeClass
  public static void mockSpongeMoneyInterface() {
    PowerMockito.mockStatic(SpongeMoneyInterface.class);

    Mockito.when(SpongeMoneyInterface.getMoneySymbol()).thenReturn("Test");
    Mockito.when(SpongeMoneyInterface.getDefaultDigits()).thenReturn(123);
  }

  @Test
  public void nullTest() {
    AssertHelper.testPacket(new PacketSendReceiverData(null, null, null, null, null));
  }

  @Test
  public void valueTest() {
    AssertHelper.testPacket(
        new PacketSendReceiverData(
            4414848415L,
            9161321324489746456L,
            new BigDecimal("45454545449848948949.48974874878979749"),
            null,
            null));
  }
}
