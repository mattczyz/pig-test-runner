package pig.testing.validation;


public class ResultValidatorFactory {
    public static ResultValidator get(String executorClass) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        ResultValidator test = null;
        Class<?> testExecutor;
   
        if(executorClass == null)
            executorClass = DirContentValidator.class.getSimpleName();
        
        if(isClass("pig.testing.validation." + executorClass)){
            testExecutor = Class.forName("pig.testing.validation." + executorClass);
        } else if (isClass(executorClass)){
            testExecutor = Class.forName(executorClass);
        } else {
            throw new ClassNotFoundException(executorClass);
        }
                
        test = (ResultValidator) testExecutor.newInstance();

        return test;
    }
    
    private static boolean isClass(String className) {
        try  {
            Class.forName(className);
            return true;
        }  catch (final ClassNotFoundException e) {
            return false;
        }
    }
}
