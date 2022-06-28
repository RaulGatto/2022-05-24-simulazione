/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.Adiacenza;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaLista"
    private Button btnCreaLista; // Value injected by FXMLLoader

    @FXML // fx:id="btnMassimo"
    private Button btnMassimo; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCanzone"
    private ComboBox<Track> cmbCanzone; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<Genre> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtMemoria"
    private TextField txtMemoria; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void btnCreaLista(ActionEvent event) {
    	txtResult.clear();
    	Track trackIniziale = this.cmbCanzone.getValue();
    	if(trackIniziale == null) {
    		txtResult.setText("Selezionare una track valida");
    		return;
    	}
    	String memoriaMassimaStr = this.txtMemoria.getText();
    	if(memoriaMassimaStr.equals("") || memoriaMassimaStr == null) {
    		txtResult.setText("Inserire un valore numerico!");
    		return;
    	}
    	int memoriaMassima;
    	try {
    		memoriaMassima = Integer.parseInt(memoriaMassimaStr);
    	}catch(NumberFormatException e) {
    		e.printStackTrace();
    		System.out.println("Errore generato da te");
    		txtResult.setText("Inserire solamente numeri nel campo memoria!");
    		return;
    	}
    	if(memoriaMassima < 30000) {	
    		txtResult.setText("Memoria troppo piccola per memorizzare qualsiasi canzone");
    		return;
    	}
    	List<Track> tracks = this.model.trovaPercorso(trackIniziale, memoriaMassima);
    	
    	for(Track tr : tracks) {
    		txtResult.appendText(String.format("%s\n", tr.getName()));
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	this.cmbCanzone.getItems().clear();
    	Genre g = this.cmbGenere.getValue();
    	if(g == null) {
    		txtResult.setText("Selezionare un genere dalla tendina!");
    		return;
    	}
    	String res = this.model.creaGrafo(g);
    	if(res.compareTo("") == 0|| res == null) {
    		txtResult.setText("Qualcosa e' andato storto");
    		return;
    	}
    	txtResult.setText(res +"\n");
    	List<Track> canzoni = this.model.getVertici();
    	if(canzoni.size() != 0 && canzoni != null) {
    		this.cmbCanzone.getItems().addAll(canzoni);
    	}
    }

    @FXML
    void doDeltaMassimo(ActionEvent event) {
    	txtResult.clear();
    	List<Adiacenza> res = this.model.getDurataMassima();
    	if(res.size() == 0 || res == null) {
    		txtResult.setText("Qualcosa e' andato storto");
    		return;
    	}
    	txtResult.setText("La/le canzoni con delta massimo sono: \n");
    	for(Adiacenza a : res) {
    		txtResult.appendText(String.format("%s, %s, %f\n", a.getT1(), a.getT2(), a.getPeso()));
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaLista != null : "fx:id=\"btnCreaLista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnMassimo != null : "fx:id=\"btnMassimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCanzone != null : "fx:id=\"cmbCanzone\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMemoria != null : "fx:id=\"txtMemoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbGenere.getItems().setAll(this.model.getAllGenres());
    }

}
