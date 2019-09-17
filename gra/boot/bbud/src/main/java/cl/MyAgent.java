package cl;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.matcher.ElementMatchers;
 
import java.lang.instrument.Instrumentation;
 
import static net.bytebuddy.matcher.ElementMatchers.named;
 
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyAgent {
 
    public static void premain(String agentArgs, Instrumentation inst) throws Exception {
        log.info("start MyAgent");

        new AgentBuilder.Default()
            .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager()) // ?
            .type(named("org.ehcache.impl.internal.sizeof.DefaultSizeOfEngine"))
            .transform(new Transformer.ForAdvice()
                .include(MyAgent.class.getClassLoader())
                .advice(ElementMatchers.named("service"), "cl.TimerAdvice"))
            .installOn(inst);

        log.info("finish MyAgent");
    }
}
