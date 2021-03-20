package cz.muni.fi.pa165.currency;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan
@EnableAspectJAutoProxy()
public class JavaConfig {

    @Bean
    public ExchangeRateTableImpl exchangeRateTable() {
        return new ExchangeRateTableImpl();
    }

    @Bean
    public CurrencyConvertorImpl currencyConvertor(ExchangeRateTable exchangeRateTable) {
        return new CurrencyConvertorImpl(exchangeRateTable);
    }
}
