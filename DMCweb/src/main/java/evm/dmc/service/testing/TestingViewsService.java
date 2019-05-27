package evm.dmc.service.testing;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@ConfigurationProperties("views.testing")
public class TestingViewsService {
    private String testindex;
    private String hello;
    private String hellorest;
    private String hellothyme;
    private String welcome;
    private String showtable;
    private String listbeans;
    private String createalg;
}
