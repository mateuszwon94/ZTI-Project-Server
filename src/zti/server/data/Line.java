package zti.server.data;

import java.sql.Time;
import java.io.Serializable;
import javax.persistence.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import zti.server.sql.DataBaseConnection;
import zti.server.util.Util;


public class Line implements Serializable {
	public Line() { }
	
	public Line(Integer number, String[] variants, Integer[] route, Integer f_peak, Integer f_not_peak, Time first, Time last) {
		this.number = number;
		this.variants = variants;
		this.route = route;
		this.f_peak = f_peak;
		this.f_not_peak = f_not_peak;
		this.first = first;
		this.last = last;
	}
	
	public Integer getNumber() { return number; }
	public void setNumber(Integer number) { this.number = number; }
	
	public String[] getVariants() { return variants; }
	public void setVariants(String[] variants) { this.variants = variants; }
	
	public Integer[] getRoute() { return route; }
	public void setRoute(Integer[] route) { this.route = route; }
	
	public Integer getFPeak() { return f_peak; }
	public void setFPeak(Integer f_peak) { this.f_peak = f_peak; }
	
	public Integer getFNotPeak() { return f_not_peak; }
	public void setFNotPeak(Integer f_not_peak) { this.f_not_peak = f_not_peak; }

	public Time getFirst() { return first; }
	public void setFirst(Time first) { this.first = first; }
	
	public Time getLast() { return last; }
	public void setLast(Time last) { this.last = last; }
	
	public Element toXml(Document doc) {
		Element lineElement = doc.createElement(Constants.LINE);
		lineElement.setAttribute(Constants.NUMBER, number.toString());
		try {
			Element variantsElement = doc.createElement(Constants.VARIANTS);
			for (String variant : variants)
				variantsElement.appendChild(Util.createElement(doc, Constants.VARIANT, variant));
			lineElement.appendChild(variantsElement);
		} catch (NullPointerException ex) { }
		Element routeElement = doc.createElement(Constants.ROUTE);
		for (Integer stop : route) {
			routeElement.appendChild(Util.createElement(doc, Constants.STOP, stop.toString()));
		}
		lineElement.appendChild(routeElement);
		
		lineElement.appendChild(Util.createElement(doc, Constants.F_PEAK, f_peak.toString()));
		lineElement.appendChild(Util.createElement(doc, Constants.F_NOT_PEAK, f_not_peak.toString()));
		lineElement.appendChild(Util.createElement(doc, Constants.FIRST, first.toString()));
		lineElement.appendChild(Util.createElement(doc, Constants.LAST, last.toString()));
		
		return lineElement;
	}
	
	private Integer number;
	private String[] variants;
	private Integer[] route;
	private Integer f_peak;
	private Integer f_not_peak;
	private Time first;
	private Time last;
	
	private static final long serialVersionUID = 1L;
}