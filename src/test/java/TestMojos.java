
import me.kisoft.StartRabbitMQMojo;
import me.kisoft.StopRabbitMQMojo;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tareq
 */
public class TestMojos {
    
    @Test
    public void testStartAndStop() throws Exception{
        new StartRabbitMQMojo().execute();
        new StopRabbitMQMojo().execute();
    }
  
}
