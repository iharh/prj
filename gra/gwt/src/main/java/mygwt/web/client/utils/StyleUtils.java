package mygwt.web.client.utils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;

public class StyleUtils {
    public static abstract class ElementSelector {
        public abstract Element select(Element element);
    }
	
    public static class ParentSelector extends ElementSelector {
        private int level;
        
        public ParentSelector(int level) {
            this.level = level;
        }
        
        @Override
        public Element select(Element element) {
            Element result = element;
            int currentLevel = 0;
            
            while (currentLevel++ < level) {
                result = result.getParentElement();
            }
            
            return result;
        }
    }
	
    public static final ParentSelector FIRST_PARENT = new ParentSelector(1);
    public static final ParentSelector SECOND_PARENT = new ParentSelector(2);
    
    public interface StyleChanger {
        public void changeStyle(Style style);
    }
    
    public static void changeStyle(Element element, ElementSelector selector, StyleChanger styleChanger) {
        Element target = safeNullResolve(element, selector);
        styleChanger.changeStyle(target.getStyle());
    }
    
    public static void addClass(Element element, ElementSelector selector, String className) {
        Element target = safeNullResolve(element, selector);
        target.addClassName(className);
    }
    
    public static void removeClass(Element element, ElementSelector selector, String className) {
        Element target = safeNullResolve(element, selector);
        target.removeClassName(className);
    }
    
    public static void clearClasses(Element element, ElementSelector selector) {
        Element target = safeNullResolve(element, selector);
        String className = target.getClassName();
        if (className != null && className.trim().length() > 0) {
            target.removeClassName(className);
        }
    }
    
    private static Element safeNullResolve(Element element, ElementSelector selector) {
        Element target = element;
        if (selector != null) {
            target = selector.select(target);
        }
        return target;
    }
}
