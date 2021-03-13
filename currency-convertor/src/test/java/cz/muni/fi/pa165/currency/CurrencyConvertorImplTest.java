package cz.muni.fi.pa165.currency;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyConvertorImplTest {

    private static final Currency CZK = Currency.getInstance("CZK");
    private static final Currency USD = Currency.getInstance("USD");

    @Mock
    private ExchangeRateTable exchangeRateTable;

    private CurrencyConvertor currencyConvertor;

    @Before
    public void init() {
        currencyConvertor = new CurrencyConvertorImpl(exchangeRateTable);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void testConvert() throws ExternalServiceFailureException {
        when(exchangeRateTable.getExchangeRate(CZK, USD))
                .thenReturn(new BigDecimal("0.05"));

        assertEquals(new BigDecimal("1.01"), currencyConvertor.convert(CZK, USD, new BigDecimal("20.102")));
        assertEquals(new BigDecimal("1.01"), currencyConvertor.convert(CZK, USD, new BigDecimal("20.298")));
        assertEquals(new BigDecimal("1.00"), currencyConvertor.convert(CZK, USD, new BigDecimal("20.100")));
        assertEquals(new BigDecimal("1.02"), currencyConvertor.convert(CZK, USD, new BigDecimal("20.300")));
    }

    @Test
    public void testConvertWithNullSourceCurrency() {
        expectedException.expect(IllegalArgumentException.class);
        currencyConvertor.convert(null, USD, BigDecimal.ONE);
    }

    @Test
    public void testConvertWithNullTargetCurrency() {
        expectedException.expect(IllegalArgumentException.class);
        currencyConvertor.convert(CZK, null, BigDecimal.ONE);
    }

    @Test
    public void testConvertWithNullSourceAmount() {
        expectedException.expect(IllegalArgumentException.class);
        currencyConvertor.convert(CZK, USD, null);
    }

    @Test
    public void testConvertWithUnknownCurrency() throws ExternalServiceFailureException {
        when(exchangeRateTable.getExchangeRate(CZK, USD))
                .thenReturn(null);
        expectedException.expect(UnknownExchangeRateException.class);
        currencyConvertor.convert(CZK, USD, BigDecimal.ONE);
    }

    @Test
    public void testConvertWithExternalServiceFailure() throws ExternalServiceFailureException {
        when(exchangeRateTable.getExchangeRate(CZK, USD))
                .thenThrow(UnknownExchangeRateException.class);
        expectedException.expect(UnknownExchangeRateException.class);
        currencyConvertor.convert(CZK, USD, BigDecimal.ONE);
    }

}
