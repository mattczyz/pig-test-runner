package pig.testing.tests;


public class TestExecutorFactory {
    public static TestExecutor get(String executorClass) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        TestExecutor test = null;
        Class<?> testExecutor;
   
        if(executorClass == null)
            executorClass = "DirContentEqual";
        
        if(isClass("pig.testing.tests." + executorClass)){
            testExecutor = Class.forName("pig.testing.tests." + executorClass);
        } else if (isClass(executorClass)){
            testExecutor = Class.forName(executorClass);
        } else {
            throw new ClassNotFoundException(executorClass);
        }
                
        test = (TestExecutor) testExecutor.newInstance();

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
