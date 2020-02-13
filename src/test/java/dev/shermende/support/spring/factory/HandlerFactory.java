package dev.shermende.support.spring.factory;

import dev.shermende.support.spring.db.entity.Payload;
import dev.shermende.support.spring.handler.InterceptorHandler;
import dev.shermende.support.spring.handler.InterceptorValidateHandler;
import dev.shermende.support.spring.handler.InterceptorWrongSupportHandler;
import dev.shermende.support.spring.handler.ReturnHandler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

@Component
public class HandlerFactory extends AbstractFactory<String, ReturnHandler<Payload, Payload>> {
    public static final String WRONG_SUPPORT = "interceptWrongSupport";
    public static final String WRONG_KEY = "anyWrongKey";
    public static final String VALIDATE = "interceptValidate";
    public static final String INTERCEPT = "intercept";

    public HandlerFactory(
        BeanFactory beanFactory
    ) {
        super(beanFactory);
    }

    @Override
    protected void registration() {
        this.registry(WRONG_SUPPORT, InterceptorWrongSupportHandler.class);
        this.registry(VALIDATE, InterceptorValidateHandler.class);
        this.registry(INTERCEPT, InterceptorHandler.class);
    }

}
