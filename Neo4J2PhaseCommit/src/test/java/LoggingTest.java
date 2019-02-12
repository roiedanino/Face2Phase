import com.insta2phase.Application;
import com.insta2phase.ReplicaApplication;
import com.insta2phase.dal.TwoPhaseLogDao;
import com.insta2phase.entities.TwoPhaseLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoggingTest {

    private TwoPhaseLogDao twoPhaseLogDao;

    @PostConstruct
    public void init(){

    }

    @Autowired
    public void setTwoPhaseLogDao(TwoPhaseLogDao twoPhaseLogDao) {
        this.twoPhaseLogDao = twoPhaseLogDao;
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        twoPhaseLogDao.deleteAll();
    }
}
