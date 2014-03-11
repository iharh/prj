package mdm.parser;

import java.util.ArrayList;
import java.util.List;

public class QueryParserResult {
    private List<String> terms = new ArrayList<String>();
    
    private List<String> quotedTerms = new ArrayList<String>();
    
    private List<String> attributeTerms = new ArrayList<String>();
    
    private List<String> rangeAttributeTerms = new ArrayList<String>();
    
    private List<String> syntaxTerms = new ArrayList<String>();
    
    private List<LcPair> lcPairs = new ArrayList<LcPair>();
    

    public List<String> getSyntaxTerms() {
        return syntaxTerms;
    }

    public void setSyntaxTerms(List<String> syntaxTerms) {
        this.syntaxTerms = syntaxTerms;
    }

    public List<String> getTerms() {
        return terms;
    }

    public void setTerms(List<String> terms) {
        this.terms = terms;
    }

    public List<String> getQuotedTerms() {
        return quotedTerms;
    }

    public void setQuotedTerms(List<String> syntaxTrms) {
        this.quotedTerms = syntaxTrms;
    }

    public List<String> getAttributeTerms() {
        return attributeTerms;
    }

    public void setAttributeTerms(List<String> attributeTerms) {
        this.attributeTerms = attributeTerms;
    }
    
    public void addTerm(String term) {
        
        terms.add(term);
    }
    
    public void addQuotedTerm(String term) {
        
        quotedTerms.add(term);
    }
    
    public void addAttributeTerm(String term) {
    
        attributeTerms.add(term);
    }
    
    public void addSyntaxTerm(String term) {
        
        syntaxTerms.add(term);
    }
    
    public List<LcPair> getLcPairs() {
		return lcPairs;
	}

	public void setLcPairs(List<LcPair> lcPairs) {
		this.lcPairs = lcPairs;
	}
	
	public void addLcPair(LcPair pair) {
        lcPairs.add(pair);
    }
	
    public List<String> getRangeAttributeTerms() {
        return rangeAttributeTerms;
    }

    public void addRangeAttributeTerm(String term) {
	rangeAttributeTerms.add(term);
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("Terms: ");
        for (String term : terms) {
            result.append(term + ", ");
        }
        result.append("Attribute Terms: ");
        for (String term : attributeTerms) {
            result.append(term + ", ");
        }
        result.append("Syntax Terms: ");
        for (String term : syntaxTerms) {
            result.append(term + ", ");
        }
        result.append("Quoted Terms: ");
        for (String term : quotedTerms) {
            result.append(term + ", ");
        }
        
        result.append(" LcPars: ").append(lcPairs.toString());
        
        return result.toString();
    }
}
