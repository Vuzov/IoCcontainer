import com.vuzov.container.context.ApplicationContext;
import com.vuzov.container.service.GetTaxi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Runner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        logger.trace("Запущен метод main()");
        ApplicationContext applicationContext = new ApplicationContext(Runner.class.getPackageName());
        applicationContext.run();

        GetTaxi getTaxi = applicationContext.getBean(GetTaxi.class);
        getTaxi.get();
    }
}
