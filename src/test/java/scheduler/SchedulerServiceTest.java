/**
 * Copyright 2014-2019  the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package scheduler;

import com.webank.fabric.node.manager.Application;
import com.webank.fabric.node.manager.scheduler.DeleteInfoTask;
import com.webank.fabric.node.manager.scheduler.PullBlockInfoTask;
import com.webank.fabric.node.manager.scheduler.StatisticsTransDailyTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SchedulerServiceTest {

    @Autowired
    private PullBlockInfoTask pullBlockTransTask;
    @Autowired
    private StatisticsTransDailyTask statisticsTransDailyTask;
    @Autowired
    private DeleteInfoTask deleteInfoTask;

    @Test
    public void pullBlockInfoTaskTest() {
        pullBlockTransTask.pullBlockStart();
    }


    @Test
    public void statisticsTransDailyTaskTest() {
        statisticsTransDailyTask.taskStart();
    }

    @Test
    public void deleteInfoTaskTest() {
        deleteInfoTask.taskStart();
    }


}
