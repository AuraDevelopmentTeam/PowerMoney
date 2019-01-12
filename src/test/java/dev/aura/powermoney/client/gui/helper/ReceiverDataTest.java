package dev.aura.powermoney.client.gui.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import net.minecraft.client.resources.I18n;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(I18n.class)
public class ReceiverDataTest {
  private static void englishFormatting() {
    Mockito.when(I18n.format("gui.powermoney.decimalseparator")).thenReturn(".");
    Mockito.when(I18n.format("gui.powermoney.groupingseparator")).thenReturn(",");
  }

  private static void germanFormatting() {
    Mockito.when(I18n.format("gui.powermoney.decimalseparator")).thenReturn(",");
    Mockito.when(I18n.format("gui.powermoney.groupingseparator")).thenReturn(".");
  }

  @Before
  public void mockTranslation() {
    PowerMockito.mockStatic(I18n.class);

    Mockito.when(I18n.format("gui.powermoney.energyunit")).thenReturn("FE");
    Mockito.when(I18n.format("gui.powermoney.persecond")).thenReturn("/s");
  }

  @Test
  public void isWaitingTest() {
    final ReceiverData receiverData = ReceiverData.waiting();

    assertTrue(receiverData.isWaiting());
  }

  @Test
  public void isEnabledTest() {
    final ReceiverData receiverData1 = ReceiverData.receiverDisabled();
    final ReceiverData receiverData2 = ReceiverData.setReceiverData(null, null, null, null, 0);

    assertFalse(receiverData1.isWaiting());
    assertFalse(receiverData1.isEnabled());
    assertFalse(receiverData2.isWaiting());
    assertTrue(receiverData2.isEnabled());
  }

  @Test
  public void getEnergyFormattedTest() {
    englishFormatting();

    ReceiverData receiverData =
        ReceiverData.setReceiverData(
            new BigInteger("987654321987654321"),
            new BigInteger("123456789123456789"),
            null,
            null,
            0);

    assertEquals("987,654,321,987,654,321 FE/s", receiverData.getLocalEnergyFormatted());
    assertEquals("123,456,789,123,456,789 FE/s", receiverData.getTotalEnergyFormatted());
  }

  @Test
  public void getEnergyFormattedTestGerman() {
    germanFormatting();

    ReceiverData receiverData =
        ReceiverData.setReceiverData(
            new BigInteger("987654321987654321"),
            new BigInteger("123456789123456789"),
            null,
            null,
            0);

    assertEquals("987.654.321.987.654.321 FE/s", receiverData.getLocalEnergyFormatted());
    assertEquals("123.456.789.123.456.789 FE/s", receiverData.getTotalEnergyFormatted());
  }

  @Test
  public void getMoneyFormattedTest() {
    englishFormatting();

    ReceiverData receiverData =
        ReceiverData.setReceiverData(null, null, new BigDecimal("123456789123456789.4567"), "$", 3);

    assertEquals("123,456,789,123,456,789.457 $/s", receiverData.getMoneyFormatted());
  }

  @Test
  public void getMoneyFormattedTestGerman() {
    germanFormatting();

    ReceiverData receiverData =
        ReceiverData.setReceiverData(null, null, new BigDecimal("123456789123456789.4567"), "€", 3);

    assertEquals("123.456.789.123.456.789,457 €/s", receiverData.getMoneyFormatted());
  }
}
