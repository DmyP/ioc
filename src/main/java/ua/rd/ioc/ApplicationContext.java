package ua.rd.ioc;

public class ApplicationContext implements Context {
    @Override
    public Object getBean(String beanName) {
        return null;
    }

    @Override
    public String[] getBeanDefenitionName() {
        return new String[0];
    }
}
