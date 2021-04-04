import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.yfr.finance.service.config.FinanceServiceRootConfig;
import org.yfr.finance.service.service.CrawlerService;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceServiceRootConfig.class)
public class FinanceServiceApplicationTest {

    @Resource
    private CrawlerService crawlerService;

    @Test
    public void test() throws InterruptedException {
        crawlerService.fetchAmericaIdx();

        TimeUnit.MINUTES.sleep(1);
    }

}