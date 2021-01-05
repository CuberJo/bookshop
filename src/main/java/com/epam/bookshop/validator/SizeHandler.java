package com.epam.bookshop.validator;

import com.epam.bookshop.AppTest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// для того, чтобы находу что-то делат, имплементим InvocationHandler
public class SizeHandler implements InvocationHandler {

    private final AppTest.AInterface original;
    public SizeHandler(AppTest.AInterface original) {
        this.original = original;
    }


//    private final Entity originalBaseEntity;
//
//    public SizeHandler(Entity originalBaseEntity) {
//        this.originalBaseEntity = originalBaseEntity;
//    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
       /* if (method.isAnnotationPresent(Size.class)) { //метод mrthod не содержит аннотаций
            // но у нас есть original класс, который до проксирования
            // и у него есть doStuff() метод, и у этого метода есть аннотация
            //todo
            return null;
        }*/
        //поэтому делаем вот что...
        //и можем убрать аннотацию из метода интрерфейса AInterface
       /* for (Method method1 : originalBaseEntity.getClass().getMethods()) {
            // находим метод с таким же названием, как и метод `method`
            if (method1.equals(method)) {
                if (method1.isAnnotationPresent(Size.class)) {
                    //todo
                   // return то, что будет соответствовать возвращаемому типу invoke(см. ниже);
                    return null;
                }
            }
        }*/

        // а иначе, выполнится оставшаяся часть кода
        // то есть, при определенныйх условиях, мы можем отменить стандартное действие, и заставить выполнить то, что нам надо


        // обращаемся к методу интерфейса(НУЖНО УЗНАТЬ, КАК ОБРАТИТСЯ К МЕТОДУ КЛАССА)
        //сейчас вызывается проксированный метод, а нужно, чтобы вызывалась аннотация
      /*  Object result = method.invoke(original, args);
        System.out.println("after");
        return result;*/
        return null;
//        System.out.println(proxy);
//        System.out.println(method);
//        System.out.println(args);
//        return method.invoke(proxy, args);
    }
}
