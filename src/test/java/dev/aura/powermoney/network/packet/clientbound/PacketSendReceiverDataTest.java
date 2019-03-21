package dev.aura.powermoney.network.packet.clientbound;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.api.MoneyInterface;
import dev.aura.powermoney.helper.AssertHelper;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("dev.aura.powermoney.PowerMoney")
@PrepareForTest(PowerMoney.class)
public class PacketSendReceiverDataTest {
  @Before
  public void mockMoneyInterface() {
    PowerMockito.mockStatic(PowerMoney.class);

    final PowerMoney mockPowerMoney = Mockito.mock(PowerMoney.class);
    final MoneyInterface mockMoneyInterface = Mockito.mock(MoneyInterface.class);

    Mockito.when(PowerMoney.getInstance()).thenReturn(mockPowerMoney);
    Mockito.when(mockPowerMoney.getActiveMoneyInterface()).thenReturn(mockMoneyInterface);
    Mockito.when(mockMoneyInterface.getCurrencySymbol()).thenReturn("Test");
    Mockito.when(mockMoneyInterface.getDefaultDigits()).thenReturn(123);
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
