import com.vuzov.container.context.ApplicationContext;
import com.vuzov.container.service.GetTaxi;
import java.util.HashMap;
import java.util.Map;
import com.vuzov.container.service.impl.CashPaymentSystem;
import com.vuzov.container.service.interfaces.PaymentSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Runner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        logger.trace("Запущен метод main()");
        /*
        Прямой маппинг используется только на тот случай, если какой-то интерфейс имеет более одной имплементации и
        является неким аналогом конфигурационного файла.
         */
        HashMap<Class, Class> ifc2Impl = new HashMap<>(Map.of(PaymentSystem.class, CashPaymentSystem.class));
        ApplicationContext applicationContext = new ApplicationContext(Runner.class.getPackageName(), ifc2Impl);
        applicationContext.run();

        GetTaxi getTaxi = applicationContext.getBean(GetTaxi.class);
        getTaxi.get();
    }
}
