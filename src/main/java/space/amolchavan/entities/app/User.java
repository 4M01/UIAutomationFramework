package space.amolchavan.entities.app;

import com.creditdatamw.zerocell.annotation.Column;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class User {
    @Column(name = "userName", index = 0)
    private String userName;

    @Column(name = "countryCode", index = 1)
    private String countryCode;

    @Column(name = "mobileNumber", index = 2)
    private String mobileNumber ;

    @Column(name = "mobilePin", index = 3)
    private String mobilePin;

    @Column(name = "emailPin", index = 4)
    private String emailPin;

    @Column(name = "userRole", index = 5)
    private String userRole;

    @Column(name = "sites", index = 6)
    private String sites;

    @Column(name = "Password", index = 7)
    private String Password;
}
