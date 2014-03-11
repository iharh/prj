package mdm.parser;

public class LcPair {
	
	private String object;
	private String attribute;
	private String positive;
	private int objStartPos;
	private int objEndPos;
	private int attrStartPos;
	private int attrEndPos;
	
	public LcPair() {
		
	}
	
	public LcPair(String object, String attribute, String positive) {
		this.object = object;
		this.attribute = attribute;
		this.positive = positive;
	}

	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getPositive() {
		return positive;
	}
	public void setPositive(String positive) {
		this.positive = positive;
	}
	public int getObjStartPos() {
		return objStartPos;
	}
	public void setObjStartPos(int objStartPos) {
		this.objStartPos = objStartPos;
	}
	public int getObjEndPos() {
		return objEndPos;
	}
	public void setObjEndPos(int objEndPos) {
		this.objEndPos = objEndPos;
	}
	public int getAttrStartPos() {
		return attrStartPos;
	}
	public void setAttrStartPos(int attrStartPos) {
		this.attrStartPos = attrStartPos;
	}
	public int getAttrEndPos() {
		return attrEndPos;
	}
	public void setAttrEndPos(int attrEndPos) {
		this.attrEndPos = attrEndPos;
	}
	
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("[object: ").append(object);
		buff.append(", attribute: ").append(attribute);
		buff.append(", positive: ").append(positive);
		buff.append(", objStartPos: ").append(objStartPos);
		buff.append(", objEndPos: ").append(objEndPos);
		buff.append(", attrStartPos: ").append(attrEndPos);
		buff.append(", attrEndPos: ").append(attrEndPos).append("]");
		return buff.toString();
	}
}
