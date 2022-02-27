package note.webnote.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    // repository 패키지
    @Pointcut("execution(* note.webnote.repository.*.*(..))")
    private void repository() {
    }

    // controller 패키지
    @Pointcut("execution(* note.webnote.web.controller.*.*(..))")
    private void controller() {
    }

    // service 패키지
    @Pointcut("execution(* note.webnote.web.service.*.*(..))")
    private void service() {}

    @Before("repository() || controller() || service()")
    public void doLog(JoinPoint joinPoint) {
        log.info("[LogTrace] {}", joinPoint.getSignature().getName());
    }
}
