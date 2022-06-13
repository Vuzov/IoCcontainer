import com.vuzov.container.LogTicker;
import com.vuzov.container.context.ApplicationContext;
import com.vuzov.container.factory.BeanFactory;
import com.vuzov.container.service.GetTaxi;
import java.io.IOException;
import org.apache.log4j.Logger;


public class Runner {

    private static final Logger logger = Logger.getLogger(Runner.class);

    public static void main(String[] args) throws IOException {
        logger.trace("Запущен метод main()");
        ApplicationContext applicationContext = new ApplicationContext();
        BeanFactory beanFactory = new BeanFactory(applicationContext);
        applicationContext.setBeanFactory(beanFactory);


        GetTaxi getTaxi = applicationContext.getBean(GetTaxi.class);
        getTaxi.get();

        LogTicker logTicker = applicationContext.getBean(LogTicker.class);

        System.in.read();
    }
}
