package evm.dmc.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties("views")
public class ViewsService {
	private String index;
	private String hello;
	private String hellorest;
	private String hellothyme;
	private String welcome;
	private String showtable;
	private String listbeans;
	private String createalg;
	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}
	/**
	 * @return the hello
	 */
	public String getHello() {
		return hello;
	}
	/**
	 * @param hello the hello to set
	 */
	public void setHello(String hello) {
		this.hello = hello;
	}
	/**
	 * @return the hellorest
	 */
	public String getHellorest() {
		return hellorest;
	}
	/**
	 * @param hellorest the hellorest to set
	 */
	public void setHellorest(String hellorest) {
		this.hellorest = hellorest;
	}
	/**
	 * @return the hellothyme
	 */
	public String getHellothyme() {
		return hellothyme;
	}
	/**
	 * @param hellothyme the hellothyme to set
	 */
	public void setHellothyme(String hellothyme) {
		this.hellothyme = hellothyme;
	}
	/**
	 * @return the welcome
	 */
	public String getWelcome() {
		return welcome;
	}
	/**
	 * @param welcome the welcome to set
	 */
	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}
	/**
	 * @return the showtable
	 */
	public String getShowtable() {
		return showtable;
	}
	/**
	 * @param showtable the showtable to set
	 */
	public void setShowtable(String showtable) {
		this.showtable = showtable;
	}
	/**
	 * @return the listbeans
	 */
	public String getListbeans() {
		return listbeans;
	}
	/**
	 * @param listbeans the listbeans to set
	 */
	public void setListbeans(String listbeans) {
		this.listbeans = listbeans;
	}
	/**
	 * @return the createalg
	 */
	public String getCreatealg() {
		return createalg;
	}
	/**
	 * @param createalg the createalg to set
	 */
	public void setCreatealg(String createalg) {
		this.createalg = createalg;
	}
	

}
