package zti.server.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import zti.server.sql.DataBaseConnection;
import zti.server.util.Util;

public class Stop implements Serializable {
	public Stop() {	}

	public Stop(Integer id, String name, Boolean nz, Float loc_x, Float loc_y, List<Integer> conns, List<Integer> times) {
		this.id = id;
		this.name = name;
		this.nz = nz;
		this.loc_x = loc_x;
		this.loc_y = loc_y;
		this.conns = conns;
		this.times = times;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public Boolean getNz() { return nz; }
	public void setNz(Boolean nz) { this.nz = nz; }
	
	public Float getLocX() { return loc_x; }
	public void setLocX(Float loc_x) { this.loc_x = loc_x; }
	
	public Float getLocY() { return loc_y; }
	public void setLocY(Float loc_y) { this.loc_y = loc_y; }
	
	public List<Integer> getConns() { return conns; }
	public void setConns(List<Integer> conns) { this.conns = conns; }
	
	public List<Integer> getTimes() { return times; }
	public void setTimes(List<Integer> times) { this.times = times; }
	
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

	private Integer id;
	private String name;
	private Boolean nz;
	private Float loc_x;
	private Float loc_y;
	private List<Integer> conns;
	private List<Integer> times;

	private static final long serialVersionUID = 1L;
}
