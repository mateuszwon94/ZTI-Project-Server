package zti.server.data;

import java.sql.Time;
import java.util.List;
import java.io.Serializable;
import javax.persistence.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import zti.server.util.Util;


/**
 * @author Mateusz Winiarski
 * Klasa reprezentujaca linie
 */
/**
 * @author mateu
 *
 */
@Entity
@Table(name = "lines")
@NamedQuery(name="findAllLines", query="SELECT l FROM Line l ORDER BY l.number")
public class Line implements Serializable {
	/**
	 * Konstruktor domyslny
	 */
	public Line() { }
	
	/**
	 * Konstruktor
	 * @param number numer linii
	 * @param variants lista nazw wariantow linii
	 * @param route trasa linii
	 * @param f_peak czestotliwosc w szczycie
	 * @param f_not_peak czestotliwosc poza szczytem
	 * @param first pierwszy odjazd
	 * @param last ostatni odjazd
	 */
	public Line(Integer number, List<String> variants, List<Integer> route, Integer f_peak, Integer f_not_peak, Time first, Time last) {
		this.number = number;
		this.variants = variants;
		this.route = route;
		this.f_peak = f_peak;
		this.f_not_peak = f_not_peak;
		this.first = first;
		this.last = last;
	}
	
	/**
	 * @return Numer linii
	 */
	public Integer getNumber() { return number; }
	
	/**
	 * @param number nowy numer linii
	 */
	public void setNumber(Integer number) { this.number = number; }
	
	/**
	 * @return nazwy wariantow linii
	 */
	public List<String> getVariants() { return variants; }
	
	/**
	 * @param variants nowe azwy warIanto linii
	 */
	public void setVariants(List<String> variants) { this.variants = variants; }
	
	/**
	 * @return trasa linii
	 */
	public List<Integer> getRoute() { return route; }
	
	/**
	 * @param route nowa trasa linii
	 */
	public void setRoute(List<Integer> route) { this.route = route; }
	
	/**
	 * @return czestotliwosc linii w szczycie
	 */
	public Integer getFPeak() { return f_peak; }
	
	/**
	 * @param f_peak nowa czestotliwosc linii w szczycie
	 */
	public void setFPeak(Integer f_peak) { this.f_peak = f_peak; }
	
	/**
	 * @return czestotliwosc linii poza szczytem
	 */
	public Integer getFNotPeak() { return f_not_peak; }
	
	/**
	 * @param f_not_peak nowa czestotliwocs lini poza szczytem
	 */
	public void setFNotPeak(Integer f_not_peak) { this.f_not_peak = f_not_peak; }

	/**
	 * @return pierwszy odjazd
	 */
	public Time getFirst() { return first; }
	
	/**
	 * @param first nowy pierwszy odjazd
	 */
	public void setFirst(Time first) { this.first = first; }
	
	/**
	 * @return ostatni odjazd
	 */
	public Time getLast() { return last; }
	
	/**
	 * @param last nowy ostatni odjazd
	 */
	public void setLast(Time last) { this.last = last; }
	
	/**
	 * Przeksztalca obiekt do postaci elementu XML
	 * @param doc Dokument w jakim generowany jest XML
	 * @return wygenerowany element XML
	 */
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
	
	@Id
	private Integer number;
	
	@Column(name = Constants.VARIANTS, columnDefinition = "text[]")
	@Convert(converter = zti.server.data.converter.TextListToArrayConveter.class)
	private List<String> variants;
	
	@Column(name = Constants.ROUTE, columnDefinition = "int[]")
	@Convert(converter = zti.server.data.converter.IntListToArrayConveter.class)
	private List<Integer> route;
	private Integer f_peak;
	private Integer f_not_peak;
	private Time first;
	private Time last;
	
	private static final long serialVersionUID = 1L;
}
