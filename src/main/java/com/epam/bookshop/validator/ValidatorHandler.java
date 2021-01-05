//package org.mycompany.bookshop.validator;
//
//import org.mycompany.bookshop.context.annotation.Validatable;
//
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//
//public class ValidatorHandler implements InvocationHandler {
//
//    private final Entity original;
//
//    public ValidatorHandler(Entity original) {
//        this.original = original;
//    }
//
//
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        System.out.println("In validatable handler");
//
//        for (Method method1 : original.getClass().getMethods()) {
//            if (method1.equals(method)) {
//                if (method1.isAnnotationPresent(Validatable.class)) {
//                    System.out.println(method + "----------------");
//                    //todo
//                    // return то, что будет соответствовать возвращаемому типу invoke(см. ниже);
//                    return null;
//                }
//            }
//        }
//        return null;
//    }
//}
