package zti.server.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import zti.server.util.Util;

/**
 * @author Mateusz Winiarski
 * Klasa reprezentujaca przystanek
 */
@Entity
@Table(name = "stops")
@NamedQuery(name="findAllStops", query="SELECT s FROM Stop s ORDER BY s.id")
public class Stop implements Serializable {
	/**
	 * Konstruktor domyslny
	 */
	public Stop() {	}

	/**
	 * Konstruktor
	 * @param id ID przystanku
	 * @param name Nazwa przystanku
	 * @param nz czy przystanek jest na rzadanie
	 * @param loc_x Wspolrzedna X
	 * @param loc_y Wspolrzedna Y
	 * @param conns Polaczone przystanki
	 * @param times Czasy przejazdow do polaczonych przystankow
	 */
	public Stop(Integer id, String name, Boolean nz, Float loc_x, Float loc_y, List<Integer> conns, List<Integer> times) {
		this.id = id;
		this.name = name;
		this.nz = nz;
		this.loc_x = loc_x;
		this.loc_y = loc_y;
		this.conns = conns;
		this.times = times;
	}
	
	/**
	 * @return ID przystanku
	 */
	public Integer getId() { return id; }
	
	/**
	 * @param id noew ID przystanku
	 */
	public void setId(Integer id) { this.id = id; }
	
	/**
	 * @return Nazwa przystanku
	 */
	public String getName() { return name; }
	
	/**
	 * @param name nowa nazwa przystanku
	 */
	public void setName(String name) { this.name = name; }
	
	/**
	 * @return czy przystanek jest na rzadanie
	 */
	public Boolean getNz() { return nz; }
	
	/**
	 * @param nz czy przystanek mabyc na rzadanie
	 */
	public void setNz(Boolean nz) { this.nz = nz; }
	
	/**
	 * @return Wspolrzedna X
	 */
	@Column(name="loc_x")
	public Float getLocX() { return loc_x; }
	
	/**
	 * @param loc_x nowa wspolrzedna X
	 */
	public void setLocX(Float loc_x) { this.loc_x = loc_x; }
	
	/**
	 * @return Wspolrzedna Y
	 */
	@Column(name="loc_y")
	public Float getLocY() { return loc_y; }
	
	/**
	 * @param loc_y nowa wspolrzedna Y
	 */
	public void setLocY(Float loc_y) { this.loc_y = loc_y; }
	
	/**
	 * @return Polaczone przystanki
	 */
	public List<Integer> getConns() { return conns; }
	
	/**
	 * @param conns noew polaczone przystanki
	 */
	public void setConns(List<Integer> conns) { this.conns = conns; }
	
	/**
	 * @return Czasy przejazdow do polaczonych przystankow
	 */
	public List<Integer> getTimes() { return times; }
	
	/**
	 * @param times noew czasy przejazdow do polaczonych przystankow
	 */
	public void setTimes(List<Integer> times) { this.times = times; }
	
	/**
	 * Przeksztalca obiekt do postaci elementu XML
	 * @param doc Dokument w jakim generowany jest XML
	 * @return wygenerowany element XML
	 */
	public Element toXml(Document doc) {
		Element stopElement = doc.createElement(Constants.STOP);
		
		stopElement.setAttribute(Constants.ID, id.toString());
		stopElement.appendChild(Util.createElement(doc, Constants.NAME, name));
		stopElement.appendChild(Util.createElement(doc, Constants.NZ, nz.toString()));
		stopElement.appendChild(Util.createElement(doc, Constants.LOC_X, loc_x.toString()));
		stopElement.appendChild(Util.createElement(doc, Constants.LOC_Y, loc_y.toString()));
		
		Element connsElement = doc.createElement(Constants.CONNS);
		for (Integer conn : conns) {
			connsElement.appendChild(Util.createElement(doc, Constants.CONN, conn.toString()));
		}
		stopElement.appendChild(connsElement);
		
		return stopElement;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private Boolean nz;
	private Float loc_x;
	private Float loc_y;
	
	@Column(name = Constants.CONNS, columnDefinition = "int[]")
	@Convert(converter = zti.server.data.converter.IntListToArrayConveter.class)
	private List<Integer> conns;
	
	@Column(name = Constants.TIMES, columnDefinition = "int[]")
	@Convert(converter = zti.server.data.converter.IntListToArrayConveter.class)
	private List<Integer> times;

	private static final long serialVersionUID = 1L;
}
