package mygwt.foundation.client.widget.list;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptGroupElement;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;

/**
 * Extends the standard GWT ListBox to automatically provide OPTGROUP
 * elements to group sections of options.
 * <p>
 * Rather than provide a separate API, it uses the names of the OPTION
 * elements to establish the grouping using a simple syntax. The text
 * before the first "|" character is used as the group name and is
 * used to group elements inside an OPTGROUP of that name. The rest of
 * the text is used as the text of the OPTION.
 * <p>
 * It uses "doubling" for escaping so if "||" appears in the 
 * name, it is converted to a single "|" and the first single "|"
 * is used as the delimiter.
 * <p>
 * As a simple example, in a normal listbox I might have:
 * 
 * <pre>
 * - Item 1A
 * - Item 2A
 * - Item 1B
 * - Item 2B
 * </pre>
 * <p>
 * You could imaging using a text prefix to represent groups so that
 * you have:
 * 
 * <pre>
 * - Group A | Item 1
 * - Group A | Item 2
 * - Group B | Item 1
 * - Group B | Item 2
 * </pre>
 * <p>
 * The ListBox would get wide and hard to read. But if you add those
 * same items to a GroupedListBox, it will create OPTGROUPS
 * automatically so that you will have:
 * 
 * <pre>
 * - Group A
 * -- Item 1
 * -- Item 2
 * - Group B
 * -- Item 1
 * -- Item 2
 * </pre>
 * <p>
 * With regard to indexes and selection, it will mostly work the same
 * as a normal ListBox. The one difference is that it will not repeat
 * groups. This means that if you add the items in this order:
 * 
 * <pre>
 * - Group A | Item 1
 * - Group B | Item 1
 * - Group A | Item 2
 * - Group B | Item 2
 * </pre>
 * 
 * then you will rearrange the items to group them:
 * 
 * <pre>
 * - Group A
 * -- Item 1
 * -- Item 2
 * - Group B
 * -- Item 1
 * -- Item 2
 * </pre>
 * 
 * Note: Though it can be used with a multiple select control, this
 * extends SingleListBox to provide {@link #setValue(String)} and
 * other useful methods.
 * 
 */
public class GroupedListBox extends SingleListBox {
    public GroupedListBox() {
    }
    
    public GroupedListBox(boolean isMultipleSelect) {
        super(true,isMultipleSelect);
    }
    
    @Override
    public void clear() {
        //super.clear(); // commented due to CMP-30873, CMP-30902 - exception in IE9-10
        
        // we need special handling to remove any OPTGROUP elements
        Element elm = getElement();
        while (elm.hasChildNodes()) {
            elm.removeChild(elm.getFirstChild());
        }
    }

    @Override
    public int getItemCount() {
        return getElement().getElementsByTagName("OPTION").getLength(); //$NON-NLS-1$
    }

    @Override
    public String getItemText(int index) {
        OptionElement opt = getOption(index);
        return opt.getInnerText();
    }
    
