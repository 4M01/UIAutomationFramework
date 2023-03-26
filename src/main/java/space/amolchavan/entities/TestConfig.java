package space.amolchavan.entities;

import org.aeonbits.owner.Config;


public interface TestConfig extends Config {
    String url();
    String patientUrl();
    Long implicitWait();
    Long explicitWait();
    String target();
    String gridUrl();
    String gridPort();
    Boolean headless();
    String screenShotStrategy();
}
