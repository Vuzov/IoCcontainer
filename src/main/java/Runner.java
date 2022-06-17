import com.vuzov.container.LogTicker;
import com.vuzov.container.context.ApplicationContext;
import com.vuzov.container.factory.BeanFactory;
import com.vuzov.container.service.GetTaxi;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Runner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) throws IOException {
        logger.trace("Запущен метод main()");
        // TODO циклическая зависимость
        ApplicationContext applicationContext = new ApplicationContext();
        BeanFactory beanFactory = new BeanFactory(applicationContext);
        applicationContext.setBeanFactory(beanFactory);


        GetTaxi getTaxi = applicationContext.getBean(GetTaxi.class);
        getTaxi.get();

        LogTicker logTicker = applicationContext.getBean(LogTicker.class);

        System.in.read();
    }
}