    @Override
    public int getSelectedIndex() {
        int sz = getItemCount();
        for (int i = 0; i < sz; ++i) {
            if (getOption(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String getValue(int index) {
        OptionElement option = getOption(index);
        return option.getValue();
    }

    public void insertItem(String group, String item, String value) {
    	insertItem(group + "|" + item, value, -1);
    }

    @Override
    public void insertItem(String item, String value, int index) {
        // find the delimiter if there is one
        int pipe = (item != null) ? item.indexOf('|') : -1;
        while (pipe != -1 && pipe + 1 != item.length() && item.charAt(pipe + 1) == '|') {
            pipe = item.indexOf('|', pipe + 2);
        }

        // extract the group if we found a delimiter
        String group = null;
        if (pipe != -1) {
            group = item.substring(0, pipe).trim();
            item = item.substring(pipe + 1).trim();
            
            // make sure we convert || -> | in the group name
            group = group.replace("||", "|");
        }
        // convert || -> | in the item name
        item = item.replace("||", "|");

        Element parent = getSelectElement();
        Node before = null;
        
        if (group != null) {
            OptGroupElement optgroup = findOptGroupElement(group);
            if (optgroup != null) {
                // add it to this optgroup
                parent = optgroup;

                // adjust the index to inside the group
                int adjusted = getIndexInGroup(group, index);
                
                // we had a real index (wasn't negative which means
                // add to the end), but it was too low for this group.
                // put it at the beginning of the group.
                if (adjusted < 0 && index >= 0) {
                    adjusted = 0;
                }
                
                // check the range and if it's out of range, we'll
                // just add it to the end
                // of the group (before == null)
                if (0 <= adjusted && adjusted < optgroup.getChildCount()) {
                    before = optgroup.getChild(adjusted); 
                }
            } else {
                // add a new group and add the item to it
                optgroup = Document.get().createOptGroupElement();
                optgroup.setLabel(group);
                parent.appendChild(optgroup);
                parent = optgroup;
                before = null;
            }
        }
        else {
            // make sure we're not past the initial "group" of
            // ungrouped options
            int max = getIndexOfFirstGroup();
            if (index < 0 || index > max) {
                before = (max < parent.getChildCount()) ? parent.getChild(max) : null;
            }
            else if (0 <= index && index < parent.getChildCount()) {
                before = parent.getChild(index);
            }
        }
        
        OptionElement option = createOption(item, value);
        parent.insertBefore(option, before);
    }


    @Override
    public boolean isItemSelected(int index) {
        OptionElement option = getOption(index);
        return (option != null) ? option.isSelected() : false;
    }

    @Override
    public void removeItem(int index) {
        OptionElement option = getOption(index);
        Element parent = option.getParentElement();
        option.removeFromParent();
        
        // remove empty OPTGROUPs
        if ("OPTGROUP".equals(parent.getTagName()) && !parent.hasChildNodes()) {
            parent.removeFromParent();
        }
    }

    @Override
    public void setItemSelected(int index, boolean selected) {
        OptionElement option = getOption(index);
        option.setSelected(selected);
    }

    @Override
    public void setItemText(int index, String text) {
        if (text == null) {
            throw new NullPointerException("Cannot set an option to have null text");
        }
        OptionElement option = getOption(index);
        option.setText(text);
    }

    @Override
    public void setSelectedIndex(int index) {
        int sz = getItemCount();
        for (int i = 0; i < sz; ++i) {
            getOption(i).setSelected(i == index);
        }
        if (index < 0) {
            getSelectElement().setSelectedIndex(index);
        }
    }

    @Override
    public void setValue(int index, String value) {
        OptionElement option = getOption(index);
        option.setValue(value);
    }

    protected SelectElement getSelectElement() {
        return getElement().cast();
    }
    
    protected void checkIndex(int index) {
        if (index < 0 || index >= getItemCount()) {
            throw new IndexOutOfBoundsException(index+" out of range [0-"+(getItemCount()-1)+"]"); //$NON-NLS-1$
        }
    }

    // Convenience for dealing with the DOM directly instead of using
    // SelectElement.getOptions, etc
    
    protected OptionElement getOption(int index) {
        checkIndex(index);
        
        // first check ungrouped
        Element elm = getElement();
        int sz = elm.getChildCount();
        int firstGroup = getIndexOfFirstGroup();
        if (index >= 0 && index < firstGroup && index < sz) {
            return option(elm.getChild(index));
        }

        // then go through the groups
        int childIndex = index - firstGroup;
        for (int i = firstGroup; i < sz; ++i) {
            Node child = elm.getChild(i);
            if (isGroup(child)) {
                if (childIndex < child.getChildCount()) {
                    return option(child.getChild(childIndex));
                }
                else {
                    childIndex -= child.getChildCount();
                }
            }
        }
        
        throw new IndexOutOfBoundsException("problem in getOption: index="+index+" range=[0-"+(getItemCount()-1)+"]");
    }
    
    private OptionElement option(Node node) {
        if (node == null) return null;
        return OptionElement.as(Element.as(node));
    }

    private OptGroupElement optgroup(Node node) {
        if (node == null) return null;
        return OptGroupElement.as(Element.as(node));
    }

    protected int getIndexOfFirstGroup() {
        Element elm = getElement();
        int sz = elm.getChildCount();
        for (int i = 0; i < sz; i++) {
            if (isGroup(elm.getChild(i))) {
                return i;
            }
        }
        return sz;
    }
    
    protected boolean isGroup(Node node) {
        return "OPTGROUP".equals(node.getNodeName());
    }
    
    protected boolean isMatchingGroup(Node child, String group) {
        if (isGroup(child)) {
            OptGroupElement optgroup = optgroup(child);
            return group.equals(optgroup.getLabel());
        }
        else {
            return false;
        }
    }
    
    protected OptGroupElement findOptGroupElement(String name) {
        if (name == null) return null;
        NodeList<Element> optgroups = getElement().getElementsByTagName("OPTGROUP");
        for (int i = 0; i < optgroups.getLength(); ++i) {
            Element optgroup = optgroups.getItem(i);
            if (isMatchingGroup(optgroup, name)) {
                return OptGroupElement.as(optgroup);
            }
        }
        return null;
    }
        
    protected int getIndexInGroup(String group, int index) {
        if (group == null) return index;
        
        int adjusted = index;
        Element elm = getElement();
        int sz = elm.getChildCount();
        for (int i = 0; i < sz; ++i) {
            Node child = elm.getChild(i);
            if (isMatchingGroup(child, group)) {
                break;
            }
            if (isGroup(child)) {
                adjusted -= child.getChildCount();
            }
            else {
                adjusted -= 1;
            }
        }       
        return adjusted;
    }
    
    protected OptionElement createOption(String item, String value) {
        OptionElement option = Document.get().createOptionElement();
        option.setText(item);
        option.setInnerText(item);
        option.setValue(value);
        return option;
    }
}
